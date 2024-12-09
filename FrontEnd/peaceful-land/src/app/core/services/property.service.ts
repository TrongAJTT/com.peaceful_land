import { Injectable, OnInit } from '@angular/core';
import { AuthService } from './auth.service';
import { User } from '../../dto/user';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PropertyService implements OnInit {
  private apiUrl = 'http://localhost:8080/api/properties';
  user!: User;
  constructor(
    private authService: AuthService,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
      this.user = this.authService.getUserDetails();
  }

  createProperty(
    user_id : number,
    offer: number,
    rental_period: string,
    location: string,
    location_detail: string,
    map_url: string,
    category: string,
    price: number,
    area: number,
    legal: string,
    bedrooms: number,
    toilets: number,
    entrance: number,
    frontage: number,
    house_orientation: string,
    balcony_orientation: string): Observable<any>{

    const token = this.authService.getToken();  // Lấy JWT từ AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.post<any>(`${this.apiUrl}/create-property`,
      { user_id, offer,rental_period,location,location_detail,map_url,
        category,price,area,legal,bedrooms,toilets,entrance,frontage,
        house_orientation,balcony_orientation
      },
      { headers });
  }

  uploadSubImages(proId: number,imgList: File[]): Observable<any>{
    const formData: FormData = new FormData(); 
    formData.append('property_id', proId.toString()); 
    imgList.forEach(img => {
      formData.append('images', img, img.name);
    })

    const token = this.authService.getToken();  // Lấy JWT từ AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.post<any>(`${this.apiUrl}/upload-images`, 
      formData, {headers});
  }
}
