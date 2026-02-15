import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  {
    path: 'auth',
    children: [
      {
        path: 'login',
        loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
      },
      {
        path: 'register',
        loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
      }
    ]
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/dashboard/dashboard/dashboard.component').then(m => m.DashboardComponent),
    canActivate: [authGuard]
  },
  {
    path: 'posts',
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./features/posts/post-list/post-list.component').then(m => m.PostListComponent)
      },
      {
        path: 'create',
        loadComponent: () => import('./features/posts/post-create/post-create.component').then(m => m.PostCreateComponent)
      },
      {
        path: 'edit/:id',
        loadComponent: () => import('./features/posts/post-edit/post-edit.component').then(m => m.PostEditComponent)
      },
      {
        path: ':id',
        loadComponent: () => import('./features/posts/post-detail/post-detail.component').then(m => m.PostDetailComponent)
      }
    ]
  },
  {
    path: 'categories',
    loadComponent: () => import('./features/categories/category-manager/category-manager.component').then(m => m.CategoryManagerComponent),
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: '/dashboard' }
];