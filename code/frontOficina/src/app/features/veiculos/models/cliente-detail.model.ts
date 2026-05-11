export interface Endereco {
  cidade: string;
  bairro: string;
  rua: string;
  numero: string;
  cep: number | string;
  estado: string;
}

export interface ClienteDetail {
  nomeCompleto: string;
  email: string;
  cpf: string;
  telefone: string;
  endereco: Endereco;
}
