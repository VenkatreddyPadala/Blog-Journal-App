export interface User {
  id: string;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  profilePicture?: string;
  roles: string[];
  enabled: boolean;
  createdAt?: Date;
  updatedAt?: Date;
}

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName?: string;
  lastName?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  id: string;
  username: string;
  email: string;
  roles: string[];
}