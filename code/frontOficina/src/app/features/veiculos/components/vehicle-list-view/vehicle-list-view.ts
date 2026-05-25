import { Component, OnInit, Input, inject, ChangeDetectorRef, SimpleChanges, OnChanges } from '@angular/core';
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
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-vehicle-list-view',
  standalone: true,
  imports: [
    CommonModule,
    SearchInput,
    DataTable,
    VeiculoCard,
    Pagination,
    StatusBadge,
    FormsModule
  ],
  templateUrl: './vehicle-list-view.html'
})
export class VehicleListView implements OnInit, OnChanges {
  @Input() title: string = 'Veículos';
  @Input() subtitle: string = 'Lista de veículos';
  @Input() defaultStatuses: string[] = [];
  @Input() showStatusFilter: boolean = false;
  @Input() enableRowClick: boolean = true;
  @Input() showRegisterButton: boolean = true;
  @Input() baseRoute: string = '/veiculos';

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
  selectedStatusFilter = ''; // '' means 'Todos'

  ngOnInit() {
    this.loadVeiculos();
  }

  ngOnChanges(changes: SimpleChanges) {
    // Reload if defaultStatuses change dynamically, though they usually won't
    if (changes['defaultStatuses'] && !changes['defaultStatuses'].firstChange) {
      this.currentPage = 0;
      this.loadVeiculos();
    }
  }

  loadVeiculos() {
    this.loading = true;
    this.error = false;
    this.empty = false;

    let statusesToSearch = [...this.defaultStatuses];

    // Se o filtro estiver ativo, sobrepõe a busca por status
    if (this.showStatusFilter) {
      if (this.selectedStatusFilter) {
        statusesToSearch = [this.selectedStatusFilter];
      } else {
        statusesToSearch = []; // Todos
      }
    }

    this.veiculoService.findAllVeiculos(this.currentPage, this.searchQuery, statusesToSearch)
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

  onStatusChange(event: any) {
    this.selectedStatusFilter = event.target.value;
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
    if (this.enableRowClick) {
      this.router.navigate([this.baseRoute, placa]);
    }
  }
}
