import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserCard } from '../user-card/user-card';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-top-navbar',
  standalone: true,
  imports: [CommonModule, UserCard, RouterModule],
  templateUrl: './top-navbar.html',
  styles: []
})
export class TopNavbar {
  @Output() toggleMenu = new EventEmitter<void>();
  pageTitle = 'Dashboard';

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.updateTitleFromUrl(event.urlAfterRedirects);
    });
  }

  onMenuClick() {
    this.toggleMenu.emit();
  }

  private updateTitleFromUrl(url: string) {
    if (url.includes('dashboard')) this.pageTitle = 'Dashboard';
    else if (url.includes('veiculos')) this.pageTitle = 'Veículos';
    else if (url.includes('clientes')) this.pageTitle = 'Clientes';
    else if (url.includes('orcamentos')) this.pageTitle = 'Orçamentos';
    else if (url.includes('funcionarios')) this.pageTitle = 'Funcionários';
    else this.pageTitle = 'Dashboard';
  }
}
