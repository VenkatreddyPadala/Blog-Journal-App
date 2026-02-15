import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Category, CategoryRequest } from '../models/category.model';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = `${environment.apiUrl}/categories`;

  constructor(private http: HttpClient) { }

  createCategory(category: CategoryRequest): Observable<Category> {
    return this.http.post<Category>(this.apiUrl, category);
  }

  updateCategory(id: string, category: CategoryRequest): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/${id}`, category);
  }

  deleteCategory(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  getCategoryById(id: string): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/${id}`);
  }

  getAllCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiUrl}/public/all`);
  }

  getCategoryByName(name: string): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/name/${name}`);
  }
}