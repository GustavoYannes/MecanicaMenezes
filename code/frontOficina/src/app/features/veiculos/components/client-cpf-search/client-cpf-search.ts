import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { ClienteLookupService } from '../../services/cliente-lookup.service';
import { ClienteSearchResult } from '../../models/cliente-search-result.model';
import { debounceTime, distinctUntilChanged, filter, switchMap, tap, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { NgxMaskDirective } from 'ngx-mask';

@Component({
  selector: 'app-client-cpf-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './client-cpf-search.html'
})
export class ClientCpfSearch implements OnInit {
  private lookupService = inject(ClienteLookupService);
  
  @Output() clienteSelected = new EventEmitter<string>();

  searchControl = new FormControl('');
  
  results = signal<ClienteSearchResult[]>([]);
  loading = signal(false);
  showDropdown = signal(false);
  hasError = signal(false);

  ngOnInit() {
    this.searchControl.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      tap(val => {
        if (!val || val.length < 3) {
          this.showDropdown.set(false);
          this.results.set([]);
          this.loading.set(false);
        } else {
          this.loading.set(true);
          this.showDropdown.set(true);
          this.hasError.set(false);
        }
      }),
      filter(val => !!val && val.length >= 3),
      switchMap(val => {
        const cleanCpf = val!.replace(/\D/g, '');
        return this.lookupService.searchByCpf(cleanCpf).pipe(
          catchError(() => {
            this.hasError.set(true);
            return of({ content: [] });
          })
        );
      })
    ).subscribe((response: any) => {
      this.results.set(response?.content || []);
      this.loading.set(false);
    });
  }

  selectCliente(cpf: string) {
    this.showDropdown.set(false);
    this.searchControl.setValue('', { emitEvent: false });
    this.clienteSelected.emit(cpf);
  }

  formatCPF(cpf: string): string {
    if (!cpf) return '';
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }
}
