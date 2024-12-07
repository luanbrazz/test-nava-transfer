import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'nava-transfer-ui';

  constructor(private router: Router) {}

  isDashboardRoute(): boolean {
    return this.router.url === '/dashboard';
  }

  logout(): void {
    localStorage.removeItem('jwt');
    this.router.navigate(['/login']);
  }
}
