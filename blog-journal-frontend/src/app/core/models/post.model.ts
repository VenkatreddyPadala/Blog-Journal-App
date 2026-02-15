import { User } from './user.model';
import { Category } from './category.model';

export interface Post {
  id: string;
  title: string;
  content: string;
  excerpt?: string;
  author?: User;
  authorId: string;
  tags: string[];
  categories?: Category[];
  categoryIds: string[];
  mediaFiles: any[];
  published: boolean;
  draft: boolean;
  createdAt: Date;
  updatedAt: Date;
  publishedAt?: Date;
  viewCount: number;
  deleted: boolean;
}

export interface PostRequest {
  title: string;
  content: string;
  excerpt?: string;
  tags: string[];
  categoryIds: string[];
  published: boolean;
  draft: boolean;
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}