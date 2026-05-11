import { VeiculoListItem } from './veiculo-list-item.model';

export interface VeiculoPage {
  content: VeiculoListItem[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
}
