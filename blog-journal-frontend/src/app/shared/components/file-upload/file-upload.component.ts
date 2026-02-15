import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MediaService, Media } from '../../../core/services/media.service';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.css'
})
export class FileUploadComponent {
  @Output() fileUploaded = new EventEmitter<Media>();
  
  selectedFile: File | null = null;
  preview: string | null = null;
  uploading = false;
  error = '';

  constructor(private mediaService: MediaService) {}

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      // Validate file type
      if (!file.type.startsWith('image/')) {
        this.error = 'Please select an image file';
        return;
      }

      // Validate file size (10MB)
      if (file.size > 10 * 1024 * 1024) {
        this.error = 'File size must not exceed 10MB';
        return;
      }

      this.selectedFile = file;
      this.error = '';

      // Create preview
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.preview = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  upload(): void {
    if (!this.selectedFile) {
      this.error = 'Please select a file';
      return;
    }

    this.uploading = true;
    this.error = '';

    this.mediaService.uploadFile(this.selectedFile).subscribe({
      next: (media) => {
        this.uploading = false;
        this.fileUploaded.emit(media);
        this.reset();
      },
      error: (error) => {
        this.uploading = false;
        this.error = error.error?.message || 'Upload failed';
      }
    });
  }

  cancel(): void {
    this.reset();
  }

  private reset(): void {
    this.selectedFile = null;
    this.preview = null;
    this.error = '';
  }
}