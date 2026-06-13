import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../auth/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  credentials = { email: '', password: '' };
  errorMessage = '';
  successMessage = '';
  isLoading = false;
  isRegisterMode = false;

  constructor(
    private authService: AuthService, 
    private router: Router,
    private http: HttpClient
  ) {}

  toggleMode() {
    this.isRegisterMode = !this.isRegisterMode;
    this.errorMessage = '';
    this.successMessage = '';
  }

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.isRegisterMode) {
      // ---> 1. CREATE THE ACCOUNT <---
      this.http.post('http://localhost:8080/api/users/register', this.credentials, { responseType: 'text' }).subscribe({
        next: () => {
          this.successMessage = 'Account created! Securing session...';
          
          // ---> 2. INSTANT AUTO-LOGIN <---
          // We don't wait. We immediately hit the auth service with the credentials they just typed.
          this.authService.login(this.credentials).subscribe({
            next: () => {
              // Teleport directly to the dashboard!
              this.router.navigate(['/dashboard']);
            },
            error: () => {
              // Fallback just in case the auto-login fails
              this.isLoading = false;
              this.errorMessage = 'Account created, but auto-login failed. Please sign in.';
              this.toggleMode();
            }
          });
        },
        error: (err) => {
          this.isLoading = false;
          this.errorMessage = err.error || 'Registration failed. Try a different email.';
        }
      });
    } else {
      // ---> STANDARD LOGIN LOGIC <---
      this.authService.login(this.credentials).subscribe({
        next: () => {
          this.router.navigate(['/dashboard']);
        },
        error: (err: any) => {
          this.isLoading = false;
          this.errorMessage = 'Invalid email or password. Please try again.';
        }
      });
    }
  }
}