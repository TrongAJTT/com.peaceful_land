import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userRole: 'none' |'user' | 'sale' | 'admin' = 'none';
  authStatusChanged: EventEmitter<'none' |'user' | 'sale' | 'admin'> = new EventEmitter();


  private apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { 
    this.loadAuthStatus()
  }

  private loadAuthStatus() {
    const savedUserRole = localStorage.getItem('userRole') as 'none' |'user' | 'sale' | 'admin';
    if (savedUserRole) {
      this.userRole = savedUserRole;
      this.authStatusChanged.emit(this.userRole);
    }
  }

  // Save JWT token to localStorage
  saveToken(token: string): void {
    localStorage.setItem('auth_token', token);
  }

  login(userId: string, password: string): Observable<any> {
    return this.http.post<any>(this.apiUrl + "/login", { userId, password }).pipe(
      tap(response => {
        if (response.token) {
          this.saveToken(response.token); // Save token after successful login
          this.setUserDetails(response.user)
          this.loadRole(response.account.role)
          this.authStatusChanged.emit(this.userRole);
        }
      })
    );
  }

  loadRole(role: number){
    switch (role){
      case 0:
        this.userRole = 'user';
        break;
      case 1:
        this.userRole = 'sale';
        break;
      case 2:
        this.userRole = 'sale';
        break;
      case 3:
        this.userRole = 'admin';
        break;
    }
    this.saveAuthStatus();
  }

  private saveAuthStatus() {
    localStorage.setItem('userRole', this.userRole);
  }

  register(email: string, password: string, phone: string, name: string,gender: boolean): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { email, password ,phone,name, gender});
  }

  // Optionally, you can store the user details in local storage after successful login
  setUserDetails(user: any): void {
    localStorage.setItem('user', JSON.stringify(user)); // Save user to localStorage
  }

  getUserDetails(): any {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null; // Retrieve user from localStorage
  }

  logout() {
    this.userRole = 'none';
    this.removeToken(); // Remove user from localStorage
    this.saveAuthStatus();
    this.authStatusChanged.emit(this.userRole);
  }

  // Get JWT token from localStorage
  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  // Remove JWT token (when logging out)
  removeToken(): void {
    localStorage.removeItem('auth_token');
  }

  // Auth
  getAuthStatus() {
    return this.userRole !== 'none';
  }

  getSaleOrUserStatus() {
    return this.userRole == 'sale' || 'user';
  }

  getAdminStatus() {
    return this.userRole === 'admin';
  }


}
