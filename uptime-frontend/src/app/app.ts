import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router'; // <-- 1. Import the RouterOutlet

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet], // <-- 2. Add it to the imports array
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {
  title = 'uptime-frontend';
}