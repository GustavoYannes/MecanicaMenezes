export interface VeiculoSearchResult {
  placa: string;
  modelo?: string;
  marca?: string;
}

export interface VeiculoSearchPage {
  content: VeiculoSearchResult[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
