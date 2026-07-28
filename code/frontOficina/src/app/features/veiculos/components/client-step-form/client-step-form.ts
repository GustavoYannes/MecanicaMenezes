import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';
import { EMPTY } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, filter, finalize, map, switchMap, tap } from 'rxjs/operators';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ClientCpfSearch } from '../client-cpf-search/client-cpf-search';
import { ClienteLookupService } from '../../services/cliente-lookup.service';
import { ClienteDetail } from '../../models/cliente-detail.model';
import { CepService } from '../../services/cep.service';

@Component({
  selector: 'app-client-step-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective, ClientCpfSearch],
  templateUrl: './client-step-form.html'
})
export class ClientStepForm implements OnInit {
  private fb = inject(FormBuilder);
  private lookupService = inject(ClienteLookupService);
  private cepService = inject(CepService);
  private destroyRef = inject(DestroyRef);

  clientForm!: FormGroup;
  loadingDetails = signal(false);
  errorDetails = signal('');
  loadingCep = signal(false);
  cepError = signal('');

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

    this.watchCepChanges();
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

  private watchCepChanges() {
    const cepControl = this.clientForm.get('endereco.cep');

    cepControl?.valueChanges.pipe(
      debounceTime(300),
      map(value => String(value || '').replace(/\D/g, '')),
      distinctUntilChanged(),
      tap(cep => {
        if (cep.length < 8) {
          this.cepError.set('');
          this.loadingCep.set(false);
        }
      }),
      filter(cep => cep.length === 8),
      tap(() => {
        this.loadingCep.set(true);
        this.cepError.set('');
      }),
      switchMap(cep => this.cepService.buscar(cep).pipe(
        catchError(() => {
          this.cepError.set('Não foi possível buscar o CEP. Preencha o endereço manualmente.');
          return EMPTY;
        }),
        finalize(() => this.loadingCep.set(false))
      )),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(address => {
      if (address.erro) {
        this.cepError.set('CEP não encontrado. Preencha o endereço manualmente.');
        return;
      }

      this.clientForm.get('endereco')?.patchValue({
        rua: address.logradouro || '',
        bairro: address.bairro || '',
        cidade: address.localidade || '',
        estado: address.uf || ''
      });
    });
  }
}
