import { Component, EventEmitter, Input, Output, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StatusBadge } from '../../../../shared/components/status-badge/status-badge';
import { ServicoService } from '../../services/servico.service';
import { EntradaService } from '../../services/entrada.service';
import { Servico } from '../../models/servico.model';

@Component({
  selector: 'app-entry-details-modal',
  standalone: true,
  imports: [CommonModule, StatusBadge],
  templateUrl: './entry-details-modal.html'
})
export class EntryDetailsModal implements OnInit {
  @Input() entrada: any;
  @Input() placa: string = '';
  @Output() close = new EventEmitter<void>();

  private servicoService = inject(ServicoService);
  private entradaService = inject(EntradaService);
  private cdr = inject(ChangeDetectorRef);

  servicos: Servico[] = [];
  loading = true;
  error = false;
  isGeneratingPdf = false;
  valorTotalEntrada = 0;

  ngOnInit() {
    if (this.entrada && this.entrada.id) {
      this.loadServicos();
    } else {
      this.error = true;
      this.loading = false;
    }
  }

  loadServicos() {
    this.servicoService.getServicosPorEntrada(this.entrada.id).subscribe({
      next: (data) => {
        this.servicos = data || [];
        this.calcularTotal();
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erro ao buscar serviços da entrada:', err);
        this.error = true;
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  calcularTotal() {
    this.valorTotalEntrada = this.servicos.reduce((total, servico) => total + (servico.valorTotal || 0), 0);
  }

  generatePdf() {
    if (this.isGeneratingPdf) return;
    
    this.isGeneratingPdf = true;
    this.entradaService.gerarPdfEntrada(this.entrada.id).subscribe({
      next: (blob) => {
        this.isGeneratingPdf = false;
        this.cdr.markForCheck();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        const sufixo = this.placa ? `-${this.placa}` : `-entrada-${this.entrada.id}`;
        a.download = `orcamento${sufixo}.pdf`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Erro ao gerar PDF', err);
        this.isGeneratingPdf = false;
        this.cdr.markForCheck();
      }
    });
  }
}
