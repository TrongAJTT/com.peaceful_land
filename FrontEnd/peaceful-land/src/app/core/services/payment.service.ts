import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = 'http://localhost:8080/api/payments';
  constructor(
    private authService: AuthService,
    private http: HttpClient
  ) { }

  payByVnPay(amount: number, orderInfo: string, ticketId:number): Observable<{ redirectUrl: string}>{
    const token = this.authService.getToken();  // Lấy JWT từ AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const formData: FormData = new FormData(); 
    formData.append('amount', amount.toString()); 
    formData.append('orderInfo', orderInfo); 
    formData.append('ticketId', ticketId.toString()); 
    
    return this.http.post<{ redirectUrl: string}>(`${this.apiUrl}/vnpay`,
      formData, {headers});
  }
}
