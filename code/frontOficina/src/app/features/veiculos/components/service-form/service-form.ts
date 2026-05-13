import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

export interface ServiceFormData {
  nome: string;
  quantidade: number;
  valor: number;
}

@Component({
  selector: 'app-service-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './service-form.html'
})
export class ServiceForm {
  @Output() addService = new EventEmitter<ServiceFormData>();
  @Output() cancel = new EventEmitter<void>();
  
  serviceForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.serviceForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      quantidade: [1, [Validators.required, Validators.min(1)]],
      valor: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  onSubmit() {
    if (this.serviceForm.valid) {
      this.addService.emit(this.serviceForm.getRawValue());
    } else {
      this.serviceForm.markAllAsTouched();
    }
  }

  resetForm() {
    this.serviceForm.reset({ quantidade: 1 });
  }
}
