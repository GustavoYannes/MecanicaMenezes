export interface FuncionarioListItem {
  id: string;
  nome: string;
  totalGeradoMensal?: number;
  totalServicoMensal?: number;
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

export interface ServicoDoMes {
  id: number;
  nome: string;
  data: string;
  quantidade: number;
  valor: number;
  valorTotal: number;
}

export interface FuncionarioResponse {
  nome: string;
  cpf: string;
  email: string;
  servicosDoMes: ServicoDoMes[];
}

export interface FuncionarioPerfilResponse {
  nome: string;
  cpf: string;
  email: string;
  telefone?: string;
}

export interface ServicoResponse {
  id: number;
  nome: string;
  data: string;
  quantidade: number;
  valor: number;
  valorTotal: number;
}

export interface PageResponse<T> {
  totalPages: number;
  totalElements: number;
  size: number;
  content: T[];
  number: number;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface RelatorioMensalResponse {
  qtdServico: number;
  totalGerado: number;
}


