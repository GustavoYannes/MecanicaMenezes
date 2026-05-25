import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Servico } from '../../models/servico.model';

@Component({
  selector: 'app-delete-service-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './delete-service-modal.html'
})
export class DeleteServiceModal {
  @Input() servico!: Servico;
  @Input() isSubmitting = false;
  @Input() isSuccess = false;
  
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
}
