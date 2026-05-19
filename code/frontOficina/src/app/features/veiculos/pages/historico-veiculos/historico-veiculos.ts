import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VehicleListView } from '../../components/vehicle-list-view/vehicle-list-view';

@Component({
  selector: 'app-historico-veiculos',
  standalone: true,
  imports: [
    CommonModule, 
    VehicleListView
  ],
  templateUrl: './historico-veiculos.html'
})
export class HistoricoVeiculos {
}
