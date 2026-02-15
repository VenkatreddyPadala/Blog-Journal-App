import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { RichTextEditorComponent } from '../../../shared/components/rich-text-editor/rich-text-editor.component';
import { PostService } from '../../../core/services/post.service';
import { CategoryService } from '../../../core/services/category.service';
import { Category } from '../../../core/models/category.model';

@Component({
  selector: 'app-post-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterLink,
    NavbarComponent,
    RichTextEditorComponent
  ],
  templateUrl: './post-create.component.html',
  styleUrl: './post-create.component.css'
})
export class PostCreateComponent implements OnInit {
  postForm: FormGroup;
  categories: Category[] = [];
  tags: string[] = [];
  tagInput = '';
  loading = false;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
    private postService: PostService,
    private categoryService: CategoryService,
    private router: Router
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
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
      },
      error: (error) => console.error('Error loading categories:', error)
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

  saveDraft(): void {
    if (this.postForm.get('title')?.invalid) {
      this.error = 'Please enter a title';
      return;
    }

    this.postForm.patchValue({
      draft: true,
      published: false
    });

    this.submit();
  }

  publish(): void {
    if (this.postForm.invalid) {
      this.error = 'Please fill in all required fields';
      return;
    }

    this.postForm.patchValue({
      draft: false,
      published: true
    });

    this.submit();
  }

  private submit(): void {
    this.loading = true;
    this.error = '';

    const postData = {
      ...this.postForm.value,
      tags: this.tags
    };

    this.postService.createPost(postData).subscribe({
      next: (post) => {
        this.router.navigate(['/posts', post.id]);
      },
      error: (error) => {
        this.error = error.error?.message || 'Failed to create post';
        this.loading = false;
      }
    });
  }
}