import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VeiculoDetail } from '../../models/veiculo-detail.model';

@Component({
  selector: 'app-vehicle-info-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './vehicle-info-card.html'
})
export class VehicleInfoCard {
  @Input({ required: true }) veiculo!: VeiculoDetail;
}
