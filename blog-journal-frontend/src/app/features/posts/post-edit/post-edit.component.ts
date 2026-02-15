import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { RichTextEditorComponent } from '../../../shared/components/rich-text-editor/rich-text-editor.component';
import { PostService } from '../../../core/services/post.service';
import { CategoryService } from '../../../core/services/category.service';
import { Category } from '../../../core/models/category.model';

@Component({
  selector: 'app-post-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterLink,
    NavbarComponent,
    RichTextEditorComponent
  ],
  templateUrl: './post-edit.component.html',
  styleUrl: './post-edit.component.css'
})
export class PostEditComponent implements OnInit {
  postForm: FormGroup;
  categories: Category[] = [];
  tags: string[] = [];
  tagInput = '';
  loading = true;
  saving = false;
  error = '';
  postId = '';

  constructor(
    private formBuilder: FormBuilder,
    private postService: PostService,
    private categoryService: CategoryService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.postForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.maxLength(200)]],
      content: ['', Validators.required],
      excerpt: [''],
      categoryIds: [[]],
      published: [true],
      draft: [false]
    });
  }

  ngOnInit(): void {
    this.postId = this.route.snapshot.params['id'];
    this.loadCategories();
    this.loadPost();
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => console.error('Error:', error)
    });
  }

  loadPost(): void {
    this.postService.getPostById(this.postId).subscribe({
      next: (post) => {
        this.postForm.patchValue({
          title: post.title,
          content: post.content,
          excerpt: post.excerpt,
          categoryIds: post.categoryIds,
          published: post.published,
          draft: post.draft
        });
        this.tags = post.tags || [];
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.router.navigate(['/posts']);
      }
    });
  }

  get f() { return this.postForm.controls; }

  addTag(): void {
    const tag = this.tagInput.trim();
    if (tag && !this.tags.includes(tag)) {
      this.tags.push(tag);
      this.tagInput = '';
    }
  }

  removeTag(tag: string): void {
    this.tags = this.tags.filter(t => t !== tag);
  }

  onCategoryChange(event: any, categoryId: string): void {
    const categoryIds = this.postForm.get('categoryIds')?.value || [];
    if (event.target.checked) {
      this.postForm.patchValue({
        categoryIds: [...categoryIds, categoryId]
      });
    } else {
      this.postForm.patchValue({
        categoryIds: categoryIds.filter((id: string) => id !== categoryId)
      });
    }
  }

  isCategorySelected(categoryId: string): boolean {
    return this.postForm.get('categoryIds')?.value?.includes(categoryId) || false;
  }

  update(): void {
    if (this.postForm.invalid) {
      this.error = 'Please fill in all required fields';
      return;
    }

    this.saving = true;
    this.error = '';

    const postData = {
      ...this.postForm.value,
      tags: this.tags
    };

    this.postService.updatePost(this.postId, postData).subscribe({
      next: (post) => {
        this.router.navigate(['/posts', post.id]);
      },
      error: (error) => {
        this.error = error.error?.message || 'Failed to update post';
        this.saving = false;
      }
    });
  }
}