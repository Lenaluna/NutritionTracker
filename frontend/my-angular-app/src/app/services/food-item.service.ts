import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FoodItem } from '../models/food-item.model';

@Injectable({
  providedIn: 'root'
})
export class FoodItemService {
  private baseUrl = '/api/food-items';

  constructor(private http: HttpClient) {}

  getFoodItems(): Observable<FoodItem[]> {
    return this.http.get<FoodItem[]>(this.baseUrl);
  }

  getFoodItemById(id: string): Observable<FoodItem> {
    return this.http.get<FoodItem>(`${this.baseUrl}/${id}`);
  }

  createFoodItem(foodItem: FoodItem): Observable<FoodItem> {
    return this.http.post<FoodItem>(this.baseUrl, foodItem);
  }

  updateFoodItem(id: string, foodItem: FoodItem): Observable<FoodItem> {
    return this.http.put<FoodItem>(`${this.baseUrl}/${id}`, foodItem);
  }

  deleteFoodItem(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
