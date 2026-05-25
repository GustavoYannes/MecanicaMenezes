import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-release-vehicle-modal',
  standalone: true,
  templateUrl: './release-vehicle-modal.html'
})
export class ReleaseVehicleModal {
  @Input() isSubmitting = false;
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();
}
