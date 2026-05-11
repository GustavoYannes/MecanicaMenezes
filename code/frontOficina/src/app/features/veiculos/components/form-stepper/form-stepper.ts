import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VehicleFormStep } from '../../models/vehicle-form-step.model';

@Component({
  selector: 'app-form-stepper',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './form-stepper.html'
})
export class FormStepper {
  @Input() currentStep: VehicleFormStep = 1;
}
