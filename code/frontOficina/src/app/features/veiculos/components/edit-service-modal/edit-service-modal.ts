import { Component, EventEmitter, Input, OnInit, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Servico } from '../../models/servico.model';
import { EditarServicoRequest } from '../../models/editar-servico-request.model';

@Component({
  selector: 'app-edit-service-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './edit-service-modal.html'
})
export class EditServiceModal implements OnInit {
  @Input() servico!: Servico;
  @Input() isSubmitting = false;
  @Input() isSuccess = false;
  
  @Output() save = new EventEmitter<EditarServicoRequest>();
  @Output() cancel = new EventEmitter<void>();

  private fb = inject(FormBuilder);
  editForm!: FormGroup;

  ngOnInit() {
    this.editForm = this.fb.group({
      nome: [this.servico.nome, [Validators.required, Validators.minLength(3)]],
      quantidade: [this.servico.quantidade, [Validators.required, Validators.min(1)]],
      valor: [this.servico.valor, [Validators.required, Validators.min(0)]]
    });
  }

  onSubmit() {
    if (this.editForm.valid && !this.isSubmitting) {
      this.save.emit(this.editForm.value as EditarServicoRequest);
    } else {
      this.editForm.markAllAsTouched();
    }
  }
}
