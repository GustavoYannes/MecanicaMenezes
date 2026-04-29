import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Cliente } from '../../models/cliente.model';

@Component({
  selector: 'app-cliente-table',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cliente-table.html',
  styles: ``
})
export class ClienteTable {
  @Input() clientes: Cliente[] = [];

  formatCPF(cpf: string): string {
    if (!cpf) return '';
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }
}
