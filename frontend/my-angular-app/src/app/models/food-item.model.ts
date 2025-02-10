export interface FoodItem {
  id?: string;
  name: string;
  proteinContent: number;
  aminoAcidProfile: Record<string, number>;
}
