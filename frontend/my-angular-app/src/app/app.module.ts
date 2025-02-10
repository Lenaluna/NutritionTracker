import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { FoodItemComponent } from './components/food-item/food-item.component';
import { NutritionLogComponent } from './components/nutrition-log/nutrition-log.component';
import { UserComponent } from './components/user/user.component';

@NgModule({
  declarations: [
    FoodItemComponent,
    NutritionLogComponent,
    UserComponent
  ],
  exports: [
    MatButtonModule,
    MatInputModule,
    MatCardModule,
    MatToolbarModule,
    MatTableModule,
    FoodItemComponent,
    NutritionLogComponent,
    UserComponent
  ]
})
export class MaterialModule { }
