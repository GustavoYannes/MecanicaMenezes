export interface FuncionarioListItem {
  nome: string;
  totalGeradoMensal: number;
  totalServicoMensal: number;
}

export interface FuncionarioPage {
  totalPages: number;
  totalElements: number;
  size: number;
  content: FuncionarioListItem[];
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface RegistrarFuncionarioRequest {
  cpf: string;
  senha?: string;
  nome: string;
  email: string;
  telefone: string;
}
