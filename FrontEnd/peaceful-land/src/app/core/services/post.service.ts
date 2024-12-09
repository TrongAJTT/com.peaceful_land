import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { User } from '../../dto/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = 'http://localhost:8080/api/posts';
  user!: User;
  constructor(
    private authService: AuthService,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
      this.user = this.authService.getUserDetails();
  }

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
}
