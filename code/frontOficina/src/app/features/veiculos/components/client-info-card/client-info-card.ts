import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClienteDetail } from '../../models/cliente-detail.model';

@Component({
  selector: 'app-client-info-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './client-info-card.html'
})
export class ClientInfoCard {
  @Input({ required: true }) cliente!: ClienteDetail;

  formatCPF(cpf: string | undefined): string {
    if (!cpf) return '';
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  formatPhone(phone: string | undefined): string {
    if (!phone) return '';
    return phone.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
  }

  formatCep(cep: number | string | undefined): string {
    if (!cep) return '';
    const cepStr = cep.toString().padStart(8, '0');
    return cepStr.replace(/(\d{5})(\d{3})/, '$1-$2');
  }
}
