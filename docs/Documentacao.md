# Sistema de Gerenciamento de Oficina

Este projeto tem como objetivo o desenvolvimento de um sistema para gerenciamento de uma oficina mecânica, permitindo controlar entradas de veículos, serviços realizados, histórico de clientes e histórico de veículos. A proposta é facilitar o dia a dia do gerente ou mecânico, centralizando as informações mais importantes em um único sistema.

O funcionamento do sistema começa com o login do usuário. Após entrar, o gerente ou mecânico é direcionado para um dashboard, onde pode visualizar indicadores principais da oficina, como total de atendimentos, atendimentos concluídos, em andamento e em espera.

A principal funcionalidade do sistema é o cadastro de entrada de veículos. Nesse fluxo, caso o cliente ou o veículo ainda não estejam cadastrados, o sistema permite realizar o cadastro no momento da entrada. Esse será o único meio de cadastrar clientes e veículos no sistema. Cada entrada representa um atendimento realizado pela oficina.

Quando uma entrada é criada, o veículo recebe inicialmente o status **Em espera**. Depois disso, podem ser adicionados um ou mais serviços ao atendimento. Ao adicionar serviços, o atendimento passa a ficar **Em andamento**. Quando o mecânico finalizar o atendimento, o status da entrada passa para **Concluído**. O sistema também deve impedir que um mesmo veículo tenha duas entradas abertas ao mesmo tempo.

Ao final do atendimento, o sistema deve gerar um orçamento em PDF por entrada, contendo os dados do cliente, os dados do veículo, a lista de serviços realizados, os valores individuais e o valor total. Esse PDF deve ficar disponível para download posteriormente.

Além disso, o sistema contará com uma tela de histórico de clientes, onde será possível visualizar todos os clientes cadastrados, consultar seus detalhes e acessar o histórico de entradas realizadas. Também haverá uma tela de histórico de veículos, permitindo visualizar os veículos cadastrados, seus detalhes e seus respectivos atendimentos anteriores. Ambas as telas devem possuir filtros para facilitar a busca das informações.

Como apoio às boas práticas de desenvolvimento, o projeto também contará com integração contínua utilizando **GitHub Actions** e análise de qualidade de código com **SonarQube**, contribuindo para maior confiabilidade, organização e qualidade do sistema.
```

## Fluxo do usuário

Você pode colocar assim no documento:

```text
Início
  ↓
Login do gerente/mecânico
  ↓
Acesso ao Dashboard
  ↓
Visualiza indicadores principais da oficina
  ↓
Escolhe uma ação:

[1] Cadastrar entrada
    ↓
    Verificar se cliente já existe
    ├─ Sim → selecionar cliente
    └─ Não → cadastrar cliente
    ↓
    Verificar se veículo já existe
    ├─ Sim → selecionar veículo
    └─ Não → cadastrar veículo
    ↓
    Verificar se veículo possui entrada em aberto
    ├─ Sim → bloquear nova entrada
    └─ Não → registrar entrada
    ↓
    Status inicial: Em espera
    ↓
    Adicionar um ou mais serviços
    ↓
    Status: Em andamento
    ↓
    Finalizar atendimento
    ↓
    Status: Concluído
    ↓
    Gerar orçamento em PDF
    ↓
    Disponibilizar PDF para download

[2] Consultar histórico de clientes
    ↓
    Listar clientes
    ↓
    Aplicar filtros
    ↓
    Selecionar cliente
    ↓
    Visualizar detalhes e histórico de entradas

[3] Consultar histórico de veículos
    ↓
    Listar veículos
    ↓
    Aplicar filtros
    ↓
    Selecionar veículo
    ↓
    Visualizar detalhes e histórico de entradas
```

## Fluxo visual simples

Se quiser algo mais bonito, pode usar esse modelo:

```text
┌────────────┐
│   Login    │
└─────┬──────┘
      │
      v
┌───────────────┐
│   Dashboard   │
└───┬─────┬─────┘
    │     │
    │     │
    v     v
┌───────────────┐      ┌────────────────────┐
│Cadastrar      │      │Histórico de        │
│Entrada        │      │Clientes            │
└─────┬─────────┘      └─────────┬──────────┘
      │                          │
      v                          v
┌───────────────┐      ┌────────────────────┐
│Cliente existe?│      │Selecionar cliente  │
└─────┬─────┬───┘      │e ver detalhes      │
      │     │          └────────────────────┘
     Sim   Não
      │     │
      │     v
      │   ┌───────────────┐
      │   │Cadastrar      │
      │   │cliente        │
      │   └───────────────┘
      v
┌───────────────┐
│Veículo existe?│
└─────┬─────┬───┘
      │     │
     Sim   Não
      │     │
      │     v
      │   ┌───────────────┐
      │   │Cadastrar      │
      │   │veículo        │
      │   └───────────────┘
      v
┌──────────────────────────┐
│Veículo já tem entrada    │
│em aberto?                │
└─────┬─────────────┬──────┘
      │             │
     Sim           Não
      │             │
      v             v
┌───────────────┐  ┌────────────────┐
│Bloquear nova  │  │Registrar       │
│entrada        │  │entrada         │
└───────────────┘  └───────┬────────┘
                           │
                           v
                  ┌────────────────┐
                  │Status:         │
                  │Em espera       │
                  └───────┬────────┘
                           │
                           v
                  ┌────────────────┐
                  │Adicionar       │
                  │serviços        │
                  └───────┬────────┘
                           │
                           v
                  ┌────────────────┐
                  │Status:         │
                  │Em andamento    │
                  └───────┬────────┘
                           │
                           v
                  ┌────────────────┐
                  │Finalizar       │
                  │atendimento     │
                  └───────┬────────┘
                           │
                           v
                  ┌────────────────┐
                  │Status:         │
                  │Concluído       │
                  └───────┬────────┘
                           │
                           v
                  ┌────────────────┐
                  │Gerar PDF       │
                  │do orçamento    │
                  └────────────────┘
```
