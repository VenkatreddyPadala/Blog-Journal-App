export interface Category {
  id: string;
  name: string;
  description?: string;
  color?: string;
  icon?: string;
  postCount: number;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface CategoryRequest {
  name: string;
  description?: string;
  color?: string;
  icon?: string;
}