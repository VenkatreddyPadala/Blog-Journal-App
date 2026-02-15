import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { PostService } from '../../../core/services/post.service';
import { CategoryService } from '../../../core/services/category.service';
import { Post } from '../../../core/models/post.model';
import { Category } from '../../../core/models/category.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  recentPosts: Post[] = [];
  categories: Category[] = [];
  stats = {
    totalPosts: 0,
    publishedPosts: 0,
    draftPosts: 0,
    totalCategories: 0
  };
  loading = true;

  constructor(
    private postService: PostService,
    private categoryService: CategoryService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  loadDashboardData(): void {
    this.loading = true;

    // Load recent posts
    this.postService.getMyPosts(0, 5).subscribe({
      next: (response) => {
        this.recentPosts = response.content;
        this.stats.totalPosts = response.totalElements;
        this.calculateStats();
      },
      error: (error) => console.error('Error loading posts:', error)
    });

    // Load categories
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.stats.totalCategories = categories.length;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading categories:', error);
        this.loading = false;
      }
    });
  }

  calculateStats(): void {
    this.stats.publishedPosts = this.recentPosts.filter(p => p.published && !p.draft).length;
    this.stats.draftPosts = this.recentPosts.filter(p => p.draft).length;
  }

  viewPost(postId: string): void {
    this.router.navigate(['/posts', postId]);
  }

  editPost(postId: string): void {
    this.router.navigate(['/posts/edit', postId]);
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  getExcerpt(content: string, maxLength: number = 150): string {
    const text = content.replace(/<[^>]*>/g, '');
    return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
  }
}