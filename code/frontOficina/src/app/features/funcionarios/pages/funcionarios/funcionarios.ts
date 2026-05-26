import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FuncionarioService } from '../../services/funcionario.service';
import { FuncionarioListItem, RegistrarFuncionarioRequest } from '../../models/funcionario.model';
import { SearchInput } from '../../../../shared/components/search-input/search-input';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { FuncionarioRegisterModal } from '../../components/funcionario-register-modal/funcionario-register-modal';

@Component({
  selector: 'app-funcionarios',
  standalone: true,
  imports: [
    CommonModule,
    SearchInput,
    DataTable,
    Pagination,
    FuncionarioRegisterModal
  ],
  templateUrl: './funcionarios.html',
  styles: ``,
})
export class Funcionarios implements OnInit {
  private funcionarioService = inject(FuncionarioService);
  private cdr = inject(ChangeDetectorRef);

  funcionarios: FuncionarioListItem[] = [];
  loading = false;
  error = false;
  empty = false;

  currentPage = 0;
  totalPages = 0;
  totalElements = 0;
  first = true;
  last = true;

  searchQuery = '';

  // Modal State
  showRegisterModal = false;
  isSubmitting = false;
  isSuccess = false;
  modalErrorMessage: string | null = null;

  ngOnInit() {
    this.loadFuncionarios();
  }

  loadFuncionarios() {
    this.loading = true;
    this.error = false;
    this.empty = false;

    this.funcionarioService.getFuncionarios(this.searchQuery, this.currentPage)
      .subscribe({
        next: (response) => {
          this.funcionarios = response?.content || [];
          this.totalPages = response?.totalPages || 0;
          this.totalElements = response?.totalElements || 0;
          this.first = response?.first ?? true;
          this.last = response?.last ?? true;
          this.loading = false;
          
          if (this.funcionarios.length === 0) {
            this.empty = true;
          }
          this.cdr.markForCheck();
        },
        error: (err) => {
          console.error('Erro ao buscar funcionários:', err);
          this.error = true;
          this.loading = false;
          this.cdr.markForCheck();
        }
      });
  }

  onSearch(query: string) {
    this.searchQuery = query;
    this.currentPage = 0;
    this.loadFuncionarios();
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.loadFuncionarios();
  }

  openRegisterModal() {
    this.showRegisterModal = true;
    this.isSubmitting = false;
    this.isSuccess = false;
    this.modalErrorMessage = null;
  }

  closeRegisterModal() {
    if (!this.isSubmitting) {
      this.showRegisterModal = false;
    }
  }

  onRegisterSave(data: RegistrarFuncionarioRequest) {
    this.isSubmitting = true;
    this.modalErrorMessage = null;

    this.funcionarioService.registrarFuncionario(data)
      .subscribe({
        next: () => {
          this.isSubmitting = false;
          this.isSuccess = true;
          this.cdr.markForCheck();

          setTimeout(() => {
            this.showRegisterModal = false;
            this.isSuccess = false;
            this.loadFuncionarios();
            this.cdr.markForCheck();
          }, 1500);
        },
        error: (err) => {
          this.isSubmitting = false;
          console.error('Erro ao cadastrar funcionário:', err);
          this.modalErrorMessage = err.error?.message || 'Erro ao cadastrar funcionário. Verifique os dados e tente novamente.';
          this.cdr.markForCheck();
        }
      });
  }
}
