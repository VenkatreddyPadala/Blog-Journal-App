import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { PostService } from '../../../core/services/post.service';
import { Post } from '../../../core/models/post.model';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, NavbarComponent],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.css'
})
export class PostDetailComponent implements OnInit {
  post: Post | null = null;
  loading = true;
  sanitizedContent: SafeHtml = '';

  constructor(
    private postService: PostService,
    private router: Router,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    this.loadPost(id);
  }

  loadPost(id: string): void {
    this.postService.getPostById(id).subscribe({
      next: (post) => {
        this.post = post;
        this.sanitizedContent = this.sanitizer.bypassSecurityTrustHtml(post.content);
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.router.navigate(['/posts']);
      }
    });
  }

  editPost(): void {
    if (this.post) {
      this.router.navigate(['/posts/edit', this.post.id]);
    }
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }
}