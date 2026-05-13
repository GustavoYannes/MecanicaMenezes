import { ClienteDetail } from './cliente-detail.model';

export interface VeiculoDetail {
  placa: string;
  marca: string;
  modelo: string;
  ano: number;
  cor: string;
  km: number;
  cliente?: ClienteDetail;
}
