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
import { EditServiceModal } from '../../components/edit-service-modal/edit-service-modal';
import { DeleteServiceModal } from '../../components/delete-service-modal/delete-service-modal';
import { EditarServicoRequest } from '../../models/editar-servico-request.model';

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
    ReleaseVehicleModal,
    EditServiceModal,
    DeleteServiceModal
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

  // Edit Service State
  showEditModal = signal(false);
  isEditingService = signal(false);
  editSuccess = signal(false);
  selectedServico = signal<Servico | null>(null);

  // Delete Service State
  showDeleteModal = signal(false);
  isDeletingService = signal(false);
  deleteSuccess = signal(false);
  serviceToDelete = signal<Servico | null>(null);

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
    if (!servico.id) {
      alert('Não é possível editar este serviço pois ele ainda não possui um identificador (ID).');
      return;
    }
    this.selectedServico.set(servico);
    this.showEditModal.set(true);
  }

  closeEditModal() {
    this.showEditModal.set(false);
    this.selectedServico.set(null);
    this.editSuccess.set(false);
  }

  onSaveEdit(data: EditarServicoRequest) {
    const servicoId = this.selectedServico()?.id;
    if (!servicoId) return;

    this.isEditingService.set(true);
    this.servicoService.editarServico(servicoId, data).subscribe({
      next: () => {
        this.isEditingService.set(false);
        this.editSuccess.set(true);
        this.loadData();
        
        setTimeout(() => {
          this.closeEditModal();
        }, 1500);
      },
      error: (err) => {
        console.error('Erro ao editar serviço', err);
        this.isEditingService.set(false);
        alert('Erro ao atualizar o serviço. Verifique os dados.');
      }
    });
  }

  onDeleteService(servico: Servico) {
    if (!servico.id) {
      alert('Não é possível excluir este serviço pois ele ainda não possui um identificador (ID).');
      return;
    }
    this.serviceToDelete.set(servico);
    this.showDeleteModal.set(true);
  }

  closeDeleteModal() {
    this.showDeleteModal.set(false);
    this.serviceToDelete.set(null);
    this.deleteSuccess.set(false);
  }

  confirmDeleteService() {
    const servicoId = this.serviceToDelete()?.id;
    if (!servicoId) return;

    this.isDeletingService.set(true);
    this.servicoService.deletarServico(servicoId).subscribe({
      next: () => {
        this.isDeletingService.set(false);
        this.deleteSuccess.set(true);
        this.loadData();
        
        setTimeout(() => {
          this.closeDeleteModal();
        }, 1500);
      },
      error: (err) => {
        console.error('Erro ao excluir serviço', err);
        this.isDeletingService.set(false);
        alert('Erro ao excluir o serviço. Tente novamente.');
      }
    });
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
