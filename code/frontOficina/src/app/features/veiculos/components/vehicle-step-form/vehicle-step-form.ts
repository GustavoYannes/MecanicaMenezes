import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { VehiclePlateSearch } from '../vehicle-plate-search/vehicle-plate-search';
import { VeiculoLookupService } from '../../services/veiculo-lookup.service';
import { VeiculoDetail } from '../../models/veiculo-detail.model';

@Component({
  selector: 'app-vehicle-step-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, VehiclePlateSearch],
  templateUrl: './vehicle-step-form.html'
})
export class VehicleStepForm implements OnInit {
  private fb = inject(FormBuilder);
  private lookupService = inject(VeiculoLookupService);

  vehicleForm!: FormGroup;
  loadingDetails = signal(false);
  errorDetails = signal('');

  ngOnInit() {
    this.vehicleForm = this.fb.group({
      placa: ['', [Validators.required]],
      marca: ['', Validators.required],
      modelo: ['', Validators.required],
      ano: [null, [Validators.required, Validators.min(1900)]],
      cor: ['', Validators.required],
      km: [null, [Validators.required, Validators.min(0)]]
    });
  }

  onVehicleSelected(placa: string) {
    this.loadingDetails.set(true);
    this.errorDetails.set('');
    
    this.lookupService.getByPlaca(placa).subscribe({
      next: (detail: VeiculoDetail) => {
        this.loadingDetails.set(false);
        this.vehicleForm.patchValue({
          placa: detail.placa,
          marca: detail.marca,
          modelo: detail.modelo,
          ano: detail.ano,
          cor: detail.cor,
          km: detail.km
        });
      },
      error: () => {
        this.loadingDetails.set(false);
        this.errorDetails.set('Erro ao carregar os detalhes do veículo.');
      }
    });
  }
}
