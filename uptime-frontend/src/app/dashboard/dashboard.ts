import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  endpoints: any[] = [];
  newEndpoint = { name: '', url: '' };
  isDeploying = false;
  private pollingInterval: any;

  constructor(
    private http: HttpClient, 
    private router: Router, 
    private authService: AuthService,
    private cdr: ChangeDetectorRef // <-- Forces Angular to wake up and redraw the UI
  ) {}

  ngOnInit() {
    // No more Local Storage race conditions! 
    this.loadEndpoints();
    this.pollingInterval = setInterval(() => this.loadEndpoints(), 5000);
  }

  ngOnDestroy() {
    if (this.pollingInterval) {
      clearInterval(this.pollingInterval);
    }
  }

  loadEndpoints() {
    this.http.get<any[]>(`${environment.apiUrl}/endpoints`)
      .subscribe({
        next: (data) => {
          this.endpoints = data;
          this.cdr.detectChanges(); // Force redraw the grid
        },
        error: (err) => console.error('Error fetching endpoints:', err)
      });
  }

  addEndpoint() {
    if (!this.newEndpoint.name || !this.newEndpoint.url) return;
    
    this.isDeploying = true; 
    this.cdr.detectChanges(); // Instantly update the button

    const payload = { ...this.newEndpoint };
    
    // Optimistic UI: Temporarily push the card
    const optimisticCard = {
      name: this.newEndpoint.name,
      url: this.newEndpoint.url,
      status: 'PENDING',
      lastChecked: null
    };
    this.endpoints.unshift(optimisticCard); 
    this.newEndpoint = { name: '', url: '' };
    
    this.http.post(`${environment.apiUrl}/endpoints`, payload).subscribe({
      next: () => {
        this.isDeploying = false;
        this.loadEndpoints(); // Re-syncs the grid
      },
      error: (err) => {
        this.isDeploying = false;
        console.error('Error adding endpoint:', err);
        this.loadEndpoints(); 
      }
    });
  }

  // Add this method below your addEndpoint() method
  deleteEndpoint(endpointId: number) {
    if (confirm('Are you sure you want to decommission this monitor? All historical telemetry will be lost.')) {
      this.http.delete(`${environment.apiUrl}/endpoints/${endpointId}`, { responseType: 'text' }).subscribe({
        next: () => {
          // Optimistically remove it from the UI for a snappy feel
          this.endpoints = this.endpoints.filter(ep => ep.id !== endpointId);
          this.cdr.detectChanges();
        },
        error: (err) => console.error('Error deleting endpoint:', err)
      });
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}