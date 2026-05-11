import { Component, inject, signal, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormStepper } from '../form-stepper/form-stepper';
import { ClientStepForm } from '../client-step-form/client-step-form';
import { VehicleStepForm } from '../vehicle-step-form/vehicle-step-form';
import { VehicleFormStep } from '../../models/vehicle-form-step.model';
import { VehicleModalService } from '../../services/vehicle-modal.service';
import { EntradaService } from '../../services/entrada.service';

@Component({
  selector: 'app-vehicle-form-modal',
  standalone: true,
  imports: [CommonModule, FormStepper, ClientStepForm, VehicleStepForm],
  templateUrl: './vehicle-form-modal.html'
})
export class VehicleFormModal {
  private modalService = inject(VehicleModalService);
  private entradaService = inject(EntradaService);
  
  isOpen = this.modalService.isOpen;
  currentStep = signal<VehicleFormStep>(1);

  isSubmitting = signal(false);
  submitError = signal<string[]>([]);
  submitSuccess = signal(false);

  @ViewChild(ClientStepForm) clientStepComponent!: ClientStepForm;
  @ViewChild(VehicleStepForm) vehicleStepComponent!: VehicleStepForm;

  closeModal() {
    this.modalService.close();
    if (this.submitSuccess()) {
      setTimeout(() => this.resetModal(), 500);
    }
  }

  resetModal() {
    this.currentStep.set(1);
    this.submitSuccess.set(false);
    this.submitError.set([]);
    this.isSubmitting.set(false);
    if (this.clientStepComponent) this.clientStepComponent.clientForm.reset();
    if (this.vehicleStepComponent) this.vehicleStepComponent.vehicleForm.reset();
  }

  nextStep() {
    if (this.clientStepComponent && this.clientStepComponent.clientForm.invalid) {
      this.clientStepComponent.clientForm.markAllAsTouched();
      return;
    }
    this.currentStep.set(2);
  }

  prevStep() {
    this.currentStep.set(1);
  }

  submitForm() {
    if (this.vehicleStepComponent && this.vehicleStepComponent.vehicleForm.invalid) {
      this.vehicleStepComponent.vehicleForm.markAllAsTouched();
      return;
    }

    const clientData = this.clientStepComponent.clientForm.getRawValue();
    const vehicleData = this.vehicleStepComponent.vehicleForm.getRawValue();

    const payload = {
      placa: vehicleData.placa.trim().toUpperCase(),
      marca: vehicleData.marca,
      modelo: vehicleData.modelo,
      ano: Number(vehicleData.ano),
      km: Number(vehicleData.km),
      cor: vehicleData.cor,
      cpf: String(clientData.cpf || '').replace(/\D/g, ''),
      telefone: String(clientData.telefone || '').replace(/\D/g, ''),
      email: clientData.email,
      nomeCliente: clientData.nomeCompleto,
      cidade: clientData.endereco.cidade,
      estado: clientData.endereco.estado,
      cep: Number(String(clientData.endereco.cep || '').replace(/\D/g, '')),
      bairro: clientData.endereco.bairro,
      rua: clientData.endereco.rua,
      numero: clientData.endereco.numero,
      endereco: `${clientData.endereco.rua}, ${clientData.endereco.numero}`
    };

    this.isSubmitting.set(true);
    this.submitError.set([]);

    this.entradaService.createEntrada(payload).subscribe({
      next: () => {
        this.isSubmitting.set(false);
        this.submitSuccess.set(true);
      },
      error: (err) => {
        this.isSubmitting.set(false);
        if (err.error && Array.isArray(err.error.errors)) {
          this.submitError.set(err.error.errors);
        } else {
          this.submitError.set(['Ocorreu um erro ao registrar a entrada. Verifique os dados e tente novamente.']);
        }
        console.error(err);
      }
    });
  }
}
