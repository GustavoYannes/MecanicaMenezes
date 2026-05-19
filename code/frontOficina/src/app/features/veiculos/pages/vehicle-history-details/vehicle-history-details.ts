import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
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
  private cdr = inject(ChangeDetectorRef);

  placa: string = '';
  veiculo: VeiculoDetail | null = null;
  loadingVehicle = true;
  errorVehicle = false;

  entradas: any[] = [];
  loadingEntradas = true;
  errorEntradas = false;
  emptyEntradas = false;

  // Pagination for entradas
  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  first = true;
  last = true;

  // Modal State
  selectedEntrada: any = null;
  showModal = false;

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
    this.loadingVehicle = true;
    this.errorVehicle = false;
    this.veiculoService.getVeiculoByPlaca(this.placa).subscribe({
      next: (data) => {
        this.veiculo = data;
        this.loadingVehicle = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erro ao buscar veículo', err);
        this.errorVehicle = true;
        this.loadingVehicle = false;
        this.cdr.markForCheck();
      }
    });
  }

  loadEntradas() {
    this.loadingEntradas = true;
    this.errorEntradas = false;
    this.emptyEntradas = false;

    this.entradaService.getEntradasPorVeiculo(this.placa, this.currentPage).subscribe({
      next: (response: any) => {
        this.entradas = response?.content || (Array.isArray(response) ? response : []);
        this.totalPages = response?.totalPages || 1;
        this.totalElements = response?.totalElements || this.entradas.length;
        this.first = response?.first ?? true;
        this.last = response?.last ?? true;
        this.loadingEntradas = false;

        if (!this.entradas || this.entradas.length === 0) {
          this.emptyEntradas = true;
        }
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erro ao buscar entradas', err);
        this.errorEntradas = true;
        this.loadingEntradas = false;
        this.cdr.markForCheck();
      }
    });
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.loadEntradas();
  }

  openEntryDetails(entrada: any) {
    this.selectedEntrada = entrada;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.selectedEntrada = null;
  }

  goBack() {
    this.router.navigate(['/historico-veiculos']);
  }
}
