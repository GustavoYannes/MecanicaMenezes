import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Cliente } from '../../models/cliente.model';

@Component({
  selector: 'app-cliente-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cliente-card.html',
  styles: ``
})
export class ClienteCard {
  @Input() cliente!: Cliente;

  formatCPF(cpf: string): string {
    if (!cpf) return '';
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }
}
