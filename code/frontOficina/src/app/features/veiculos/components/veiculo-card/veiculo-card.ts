import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VeiculoListItem } from '../../models/veiculo-list-item.model';
import { StatusBadge } from '../../../../shared/components/status-badge/status-badge';

@Component({
  selector: 'app-veiculo-card',
  standalone: true,
  imports: [CommonModule, StatusBadge],
  templateUrl: './veiculo-card.html'
})
export class VeiculoCard {
  @Input() veiculo!: VeiculoListItem;
}
