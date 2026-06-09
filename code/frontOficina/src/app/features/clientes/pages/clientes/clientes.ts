import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../services/cliente';
import { Cliente } from '../../models/cliente.model';
import { SearchInput } from '../../../../shared/components/search-input/search-input';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { ClienteCard } from '../../components/cliente-card/cliente-card';
import { Pagination } from '../../../../shared/components/pagination/pagination';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [
    CommonModule, 
    SearchInput, 
    DataTable, 
    ClienteCard, 
    Pagination
  ],
  templateUrl: './clientes.html',
  styles: ``
})
export class Clientes implements OnInit {
  private clienteService = inject(ClienteService);

  // State signals — required for zoneless change detection
  clientes = signal<Cliente[]>([]);
  loading = signal(false);
  error = signal(false);
  empty = signal(false);

  currentPage = signal(0);
  totalPages = signal(0);
  totalElements = signal(0);
  first = signal(true);
  last = signal(true);
  
  searchQuery = '';

  ngOnInit() {
    this.loadClientes();
  }

  formatCPF(cpf: string): string {
    if (!cpf) return '';
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  loadClientes() {
    this.loading.set(true);
    this.error.set(false);
    this.empty.set(false);

    this.clienteService.getClientes(this.currentPage(), this.searchQuery)
      .subscribe({
        next: (response: any) => {
          const items = response?.content || (Array.isArray(response) ? response : []);
          this.clientes.set(items);
          this.totalPages.set(response?.totalPages || 1);
          this.totalElements.set(response?.totalElements || items.length);
          this.first.set(response?.first ?? true);
          this.last.set(response?.last ?? true);
          this.loading.set(false);
          this.empty.set(!items || items.length === 0);
        },
        error: (err) => {
          console.error('Erro ao buscar clientes:', err);
          this.error.set(true);
          this.loading.set(false);
        }
      });
  }

  onSearch(query: string) {
    this.searchQuery = query;
    this.currentPage.set(0);
    this.loadClientes();
  }

  onPageChange(page: number) {
    this.currentPage.set(page);
    this.loadClientes();
  }
}
