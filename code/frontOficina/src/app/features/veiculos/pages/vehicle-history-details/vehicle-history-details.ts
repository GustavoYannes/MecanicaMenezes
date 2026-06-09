import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { VeiculoService } from '../../services/veiculo.service';
import { EntradaService } from '../../services/entrada.service';
import { VehicleInfoCard } from '../../components/vehicle-info-card/vehicle-info-card';
import { ClientInfoCard } from '../../components/client-info-card/client-info-card';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { StatusBadge } from '../../../../shared/components/status-badge/status-badge';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { EntryDetailsModal } from '../../components/entry-details-modal/entry-details-modal';
import { VeiculoDetail } from '../../models/veiculo-detail.model';

@Component({
  selector: 'app-vehicle-history-details',
  standalone: true,
  imports: [
    CommonModule, 
    VehicleInfoCard, 
    ClientInfoCard, 
    DataTable, 
    StatusBadge, 
    Pagination,
    EntryDetailsModal
  ],
  templateUrl: './vehicle-history-details.html'
})
export class VehicleHistoryDetails implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private veiculoService = inject(VeiculoService);
  private entradaService = inject(EntradaService);

  placa: string = '';
  
  // State signals
  veiculo = signal<VeiculoDetail | null>(null);
  loadingVehicle = signal(true);
  errorVehicle = signal(false);

  entradas = signal<any[]>([]);
  loadingEntradas = signal(true);
  errorEntradas = signal(false);
  emptyEntradas = signal(false);

  // Pagination for entradas
  currentPage = signal(0);
  totalPages = signal(0);
  totalElements = signal(0);
  first = signal(true);
  last = signal(true);

  // Modal State
  selectedEntrada = signal<any>(null);
  showModal = signal(false);

  ngOnInit() {
    this.placa = this.route.snapshot.paramMap.get('placa') || '';
    if (this.placa) {
      this.loadVehicleDetails();
      this.loadEntradas();
    } else {
      this.goBack();
    }
  }

  loadVehicleDetails() {
    this.loadingVehicle.set(true);
    this.errorVehicle.set(false);
    this.veiculoService.getVeiculoByPlaca(this.placa).subscribe({
      next: (data) => {
        this.veiculo.set(data);
        this.loadingVehicle.set(false);
      },
      error: (err) => {
        console.error('Erro ao buscar veículo', err);
        this.errorVehicle.set(true);
        this.loadingVehicle.set(false);
      }
    });
  }

  loadEntradas() {
    this.loadingEntradas.set(true);
    this.errorEntradas.set(false);
    this.emptyEntradas.set(false);

    this.entradaService.getEntradasPorVeiculo(this.placa, this.currentPage()).subscribe({
      next: (response: any) => {
        const items = response?.content || (Array.isArray(response) ? response : []);
        this.entradas.set(items);
        this.totalPages.set(response?.totalPages || 1);
        this.totalElements.set(response?.totalElements || items.length);
        this.first.set(response?.first ?? true);
        this.last.set(response?.last ?? true);
        this.loadingEntradas.set(false);

        if (!items || items.length === 0) {
          this.emptyEntradas.set(true);
        }
      },
      error: (err) => {
        console.error('Erro ao buscar entradas', err);
        this.errorEntradas.set(true);
        this.loadingEntradas.set(false);
      }
    });
  }

  onPageChange(page: number) {
    this.currentPage.set(page);
    this.loadEntradas();
  }

  openEntryDetails(entrada: any) {
    this.selectedEntrada.set(entrada);
    this.showModal.set(true);
  }

  closeModal() {
    this.showModal.set(false);
    this.selectedEntrada.set(null);
  }

  goBack() {
    this.router.navigate(['/historico-veiculos']);
  }
}
