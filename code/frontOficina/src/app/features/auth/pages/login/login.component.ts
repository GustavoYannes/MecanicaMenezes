import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NgxMaskDirective } from 'ngx-mask';
import { AuthService } from '../../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, NgxMaskDirective],
  templateUrl: './login.component.html',
  styles: [] // We use 100% Tailwind
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm: FormGroup = this.fb.group({
    cpf: ['', [Validators.required, Validators.minLength(11)]],
    senha: ['', [Validators.required, Validators.minLength(4)]]
  });

  loading = false;
  errorMessage = '';
  showPassword = false;

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    // The mask applied on input usually keeps raw value or masked value based on config
    // Assuming ngx-mask is configured to drop special characters, we can remove formatting if needed.
    // '000.000.000-00'
    let { cpf, senha } = this.loginForm.value;
    
    // Safety generic stripping (digits only)
    cpf = cpf.replace(/\D/g, '');

    this.authService.login({ cpf, senha }).subscribe({
      next: () => {
        this.loading = false;
        // Navigate somewhere after success
        this.router.navigate(['/dashboard']); 
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || 'Falha ao autenticar. Verifique suas credenciais.';
      }
    });
  }
}
