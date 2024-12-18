import { Component } from '@angular/core';
import { OAuthService, OAuthErrorEvent, OAuthModule } from 'angular-oauth2-oidc';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,  // Mark this component as standalone
  template: `<h1>Book List App</h1>
    <p>
      <button *ngIf="!isLoggedIn()" (click)='login()'>Log In</button>
      <button *ngIf="isLoggedIn()" (click)='loadBooks()'>Load Books</button>
      <button (click)='logout()'>Log out</button>
      <button (click)='refresh()'>Refresh</button>
    </p>
    
    <span *ngIf="isLoggedIn()"><div *ngFor="let book of books">
      {{book.name}}
    </div></span>
  `,
  styles: [],
  imports: [CommonModule, OAuthModule]
})

export class AppComponent {
  username = '';
  books: Book[];

  constructor(private oauthService: OAuthService, private http: HttpClient) {

    this.books = [];
    // For debugging:
    oauthService.events.subscribe(e => e instanceof OAuthErrorEvent ? console.error(e) : console.warn(e));

    // Load information from Auth0 (could also be configured manually)
    oauthService.loadDiscoveryDocument()

      // See if the hash fragment contains tokens (when user got redirected back)
      .then(() => oauthService.tryLogin())

      // If we're still not logged in yet, try with a silent refresh:
      .then(() => {
        if (!oauthService.hasValidAccessToken()) {
          return oauthService.silentRefresh();
        }
        return;
      })

      // Get username, if possible.
      .then(() => {
        if (oauthService.getIdentityClaims()) {
          this.username = oauthService.getIdentityClaims()['name'];
        }
      });

    oauthService.setupAutomaticSilentRefresh();
  }

  get token() { 
    this.oauthService.setStorage(sessionStorage);
    var token = this.oauthService.getAccessToken();
    console.log('Access Token = ' + token);
    return token; 
  }

  get claims() { 
    return this.oauthService.getIdentityClaims(); 
  }

  login() { 
    this.oauthService.initCodeFlow();
  }
  logout() { 
    this.oauthService.logOut(); 
  }
  refresh() { 
    this.oauthService.silentRefresh(); 
  }

  isLoggedIn(){
    if (this.oauthService.getAccessToken() === null){
       return false;
    }
    return true;
  } 

  loadBooks(){
     
     this.http
      .get<Book[]>('http://localhost:8080/books', 
        {headers: {'Authorization': 'Bearer '+ this.oauthService.getAccessToken()}})
      .subscribe(data => {this.books = data});
  }
}

interface Book {
  id: string;
  name: string;
}