import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';
import { ClientCpfSearch } from '../client-cpf-search/client-cpf-search';
import { ClienteLookupService } from '../../services/cliente-lookup.service';
import { ClienteDetail } from '../../models/cliente-detail.model';

@Component({
  selector: 'app-client-step-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective, ClientCpfSearch],
  templateUrl: './client-step-form.html'
})
export class ClientStepForm implements OnInit {
  private fb = inject(FormBuilder);
  private lookupService = inject(ClienteLookupService);

  clientForm!: FormGroup;
  loadingDetails = signal(false);
  errorDetails = signal('');

  ngOnInit() {
    this.clientForm = this.fb.group({
      nomeCompleto: ['', Validators.required],
      cpf: ['', Validators.required],
      telefone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      endereco: this.fb.group({
        cep: ['', Validators.required],
        estado: ['', Validators.required],
        cidade: ['', Validators.required],
        bairro: ['', Validators.required],
        rua: ['', Validators.required],
        numero: ['', Validators.required],
      })
    });
  }

  onClienteSelected(cpf: string) {
    this.loadingDetails.set(true);
    this.errorDetails.set('');
    
    this.lookupService.getByCpf(cpf).subscribe({
      next: (detail: ClienteDetail) => {
        this.loadingDetails.set(false);
        this.clientForm.patchValue({
          nomeCompleto: detail.nomeCompleto,
          cpf: detail.cpf,
          telefone: detail.telefone,
          email: detail.email,
          endereco: {
            cep: detail.endereco.cep,
            estado: detail.endereco.estado,
            cidade: detail.endereco.cidade,
            bairro: detail.endereco.bairro,
            rua: detail.endereco.rua,
            numero: detail.endereco.numero
          }
        });
      },
      error: () => {
        this.loadingDetails.set(false);
        this.errorDetails.set('Erro ao carregar os detalhes do cliente.');
      }
    });
  }
}
