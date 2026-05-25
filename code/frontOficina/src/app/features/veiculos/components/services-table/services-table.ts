import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Servico } from '../../models/servico.model';
import { DataTable } from '../../../../shared/components/data-table/data-table';

@Component({
  selector: 'app-services-table',
  standalone: true,
  imports: [CommonModule, DataTable],
  templateUrl: './services-table.html'
})
export class ServicesTable {
  @Input({ required: true }) servicos: Servico[] = [];
  @Output() edit = new EventEmitter<Servico>();
  @Output() delete = new EventEmitter<Servico>();
}
