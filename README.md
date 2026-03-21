# Oficina Menezes

Sistema de gerenciamento para oficina mecânica com controle de entrada de veículos, clientes, status de atendimento e fluxo operacional da oficina.

O objetivo do sistema é permitir o acompanhamento dos veículos em atendimento, organizar os dados dos clientes e automatizar o processo de entrada, reduzindo retrabalho e melhorando o controle da operação.

---

## Tecnologias

### Backend
- **Java 25**
- **Spring Boot**
- **PostgreSQL**

### Frontend
- **Angular**
- **Tailwind CSS**

### Banco de dados
- **PostgreSQL**

---

## Objetivo do projeto

O sistema da **Oficina Menezes** foi pensado para facilitar a operação diária da oficina, principalmente no controle de:

- entrada de veículos
- cadastro de clientes
- cadastro de veículos
- acompanhamento dos veículos na oficina
- status do atendimento
- pesquisa por placa
- filtragem por status

---

## Funcionalidades iniciais previstas

Com base nas telas atuais, o sistema terá inicialmente as seguintes funcionalidades:

- listar veículos registrados
- buscar veículo pela placa
- filtrar veículos por status
- registrar entrada de veículo
- cadastrar cliente
- cadastrar veículo
- associar cliente ao veículo
- controlar status do veículo dentro da oficina

---

## Fluxo principal do sistema

### Registro de entrada do veículo

A principal regra de negócio do sistema é:

> Sempre que um veículo realizar entrada na oficina, o endpoint de **registrar entrada** deve verificar se o **cliente** e o **veículo** já existem.  
> Caso não existam, o sistema deve criá-los automaticamente antes de concluir a entrada.

### Fluxo esperado

1. Usuário abre a tela de **Registrar Veículo**
2. Preenche os dados do cliente
3. Preenche os dados do veículo
4. O sistema envia tudo para o backend
5. O backend:
   - verifica se o cliente já existe
   - verifica se o veículo já existe
   - cria cliente se necessário
   - cria veículo se necessário
   - registra a entrada do veículo
6. O veículo passa a aparecer na listagem principal

---

## Endpoints necessários com base nas telas

Abaixo estão os endpoints mínimos inferidos apenas pelas interfaces apresentadas.

---

# Clientes

## Buscar cliente por nome ou CPF
Usado no campo de busca de cliente existente na etapa 1.

```http
GET /api/clientes?search={valor}
````

### Exemplos

```http
GET /api/clientes?search=Jonathan
GET /api/clientes?search=12345678900
```

# Endpoints mínimos realmente necessários para o MVP

Se o foco inicial for somente o que já aparece nas telas, estes são os mais importantes:

```http
GET    /api/veiculos
GET    /api/clientes?search={valor}
GET    /api/veiculos/placa/{placa}
POST   /api/entradas
PATCH  /api/entradas/{id}/status
```

Esses endpoints já cobrem:

* listagem
* busca
* filtro
* cadastro por entrada
* atualização de status
* visão resumida da oficina