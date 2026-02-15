import { Component, forwardRef, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { QuillModule } from 'ngx-quill';

@Component({
  selector: 'app-rich-text-editor',
  standalone: true,
  imports: [CommonModule, QuillModule, FormsModule],  // ← Added FormsModule here!
  templateUrl: './rich-text-editor.component.html',
  styleUrl: './rich-text-editor.component.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RichTextEditorComponent),
      multi: true
    }
  ]
})
export class RichTextEditorComponent implements ControlValueAccessor, OnInit {
  content = '';
  disabled = false;

  quillConfig = {
    toolbar: [
      ['bold', 'italic', 'underline', 'strike'],
      ['blockquote', 'code-block'],
      [{ 'header': 1 }, { 'header': 2 }],
      [{ 'list': 'ordered'}, { 'list': 'bullet' }],
      [{ 'script': 'sub'}, { 'script': 'super' }],
      [{ 'indent': '-1'}, { 'indent': '+1' }],
      [{ 'size': ['small', false, 'large', 'huge'] }],
      [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
      [{ 'color': [] }, { 'background': [] }],
      [{ 'align': [] }],
      ['clean'],
      ['link', 'image']
    ]
  };

  private onChange: (value: string) => void = () => {};
  private onTouched: () => void = () => {};

  ngOnInit(): void {}

  writeValue(value: string): void {
    this.content = value || '';
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  onContentChanged(event: any): void {
    this.content = event.html;
    this.onChange(this.content);
    this.onTouched();
  }
}