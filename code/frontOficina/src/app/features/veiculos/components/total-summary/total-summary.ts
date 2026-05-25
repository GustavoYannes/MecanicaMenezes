import { Component, Input, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Servico } from '../../models/servico.model';

@Component({
  selector: 'app-total-summary',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './total-summary.html'
})
export class TotalSummary implements OnChanges {
  @Input({ required: true }) servicos: Servico[] = [];
  
  total = 0;

  ngOnChanges() {
    this.total = this.servicos.reduce((acc, curr) => acc + (curr.valorTotal || 0), 0);
  }
}
