import { Component, OnInit, Input, inject, signal, SimpleChanges, OnChanges } from '@angular/core';
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
  private router = inject(Router);

  // State signals — required for zoneless change detection
  veiculos = signal<VeiculoListItem[]>([]);
  loading = signal(false);
  error = signal(false);
  empty = signal(false);

  currentPage = signal(0);
  totalPages = signal(0);
  totalElements = signal(0);
  first = signal(true);
  last = signal(true);

  searchQuery = '';
  selectedStatusFilter = ''; // '' means 'Todos'

  ngOnInit() {
    this.loadVeiculos();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['defaultStatuses'] && !changes['defaultStatuses'].firstChange) {
      this.currentPage.set(0);
      this.loadVeiculos();
    }
  }

  loadVeiculos() {
    this.loading.set(true);
    this.error.set(false);
    this.empty.set(false);

    let statusesToSearch = [...this.defaultStatuses];

    if (this.showStatusFilter) {
      if (this.selectedStatusFilter) {
        statusesToSearch = [this.selectedStatusFilter];
      } else {
        statusesToSearch = [];
      }
    }

    this.veiculoService.findAllVeiculos(this.currentPage(), this.searchQuery, statusesToSearch)
      .subscribe({
        next: (response: any) => {
          const items = response?.content || (Array.isArray(response) ? response : []);
          this.veiculos.set(items);
          this.totalPages.set(response?.totalPages || 1);
          this.totalElements.set(response?.totalElements || items.length);
          this.first.set(response?.first ?? true);
          this.last.set(response?.last ?? true);
          this.loading.set(false);
          this.empty.set(!items || items.length === 0);
        },
        error: (err) => {
          console.error('Erro ao buscar veículos:', err);
          this.error.set(true);
          this.loading.set(false);
        }
      });
  }

  onSearch(query: string) {
    this.searchQuery = query;
    this.currentPage.set(0);
    this.loadVeiculos();
  }

  onStatusChange(event: any) {
    this.selectedStatusFilter = event.target.value;
    this.currentPage.set(0);
    this.loadVeiculos();
  }

  onPageChange(page: number) {
    this.currentPage.set(page);
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
