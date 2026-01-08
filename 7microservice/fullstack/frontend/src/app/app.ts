import {Component, inject, OnInit, signal} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Header} from './shared/header/header';
import {OidcSecurityService} from 'angular-auth-oidc-client';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit{
  title = 'microservices-shop-frontend';

//  protected readonly title = signal('my first angular app');
  private readonly oidSecurityService = inject(OidcSecurityService);
  ngOnInit() {
    this.oidSecurityService.checkAuth().subscribe(
      ({isAuthenticated}) => {
        console.log('app authenticated', isAuthenticated);
      }
    )
  }
}
