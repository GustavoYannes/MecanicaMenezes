import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/pages/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: '',
    loadComponent: () => import('./layout/dashboard-layout/dashboard-layout').then(m => m.DashboardLayout),
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        loadComponent: () => import('./features/dashboard/pages/dashboard/dashboard').then(m => m.Dashboard)
      },
      {
        path: 'veiculos',
        loadComponent: () => import('./features/veiculos/pages/veiculos/veiculos').then(m => m.Veiculos)
      },
      {
        path: 'clientes',
        loadComponent: () => import('./features/clientes/pages/clientes/clientes').then(m => m.Clientes)
      },
      {
        path: 'orcamentos',
        loadComponent: () => import('./features/orcamentos/pages/orcamentos/orcamentos').then(m => m.Orcamentos)
      },
      {
        path: 'funcionarios',
        loadComponent: () => import('./features/funcionarios/pages/funcionarios/funcionarios').then(m => m.Funcionarios)
      },
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '**',
    redirectTo: '/login'
  }
];
