import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators, FormGroup } from '@angular/forms';
import { NgxMaskDirective } from 'ngx-mask';
import { RegistrarFuncionarioRequest } from '../../models/funcionario.model';

@Component({
  selector: 'app-funcionario-register-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './funcionario-register-modal.html'
})
export class FuncionarioRegisterModal {
  @Input() isSubmitting = false;
  @Input() isSuccess = false;
  @Input() errorMessage: string | null = null;
  
  @Output() save = new EventEmitter<RegistrarFuncionarioRequest>();
  @Output() cancel = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  
  showPassword = false;

  registerForm: FormGroup = this.fb.group({
    nome: ['', [Validators.required, Validators.minLength(3)]],
    cpf: ['', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]],
    email: ['', [Validators.required, Validators.email]],
    telefone: ['', [Validators.required, Validators.pattern(/^[0-9]{10,11}$/)]],
    senha: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(20)]]
  });

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  onSubmit() {
    if (this.registerForm.valid && !this.isSubmitting) {
      this.save.emit(this.registerForm.value);
    } else {
      this.registerForm.markAllAsTouched();
    }
  }
}
