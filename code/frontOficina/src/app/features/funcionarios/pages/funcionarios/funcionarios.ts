import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FuncionarioService } from '../../services/funcionario.service';
import { ServicoService } from '../../../veiculos/services/servico.service';
import { FuncionarioListItem, RegistrarFuncionarioRequest } from '../../models/funcionario.model';
import { SearchInput } from '../../../../shared/components/search-input/search-input';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { Pagination } from '../../../../shared/components/pagination/pagination';
import { FuncionarioRegisterModal } from '../../components/funcionario-register-modal/funcionario-register-modal';
import { forkJoin, of } from 'rxjs';
import { switchMap, catchError, map } from 'rxjs/operators';

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
  private servicoService = inject(ServicoService);

  // State signals
  funcionarios = signal<FuncionarioListItem[]>([]);
  loading = signal(false);
  error = signal(false);
  empty = signal(false);

  currentPage = signal(0);
  totalPages = signal(0);
  totalElements = signal(0);
  first = signal(true);
  last = signal(true);

  searchQuery = '';

  // Modal State signals
  showRegisterModal = signal(false);
  isSubmitting = signal(false);
  isSuccess = signal(false);
  modalErrorMessage = signal<string | null>(null);

  ngOnInit() {
    this.loadFuncionarios();
  }

  private getPeriodoMesAtual(): { inicio: string; fim: string } {
    const agora = new Date();
    const ano = agora.getFullYear();
    const mes = agora.getMonth();

    const primeiroDia = new Date(ano, mes, 1);
    const ultimoDia = new Date(ano, mes + 1, 0);

    const formatarData = (d: Date): string => {
      const y = d.getFullYear();
      const m = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${y}-${m}-${day}`;
    };

    return {
      inicio: formatarData(primeiroDia),
      fim: formatarData(ultimoDia),
    };
  }

  loadFuncionarios() {
    this.loading.set(true);
    this.error.set(false);
    this.empty.set(false);

    const { inicio, fim } = this.getPeriodoMesAtual();

    this.funcionarioService.getFuncionarios(this.searchQuery, this.currentPage())
      .subscribe({
        next: (response) => {
          const content = response?.content || [];
          
          const items = content.map(mecanico => ({
            ...mecanico,
            totalServicoMensal: undefined,
            totalGeradoMensal: undefined
          }));

          this.funcionarios.set(items);
          this.totalPages.set(response?.totalPages || 0);
          this.totalElements.set(response?.totalElements || 0);
          this.first.set(response?.first ?? true);
          this.last.set(response?.last ?? true);
          this.loading.set(false);
          this.empty.set(items.length === 0);

          // Disparar buscas individuais em paralelo
          content.forEach((mecanico) => {
            this.servicoService.getRelatorioMensal(inicio, fim, mecanico.id)
              .subscribe({
                next: (report) => {
                  this.funcionarios.update(funcs => funcs.map(f => {
                    if (f.id === mecanico.id) {
                      return {
                        ...f,
                        totalServicoMensal: report?.qtdServico ?? 0,
                        totalGeradoMensal: report?.totalGerado ?? 0
                      };
                    }
                    return f;
                  }));
                },
                error: (err) => {
                  console.error(`Erro ao carregar relatório do mecânico ${mecanico.nome}:`, err);
                  this.funcionarios.update(funcs => funcs.map(f => {
                    if (f.id === mecanico.id) {
                      return {
                        ...f,
                        totalServicoMensal: -1,
                        totalGeradoMensal: -1
                      };
                    }
                    return f;
                  }));
                }
              });
          });
        },
        error: (err) => {
          console.error('Erro ao buscar funcionários:', err);
          this.error.set(true);
          this.loading.set(false);
        }
      });
  }

  onSearch(query: string) {
    this.searchQuery = query;
    this.currentPage.set(0);
    this.loadFuncionarios();
  }

  onPageChange(page: number) {
    this.currentPage.set(page);
    this.loadFuncionarios();
  }

  openRegisterModal() {
    this.showRegisterModal.set(true);
    this.isSubmitting.set(false);
    this.isSuccess.set(false);
    this.modalErrorMessage.set(null);
  }

  closeRegisterModal() {
    if (!this.isSubmitting()) {
      this.showRegisterModal.set(false);
    }
  }

  onRegisterSave(data: RegistrarFuncionarioRequest) {
    this.isSubmitting.set(true);
    this.modalErrorMessage.set(null);

    this.funcionarioService.registrarFuncionario(data)
      .subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.isSuccess.set(true);

          setTimeout(() => {
            this.showRegisterModal.set(false);
            this.isSuccess.set(false);
            this.loadFuncionarios();
          }, 1500);
        },
        error: (err) => {
          this.isSubmitting.set(false);
          console.error('Erro ao cadastrar funcionário:', err);
          this.modalErrorMessage.set(err.error?.message || 'Erro ao cadastrar funcionário. Verifique os dados e tente novamente.');
        }
      });
  }
}
