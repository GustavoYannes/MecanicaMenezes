import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span 
      class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold"
      [ngClass]="getBadgeClasses()">
      {{ getStatusLabel() }}
    </span>
  `
})
export class StatusBadge {
  @Input() status: string = '';

  getBadgeClasses(): string {
    const normalized = this.status?.toUpperCase() || '';
    if (normalized === 'EM_ESPERA' || normalized === 'ESPERA') {
      return 'bg-stone-100 text-stone-600 border border-stone-200 shadow-sm';
    } else if (normalized.includes('PROGRESSO') || normalized.includes('PROGRESO')) {
      return 'bg-[#FFF0E6] text-brand-accent border border-brand-accent/20 shadow-sm';
    } else if (normalized === 'CONCLUIDO') {
      return 'bg-emerald-50 text-emerald-700 border border-emerald-200 shadow-sm';
    }
    return 'bg-[#F5ECE5] text-stone-600 border border-[#D5C2B3] shadow-sm'; // fallback
  }

  getStatusLabel(): string {
    const normalized = this.status?.toUpperCase() || '';
    if (normalized === 'EM_ESPERA' || normalized === 'ESPERA') return 'Em espera';
    if (normalized.includes('PROGRESSO') || normalized.includes('PROGRESO')) return 'Em progresso';
    if (normalized === 'CONCLUIDO') return 'Concluído';
    return this.status;
  }
}
