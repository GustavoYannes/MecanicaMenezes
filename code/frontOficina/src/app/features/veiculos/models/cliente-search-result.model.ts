export interface ClienteSearchResult {
  nome: string;
  CPF: string;
  totalGasto: number;
}

export interface ClienteSearchPage {
  content: ClienteSearchResult[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
