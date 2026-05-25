import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VehicleListView } from '../../components/vehicle-list-view/vehicle-list-view';

@Component({
  selector: 'app-veiculos-list',
  standalone: true,
  imports: [
    CommonModule, 
    VehicleListView
  ],
  templateUrl: './veiculos-list.html'
})
export class VeiculosList {
}
