export interface DashboardResumo {
  quantidadeCarrosAtendidos: number;
  tempoMedioPermanenciaDias: number;
}

export interface DashboardEntradas {
  abertas: number;
  fechadas: number;
}

export interface DashboardEvolucaoItem {
  periodo: string;
  inicio: string;
  fim: string;
  quantidadeServicos: number;
  faturamento: number;
}

export interface DashboardEvolucao {
  diaria: DashboardEvolucaoItem[];
  semanal: DashboardEvolucaoItem[];
  mensal: DashboardEvolucaoItem[];
}

export interface DashboardData {
  inicio: string;
  fim: string;
  resumo: DashboardResumo;
  entradas: DashboardEntradas;
  evolucao: DashboardEvolucao;
}
