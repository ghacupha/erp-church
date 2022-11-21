export interface IPlaceholder {
  id?: number;
  placeholderIndex?: string;
  placeholderValue?: string | null;
  archetype?: IPlaceholder | null;
}

export const defaultValue: Readonly<IPlaceholder> = {};
