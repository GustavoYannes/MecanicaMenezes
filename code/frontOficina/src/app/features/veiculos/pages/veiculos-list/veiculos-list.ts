import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { VeiculoService } from '../../services/veiculo.service';
import { VeiculoListItem } from '../../models/veiculo-list-item.model';
import { SearchInput } from '../../../../shared/components/search-input/search-input';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { VeiculoCard } from '../../components/veiculo-card/veiculo-card';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { StatusBadge } from '../../../../shared/components/status-badge/status-badge';
import { VehicleModalService } from '../../services/vehicle-modal.service';

@Component({
  selector: 'app-veiculos-list',
  standalone: true,
  imports: [
    CommonModule, 
    SearchInput, 
    DataTable, 
    VeiculoCard, 
    Pagination,
    StatusBadge
  ],
  templateUrl: './veiculos-list.html'
})
export class VeiculosList implements OnInit {
  private veiculoService = inject(VeiculoService);
  private modalService = inject(VehicleModalService);
  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);

  veiculos: VeiculoListItem[] = [];
  loading = false;
  error = false;
  empty = false;

  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  first = true;
  last = true;
  
  searchQuery = '';

  ngOnInit() {
    this.loadVeiculos();
  }

  loadVeiculos() {
    this.loading = true;
    this.error = false;
    this.empty = false;

    this.veiculoService.findAllVeiculos(this.currentPage, this.searchQuery)
      .subscribe({
        next: (response: any) => {
          this.veiculos = response?.content || (Array.isArray(response) ? response : []);
          this.totalPages = response?.totalPages || 1;
          this.totalElements = response?.totalElements || this.veiculos.length;
          this.first = response?.first ?? true;
          this.last = response?.last ?? true;
          this.loading = false;
          
          if (!this.veiculos || this.veiculos.length === 0) {
            this.empty = true;
          }
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Erro ao buscar veículos:', err);
          this.error = true;
          this.loading = false;
          this.cdr.markForCheck();
        }
      });
  }

  onSearch(query: string) {
    this.searchQuery = query;
    this.currentPage = 0;
    this.loadVeiculos();
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.loadVeiculos();
  }

  openRegistrarVeiculo() {
    this.modalService.open();
  }

  viewDetails(placa: string) {
    this.router.navigate(['/veiculos', placa]);
  }
}
