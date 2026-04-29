import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteService } from '../../services/cliente';
import { Cliente } from '../../models/cliente.model';
import { ClienteSearch } from '../../components/cliente-search/cliente-search';
import { ClienteTable } from '../../components/cliente-table/cliente-table';
import { ClienteCard } from '../../components/cliente-card/cliente-card';
import { ClientePagination } from '../../components/cliente-pagination/cliente-pagination';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [
    CommonModule, 
    ClienteSearch, 
    ClienteTable, 
    ClienteCard, 
    ClientePagination
  ],
  templateUrl: './clientes.html',
  styles: ``
})
export class Clientes implements OnInit {
  private clienteService = inject(ClienteService);
  private cdr = inject(ChangeDetectorRef);

  clientes: Cliente[] = [];
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
    this.loadClientes();
  }

  loadClientes() {
    this.loading = true;
    this.error = false;
    this.empty = false;

    this.clienteService.getClientes(this.currentPage, this.searchQuery)
      .subscribe({
        next: (response: any) => {
          this.clientes = response?.content || (Array.isArray(response) ? response : []);
          this.totalPages = response?.totalPages || 1;
          this.totalElements = response?.totalElements || this.clientes.length;
          this.first = response?.first ?? true;
          this.last = response?.last ?? true;
          this.loading = false;
          
          if (!this.clientes || this.clientes.length === 0) {
            this.empty = true;
          }
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Erro ao buscar clientes:', err);
          this.error = true;
          this.loading = false;
          this.cdr.markForCheck();
        }
      });
  }

  onSearch(query: string) {
    this.searchQuery = query;
    this.currentPage = 0; // Volta para a página 0 ao pesquisar
    this.loadClientes();
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.loadClientes();
  }
}
