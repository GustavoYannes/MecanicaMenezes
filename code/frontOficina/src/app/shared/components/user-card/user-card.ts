import { Component, inject, OnInit } from '@angular/core';
import { TokenService } from '../../../core/services/token.service';
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
  userName = 'Usuário';
  userInitials = 'U';

  ngOnInit() {
    const storedName = this.tokenService.getUserName();
    if (storedName) {
      this.userName = storedName;
      this.userInitials = this.getInitials(storedName);
    }
  }

  private getInitials(name: string): string {
    const parts = name.trim().split(' ');
    if (parts.length > 1) {
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    }
    return name.slice(0, 2).toUpperCase();
  }
}
