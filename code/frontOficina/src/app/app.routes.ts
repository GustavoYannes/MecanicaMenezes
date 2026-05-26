import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

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
        loadComponent: () => import('./features/veiculos/pages/veiculos-list/veiculos-list').then(m => m.VeiculosList)
      },
      {
        path: 'veiculos/:placa',
        loadComponent: () => import('./features/veiculos/pages/vehicle-details/vehicle-details').then(m => m.VehicleDetails)
      },
      {
        path: 'historico-veiculos',
        loadComponent: () => import('./features/veiculos/pages/historico-veiculos/historico-veiculos').then(m => m.HistoricoVeiculos)
      },
      {
        path: 'historico-veiculos/:placa',
        loadComponent: () => import('./features/veiculos/pages/vehicle-history-details/vehicle-history-details').then(m => m.VehicleHistoryDetails)
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
        canActivate: [authGuard, roleGuard],
        data: { roles: ['GERENTE'] },
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
