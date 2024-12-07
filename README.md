# Teste Técnico Nava - Java Full Stack

## Descrição

Este projeto foi desenvolvido como parte de um teste técnico para a empresa **Nava**. Ele consiste em um sistema de **agendamento de transferências financeiras**, permitindo que os usuários realizem as seguintes funcionalidades:

1. **Agendar transferências financeiras** com os seguintes dados:
   - **Conta de origem** (padrão: XXXXXXXXXX)
   - **Conta de destino** (padrão: XXXXXXXXXX)
   - **Valor da transferência**
   - **Taxa** (calculada dinamicamente conforme as regras do teste)
   - **Data de transferência**
   - **Data de agendamento** (definida como a data atual)

2. **Visualizar todas as transferências agendadas**, incluindo os detalhes das taxas e status.
3. **Cancelar transferências** (somente para transferências futuras).
4. **Calcular a taxa** com base na data da transferência:
   - **Regras de cálculo**:
     | Dias Entre Agendamento e Transferência | Taxa (Fixa) | Taxa (% sobre o valor) |
     |---------------------------------------|-------------|------------------------|
     | 0                                     | R$ 3,00     | 2,5%                  |
     | 1–10                                  | R$ 12,00    | 0%                    |
     | 11–20                                 | R$ 0,00     | 8,2%                  |
     | 21–30                                 | R$ 0,00     | 6,9%                  |
     | 31–40                                 | R$ 0,00     | 4,7%                  |
     | 41–50                                 | R$ 0,00     | 1,7%                  |

---

## Decisões Arquiteturais

### Backend
- **Framework**: Spring Boot 3.4.0
  - Escolhido pela simplicidade em construir APIs REST robustas e escaláveis.
- **Banco de Dados**: H2 (banco em memória)
  - Utilizado para simplificar o desenvolvimento e garantir rapidez na prototipagem.
- **Segurança**:
  - Implementação de autenticação e autorização com **JWT**.
  - Configuração de **CORS** para permitir comunicação com o frontend.
- **Validação**:
  - Validação dos dados de entrada e regras de negócio para calcular taxas corretamente e restringir transferências inválidas.
- **Estrutura**:
  - Arquitetura separada em camadas: controladores, serviços e repositórios.

### Frontend
- **Framework**: Angular 16.2.0
  - Escolhido por sua modularidade e arquitetura baseada em componentes.
- **Estilo**: Bootstrap 5.3.3 com SCSS personalizado
  - Garante responsividade e uma interface limpa e amigável.
- **Autenticação**:
  - Gerenciamento de sessão com JWT armazenado em `localStorage`.
- **Funcionalidades**:
  - Criação de componentes para **cadastro**, **login** e **dashboard**.
  - Interações com a API através do módulo `HttpClient`.

---

## Requisitos para Execução

### Tecnologias Utilizadas
- **Backend**:
  - **Java**: 17
  - **Spring Boot**: 3.4.0
  - **Banco de Dados**: H2 (in-memory)
- **Frontend**:
  - **Angular**: 16.2.0
  - **Bootstrap**: 5.3.3

### Pré-requisitos
- **Java 17** instalado
- **Node.js** (versão 16 ou superior) instalado
- **Angular CLI** instalado globalmente:
  ```bash
  npm install -g @angular/cli


## Instruções de Configuração

### Backend
1. Navegue até o diretório do backend:
   ```bash
   cd nava-transfer-api
   ```
2. Compile o projeto:
   ```bash
   ./mvnw clean package
   ```
3. Inicie a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
4. O backend estará disponível em `http://localhost:8080`.

### Frontend
1. Navegue até o diretório do frontend:
   ```bash
   cd nava-transfer-ui
   ```
2. Instale as dependências:
   ```bash
   npm install
   ```
3. Inicie o servidor de desenvolvimento:
   ```bash
   ng serve
   ```
4. O frontend estará disponível em `http://localhost:4200`.

---

## Endpoints da API

| Método  | Endpoint                    | Descrição                                          |
|---------|-----------------------------|--------------------------------------------------|
| POST    | `/signup`                   | Cadastra um novo usuário                          |
| POST    | `/login`                    | Realiza login e retorna um token JWT              |
| POST    | `/api/transfers`            | Cria uma nova transferência                       |
| GET     | `/api/transfers`            | Retorna todas as transferências                   |
| GET     | `/api/transfers/{id}`       | Retorna uma transferência pelo ID                 |
| PATCH   | `/api/transfers/{id}`       | Cancela uma transferência futura                  |

---

## Observações

### Autenticação:
- Todas as rotas em `/api/transfers` requerem autenticação.



### Regras de Negócio:
- Transferências inválidas (ex.: sem taxa aplicável) retornarão erros de validação.
- Somente transferências com status **PENDING** podem ser canceladas.

---

## Histórico de Commits
O repositório foi desenvolvido com commits frequentes para facilitar o rastreamento do progresso e das decisões tomadas. Acesse o repositório para mais detalhes.
