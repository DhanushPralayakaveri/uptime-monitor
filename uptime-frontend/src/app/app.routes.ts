import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard';
import { LoginComponent } from './login/login';
import { LandingComponent } from './landing/landing';

export const routes: Routes = [
  { path: '', component: LandingComponent }, // <-- Sets Landing as the home page
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent }
];