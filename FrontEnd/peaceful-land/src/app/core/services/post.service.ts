import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, OnInit } from '@angular/core';
import { AuthService } from './auth.service';
import { User } from '../../dto/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService{
  private apiUrl = 'http://localhost:8080/api/posts';
  constructor(
    private authService: AuthService,
    private http: HttpClient
  ) { }


  createPost(property_id: number, title: string, description: string, expiration: string): Observable<any>{
    const formData: FormData = new FormData(); 
    formData.append('property_id', property_id.toString()); 
    formData.append('title', title); 
    formData.append('description', description); 
    formData.append('expiration', expiration); 

    const token = this.authService.getToken();  // Lấy JWT từ AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/create-post`, 
      {property_id,title,description,expiration}, {headers});
  }

  uploadThumnailImg(post_id: number,img: File): Observable<any>{
    const formData: FormData = new FormData(); 
    formData.append('post_id', post_id.toString()); 
    formData.append('image', img, img.name);

    const token = this.authService.getToken();  // Lấy JWT từ AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/change-thumbnail`, 
      formData, {headers});
  }

  postToAdmin(post_id: number): Observable<any>{
    const formData: FormData = new FormData(); 
    formData.append('post_id', post_id.toString()); 

    const token = this.authService.getToken();  // Lấy JWT từ AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/request-approve`, 
      {post_id}, {headers});
  }

  getPostTopK(k: number,user_id: number): Observable<any>{
    return this.http.post<any>(`${this.apiUrl}/find-nearest-${k}`, 
      {user_id});
  }


  searchPostByPage(userId: number,offer: boolean,status: boolean,page: number, size: number): Observable<any>{
    return this.http.post<any>(`${this.apiUrl}/search?page=${page}&size=${size}`, 
      {userId,offer,status});
  }

  getPostById(user_id: number, postId: number): Observable<any>{
    return this.http.post<any>(`${this.apiUrl}/${postId}`, 
      {user_id});
  }

  changeInterested(postId: number, user_id: number, interested: number, notification: number): Observable<any>{
    const token = this.authService.getToken();  // Lấy JWT từ AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/${postId}/interest`, 
      {user_id, interested,notification}, {headers});
  }

  requestPermitContact(user_id: number,postId: number): Observable<any>{
    const token = this.authService.getToken();  // Lấy JWT từ AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/${postId}/request-permission`, 
      {user_id}, {headers});
  }

  makeSchedule(user_id:number ,postId: number,type: number,expected_date:string,expected_hour: number,
    name:string,phone: string,email: string,
    interest_level:number): Observable<any>{
      const token = this.authService.getToken();  // Lấy JWT từ AuthService
      const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
      return this.http.post<any>(`${this.apiUrl}/${postId}/request-tour`, 
        {user_id,type,expected_date,expected_hour,name,phone,email,interest_level}, {headers});
    }

  makeContact(user_id:number ,postId:number,name:string, phone: string,email: string,
    interest_level:number,message: string): Observable<any>{
    const token = this.authService.getToken();  // Lấy JWT từ AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/${postId}/request-contact`, 
      {user_id,name,phone,email,interest_level,message}, {headers});
  }

  getLogsOfPro(proId: number): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/${proId}/property-logs`);
  }

  getLogsOfPost(proId: number): Observable<any>{
    return this.http.get<any>(`${this.apiUrl}/${proId}/post-logs`);
  }
}
