import { Component } from '@angular/core';
import { RouterModule } from '@angular/router'; // <-- Add this

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [RouterModule], // <-- Add this
  templateUrl: './landing.html',
  styleUrl: './landing.css'
})
export class LandingComponent { }