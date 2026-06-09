import { Component, EventEmitter, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { VehicleModalService } from '../../../features/veiculos/services/vehicle-modal.service';
import { TokenService } from '../../../core/services/token.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.html',
  styles: []
})
export class Sidebar {
  @Output() closeMenu = new EventEmitter<void>();

  private modalService = inject(VehicleModalService);
  private tokenService = inject(TokenService);

  // Menu items built once at construction time to avoid creating new arrays every CD cycle
  readonly menuItems: { label: string; path: string; icon: string }[];

  constructor() {
    const items = [
      { label: 'Dashboard', path: '/dashboard', icon: 'M4 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2V6zM14 6a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2V6zM4 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2H6a2 2 0 01-2-2v-2zM14 16a2 2 0 012-2h2a2 2 0 012 2v2a2 2 0 01-2 2h-2a2 2 0 01-2-2v-2z' },
      { label: 'Carros na Oficina', path: '/veiculos', icon: 'M8 7h8a2 2 0 012 2v8M8 7H6a2 2 0 00-2 2v8m4-10v5m8-5v5M5 17h14a2 2 0 002-2v-3a2 2 0 00-2-2H5a2 2 0 00-2 2v3a2 2 0 002 2zm3-3h.01M16 14h.01' },
      { label: 'Clientes', path: '/clientes', icon: 'M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z' },
      { label: 'Histórico de Veículos', path: '/historico-veiculos', icon: 'M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z' }
    ];

    if (this.tokenService.isManager()) {
      items.push({ label: 'Funcionários', path: '/funcionarios', icon: 'M10 6H5a2 2 0 00-2 2v9a2 2 0 002 2h14a2 2 0 002-2V8a2 2 0 00-2-2h-5m-4 0V5a2 2 0 114 0v1m-4 0a2 2 0 104 0m-5 8a2 2 0 100-4 2 2 0 000 4zm0 0c1.306 0 2.417.835 2.83 2M9 14a3.001 3.001 0 00-2.83 2M15 11h3m-3 4h2' });
    }

    items.push({ label: 'Meu Perfil', path: '/meu-perfil', icon: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z' });

    this.menuItems = items;
  }

  trackByPath(index: number, item: { path: string }): string {
    return item.path;
  }

  onBackdropClick() {
    this.closeMenu.emit();
  }

  openRegistrarEntrada() {
    this.modalService.open();
    this.closeMenu.emit();
  }
}
