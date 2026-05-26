import { Component, inject, OnInit, signal, HostListener } from '@angular/core';
import { TokenService } from '../../../core/services/token.service';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-card.html',
  styles: []
})
export class UserCard implements OnInit {
  private tokenService = inject(TokenService);
  private authService = inject(AuthService);
  private router = inject(Router);

  userName = signal('Usuário');
  userInitials = signal('U');
  userRole = signal('Mecânico');
  dropdownOpen = signal(false);

  ngOnInit() {
    const storedName = this.tokenService.getUserName();
    if (storedName) {
      this.userName.set(storedName);
      this.userInitials.set(this.getInitials(storedName));
    }
    const role = this.tokenService.getUserRole();
    if (role) {
      this.userRole.set(this.formatRole(role));
    }
  }

  private formatRole(role: string): string {
    const roleMap: Record<string, string> = {
      'GERENTE': 'Gerente',
      'MECANICO': 'Mecânico',
      'ASSISTENTE': 'Assistente'
    };
    return roleMap[role.toUpperCase()] || role;
  }

  private getInitials(name: string): string {
    const parts = name.trim().split(' ');
    if (parts.length > 1) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }
    return name.slice(0, 2).toUpperCase();
  }

  toggleDropdown(event: Event) {
    event.stopPropagation();
    this.dropdownOpen.update(val => !val);
  }

  @HostListener('document:click')
  closeDropdown() {
    if (this.dropdownOpen()) {
      this.dropdownOpen.set(false);
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
