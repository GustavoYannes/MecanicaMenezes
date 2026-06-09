import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FuncionarioService } from '../../services/funcionario.service';
import { ServicoService } from '../../../veiculos/services/servico.service';
import { TokenService } from '../../../../core/services/token.service';
import { FuncionarioPerfilResponse, ServicoResponse, PageResponse, RelatorioMensalResponse } from '../../models/funcionario.model';
import { DataTable } from '../../../../shared/components/data-table/data-table';
import { Pagination } from '../../../../shared/components/pagination/pagination';

@Component({
  selector: 'app-meu-perfil',
  standalone: true,
  imports: [CommonModule, DataTable, Pagination],
  templateUrl: './meu-perfil.html',
  styles: ``
})
export class MeuPerfil implements OnInit {
  private funcionarioService = inject(FuncionarioService);
  private servicoService = inject(ServicoService);
  private tokenService = inject(TokenService);

  funcionario = signal<FuncionarioPerfilResponse | null>(null);
  servicosPage = signal<PageResponse<ServicoResponse> | null>(null);
  relatorio = signal<RelatorioMensalResponse | null>(null);

  loadingPerfil = signal(true);
  loadingServicos = signal(true);
  loadingRelatorio = signal(true);

  errorPerfil = signal(false);
  errorServicos = signal(false);
  errorRelatorio = signal(false);

  currentPage = signal(0);
  inicioDate = '';
  fimDate = '';
  uuidFuncionario = '';

  ngOnInit() {
    const today = new Date();
    this.inicioDate = this.getStartOfMonth(today);
    this.fimDate = this.getEndOfMonth(today);
    this.uuidFuncionario = this.tokenService.getUuid() || '';

    this.loadPerfil();
    this.loadRelatorio();
    this.loadServicos(this.currentPage());
  }

  getStartOfMonth(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    return `${year}-${month}-01`;
  }

  getEndOfMonth(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const lastDay = new Date(year, date.getMonth() + 1, 0).getDate();
    return `${year}-${month}-${String(lastDay).padStart(2, '0')}`;
  }

  loadPerfil() {
    this.loadingPerfil.set(true);
    this.errorPerfil.set(false);

    this.funcionarioService.getFuncionarioLogado().subscribe({
      next: (data) => {
        this.funcionario.set(data);
        this.loadingPerfil.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar perfil do funcionário:', err);
        this.errorPerfil.set(true);
        this.loadingPerfil.set(false);
      }
    });
  }

  loadRelatorio() {
    if (!this.uuidFuncionario) {
      this.errorRelatorio.set(true);
      this.loadingRelatorio.set(false);
      return;
    }
    this.loadingRelatorio.set(true);
    this.errorRelatorio.set(false);

    this.servicoService.getRelatorioMensal(this.inicioDate, this.fimDate, this.uuidFuncionario).subscribe({
      next: (data) => {
        this.relatorio.set(data);
        this.loadingRelatorio.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar relatório mensal:', err);
        this.errorRelatorio.set(true);
        this.loadingRelatorio.set(false);
      }
    });
  }

  loadServicos(page: number) {
    if (!this.uuidFuncionario) {
      this.errorServicos.set(true);
      this.loadingServicos.set(false);
      return;
    }
    this.loadingServicos.set(true);
    this.errorServicos.set(false);
    this.currentPage.set(page);

    this.servicoService.getServicosPaginados(this.inicioDate, this.fimDate, this.uuidFuncionario, page).subscribe({
      next: (data) => {
        this.servicosPage.set(data);
        this.loadingServicos.set(false);
      },
      error: (err) => {
        console.error('Erro ao carregar serviços paginados:', err);
        this.errorServicos.set(true);
        this.loadingServicos.set(false);
      }
    });
  }

  onPageChange(page: number) {
    this.loadServicos(page);
  }

  formatCPF(cpf: string | undefined): string {
    if (!cpf) return '';
    const clean = cpf.replace(/\D/g, '');
    if (clean.length !== 11) return cpf;
    return `${clean.slice(0, 3)}.${clean.slice(3, 6)}.${clean.slice(6, 9)}-${clean.slice(9)}`;
  }

  formatTelefone(telefone: string | undefined): string {
    if (!telefone) return '';
    const clean = telefone.replace(/\D/g, '');
    if (clean.length === 11) {
      return `(${clean.slice(0, 2)}) ${clean.slice(2, 7)}-${clean.slice(7)}`;
    } else if (clean.length === 10) {
      return `(${clean.slice(0, 2)}) ${clean.slice(2, 6)}-${clean.slice(6)}`;
    }
    return telefone;
  }
}
