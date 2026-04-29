import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Sidebar } from '../../shared/components/sidebar/sidebar';
import { TopNavbar } from '../../shared/components/top-navbar/top-navbar';

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, Sidebar, TopNavbar],
  templateUrl: './dashboard-layout.html',
  styles: []
})
export class DashboardLayout {
  isMobileMenuOpen = false;
  isMobile = window.innerWidth < 1024; // lg breakpoint em Tailwind

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.isMobile = event.target.innerWidth < 1024;
    // Se voltar para tela grande, reseta o estado do menu para evitar travamentos
    if (!this.isMobile && this.isMobileMenuOpen) {
      this.isMobileMenuOpen = false;
    }
  }

  toggleMobileMenu() {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }

  closeMobileMenu() {
    if (this.isMobile) {
      this.isMobileMenuOpen = false;
    }
  }
}
