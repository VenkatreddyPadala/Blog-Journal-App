import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../../shared/components/navbar/navbar.component';
import { CategoryService } from '../../../core/services/category.service';
import { Category } from '../../../core/models/category.model';

@Component({
  selector: 'app-category-manager',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, NavbarComponent],
  templateUrl: './category-manager.component.html',
  styleUrl: './category-manager.component.css'
})
export class CategoryManagerComponent implements OnInit {
  categories: Category[] = [];
  categoryForm: FormGroup;
  editingId: string | null = null;
  loading = true;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
    private categoryService: CategoryService
  ) {
    this.categoryForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.maxLength(50)]],
      description: [''],
      color: ['#667eea'],
      icon: ['📁']
    });
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.categoryService.getAllCategories().subscribe({
      next: (categories) => {
        this.categories = categories;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error:', error);
        this.loading = false;
      }
    });
  }

  get f() { return this.categoryForm.controls; }

  submit(): void {
    if (this.categoryForm.invalid) {
      return;
    }

    if (this.editingId) {
      this.updateCategory();
    } else {
      this.createCategory();
    }
  }

  createCategory(): void {
    this.categoryService.createCategory(this.categoryForm.value).subscribe({
      next: () => {
        this.loadCategories();
        this.categoryForm.reset({ color: '#667eea', icon: '📁' });
      },
      error: (error) => {
        this.error = error.error?.message || 'Failed to create category';
      }
    });
  }

  updateCategory(): void {
    if (!this.editingId) return;

    this.categoryService.updateCategory(this.editingId, this.categoryForm.value).subscribe({
      next: () => {
        this.loadCategories();
        this.cancelEdit();
      },
      error: (error) => {
        this.error = error.error?.message || 'Failed to update category';
      }
    });
  }

  editCategory(category: Category): void {
    this.editingId = category.id;
    this.categoryForm.patchValue({
      name: category.name,
      description: category.description,
      color: category.color,
      icon: category.icon
    });
  }

  cancelEdit(): void {
    this.editingId = null;
    this.categoryForm.reset({ color: '#667eea', icon: '📁' });
  }

  deleteCategory(id: string): void {
    if (confirm('Are you sure? This will fail if the category has posts.')) {
      this.categoryService.deleteCategory(id).subscribe({
        next: () => this.loadCategories(),
        error: (error) => {
          this.error = error.error?.message || 'Cannot delete category with posts';
        }
      });
    }
  }
}