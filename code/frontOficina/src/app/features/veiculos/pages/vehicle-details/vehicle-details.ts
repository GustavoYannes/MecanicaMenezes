import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, switchMap, tap } from 'rxjs/operators';
import { of } from 'rxjs';

import { VeiculoService } from '../../services/veiculo.service';
import { EntradaService } from '../../services/entrada.service';
import { ServicoService } from '../../services/servico.service';

import { VeiculoDetail } from '../../models/veiculo-detail.model';
import { EntradaAberta } from '../../models/entrada-aberta.model';
import { Servico } from '../../models/servico.model';
import { ServiceFormData } from '../../components/service-form/service-form';

import { VehicleInfoCard } from '../../components/vehicle-info-card/vehicle-info-card';
import { ClientInfoCard } from '../../components/client-info-card/client-info-card';
import { ServicesTable } from '../../components/services-table/services-table';
import { TotalSummary } from '../../components/total-summary/total-summary';
import { ServiceForm } from '../../components/service-form/service-form';
import { ReleaseVehicleModal } from '../../components/release-vehicle-modal/release-vehicle-modal';

@Component({
  selector: 'app-vehicle-details',
  standalone: true,
  imports: [
    CommonModule,
    VehicleInfoCard,
    ClientInfoCard,
    ServicesTable,
    TotalSummary,
    ServiceForm,
    ReleaseVehicleModal
  ],
  templateUrl: './vehicle-details.html'
})
export class VehicleDetails implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private veiculoService = inject(VeiculoService);
  private entradaService = inject(EntradaService);
  private servicoService = inject(ServicoService);

  placa = '';
  
  // State
  loading = signal(true);
  error = signal<string | null>(null);
  
  // Data
  veiculo = signal<VeiculoDetail | null>(null);
  entrada = signal<EntradaAberta | null>(null);
  servicos = signal<Servico[]>([]);

  showServiceForm = signal(false);
  isSubmittingService = signal(false);

  // Release and PDF States
  showReleaseModal = signal(false);
  isReleasing = signal(false);
  isGeneratingPdf = signal(false);

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.placa = params.get('placa') || '';
      if (this.placa) {
        this.loadData();
      } else {
        this.error.set('Placa não informada');
        this.loading.set(false);
      }
    });
  }

  loadData() {
    this.loading.set(true);
    this.error.set(null);

    // Fluxo com SwitchMap mantendo 1 único loading para tudo:
    this.veiculoService.getVeiculoByPlaca(this.placa).pipe(
      tap(v => this.veiculo.set(v)),
      switchMap(() => this.entradaService.getEntradaAberta(this.placa).pipe(
        catchError(err => {
          console.warn('Nenhuma entrada aberta encontrada para o veículo', err);
          return of(null);
        })
      )),
      tap(e => this.entrada.set(e)),
      switchMap(entrada => {
        if (entrada && entrada.id) {
          return this.servicoService.getServicosPorEntrada(entrada.id).pipe(
            catchError(err => {
              console.error('Erro ao buscar serviços', err);
              return of([]);
            })
          );
        }
        return of([]);
      }),
      catchError(err => {
        console.error('Erro ao buscar detalhes do veículo', err);
        this.error.set('Não foi possível carregar os dados completos do veículo.');
        return of([]);
      })
    ).subscribe({
      next: (servicos) => {
        this.servicos.set(servicos);
        this.loading.set(false);
      }
    });
  }

  goBack() {
    this.router.navigate(['/veiculos']);
  }

  onAddService(formData: ServiceFormData) {
    const entradaId = this.entrada()?.id;
    if (!entradaId) return;

    const payload = {
      ...formData,
      idEntrada: entradaId
    };

    this.isSubmittingService.set(true);
    this.servicoService.createServico(payload).subscribe({
      next: () => {
        this.isSubmittingService.set(false);
        this.showServiceForm.set(false);
        this.loadData(); // Recarrega os serviços após adicionar
      },
      error: (err) => {
        console.error('Erro ao adicionar serviço', err);
        this.isSubmittingService.set(false);
        alert('Erro ao adicionar o serviço. Verifique os dados.');
      }
    });
  }

  onEditService(servico: Servico) {
    console.log('Editar serviço', servico);
    alert('Funcionalidade de edição em breve.');
  }

  onDeleteService(servico: any) {
    // Implement delete confirmation logic here
  }

  // --- Release Vehicle Flow ---
  
  openReleaseModal() {
    this.showReleaseModal.set(true);
  }

  closeReleaseModal() {
    this.showReleaseModal.set(false);
  }

  confirmRelease() {
    const currentEntrada = this.entrada();
    if (!currentEntrada) return;

    this.isReleasing.set(true);
    this.entradaService.liberarVeiculo(currentEntrada.id).subscribe({
      next: () => {
        this.isReleasing.set(false);
        this.showReleaseModal.set(false);
        // Retornar para a página anterior após liberar
        this.goBack();
      },
      error: (err) => {
        console.error('Erro ao liberar veículo', err);
        this.isReleasing.set(false);
      }
    });
  }

  // --- PDF Generation Flow ---

  generatePdf() {
    const currentEntrada = this.entrada();
    if (!currentEntrada || this.isGeneratingPdf()) return;

    this.isGeneratingPdf.set(true);
    this.entradaService.gerarPdfEntrada(currentEntrada.id).subscribe({
      next: (blob) => {
        this.isGeneratingPdf.set(false);
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        const placa = this.veiculo()?.placa || 'desconhecida';
        a.download = `orcamento-${placa}.pdf`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: (err) => {
        console.error('Erro ao gerar PDF', err);
        this.isGeneratingPdf.set(false);
      }
    });
  }
}
