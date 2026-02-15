import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment.development';
import { Post, PostRequest, PageResponse } from '../models/post.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = `${environment.apiUrl}/posts`;

  constructor(private http: HttpClient) { }

  createPost(post: PostRequest): Observable<Post> {
    return this.http.post<Post>(this.apiUrl, post);
  }

  updatePost(id: string, post: PostRequest): Observable<Post> {
    return this.http.put<Post>(`${this.apiUrl}/${id}`, post);
  }

  deletePost(id: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  getPostById(id: string): Observable<Post> {
    return this.http.get<Post>(`${this.apiUrl}/${id}`);
  }

  getAllPosts(page: number = 0, size: number = 10, sortBy: string = 'publishedAt'): Observable<PageResponse<Post>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy);
    return this.http.get<PageResponse<Post>>(`${this.apiUrl}/public/all`, { params });
  }

  getMyPosts(page: number = 0, size: number = 10, sortBy: string = 'updatedAt'): Observable<PageResponse<Post>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy);
    return this.http.get<PageResponse<Post>>(`${this.apiUrl}/my-posts`, { params });
  }

  getMyDrafts(page: number = 0, size: number = 10): Observable<PageResponse<Post>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Post>>(`${this.apiUrl}/my-drafts`, { params });
  }

  getPostsByCategory(categoryId: string, page: number = 0, size: number = 10): Observable<PageResponse<Post>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Post>>(`${this.apiUrl}/category/${categoryId}`, { params });
  }

  getPostsByTag(tag: string, page: number = 0, size: number = 10): Observable<PageResponse<Post>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Post>>(`${this.apiUrl}/tag/${tag}`, { params });
  }

  searchPosts(searchTerm: string, page: number = 0, size: number = 10): Observable<PageResponse<Post>> {
    const params = new HttpParams()
      .set('q', searchTerm)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Post>>(`${this.apiUrl}/search`, { params });
  }

  searchMyPosts(searchTerm: string, page: number = 0, size: number = 10): Observable<PageResponse<Post>> {
    const params = new HttpParams()
      .set('q', searchTerm)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PageResponse<Post>>(`${this.apiUrl}/my-posts/search`, { params });
  }
}