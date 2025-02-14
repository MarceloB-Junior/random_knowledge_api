# Random Knowledge API

## Descrição

A Random Knowledge API é uma aplicação RESTful que permite gerenciar e distribuir curiosidades e categorias de forma eficiente. Oferecendo funcionalidades CRUD completas.

Um dos principais recursos da API é a capacidade de fornecer curiosidades aleatórias, permitindo que os usuários descubram informações novas e interessantes de forma totalmente aleatória.

A autenticação é baseada em JWT, garantindo a segurança dos endpoints. A documentação completa da API está disponível via Swagger, facilitando a integração.

## Tabela de Conteúdos

-  [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Como Começar](#como-começar)
  - [Pré-requisitos](#pré-requisitos)
  - [Instalação](#instalação)
  - [Executando a Aplicação](#executando-a-aplicação)
-  [Endpoints da API](#endpoints-da-api)
-  [Autenticação](#autenticação)
-  [Estrutura do Projeto](#estrutura-do-projeto)

## Tecnologias Utilizadas
-   **Java 23:** Linguagem de programação robusta e amplamente utilizada, garante a portabilidade e escalabilidade da API.
-   **Spring Boot 3.3.7:** Framework que simplifica a configuração e o desenvolvimento da API, oferecendo recursos como injeção de dependência e auto configuração.
-   **Spring Security:** Garante a segurança da API, oferecendo autenticação e autorização robustas para proteger os endpoints e dados.
-   **Spring Data JPA:** Facilita o acesso e a manipulação de dados no banco de dados, reduzindo o código boilerplate necessário.
-   **MySQL:** Banco de dados relacional popular e confiável para armazenar dados da API.
-   **Maven:** Ferramenta de gerenciamento de dependências e construção que automatiza o processo de compilação, teste e empacotamento da API.
-   **Lombok:** Biblioteca que reduz a quantidade de código boilerplate, como getters e setters, tornando o código mais limpo e legível.
-   **Java JWT:** Biblioteca para criar e verificar tokens JWT, garantindo a autenticação segura dos usuários.
-  **SpringDoc OpenAPI/Swagger:** Ferramenta para gerar a documentação da API de forma automatizada, facilitando o uso e a integração por outros desenvolvedores. Permite a visualização e interação com os endpoints da API.

## Como Começar

### Pré-requisitos

Antes de começar, certifique-se de que você atendeu aos seguintes requisitos:

- Java Development Kit (JDK) 23 ou superior instalado em sua máquina.
- Maven instalado em sua máquina.
- Servidor MySQL em execução e acessível.

### Instalação

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/MarceloB-Junior/random_knowledge_api.git
   cd random_knowledge_api
   ```

2. **Configuração do Banco de Dados**
-  Crie um novo banco de dados no MySQL (por exemplo, `random_knowledge_db`).
- Configure as credenciais do banco de dados no arquivo `application.properties`:

```properties
    spring.application.name=random_knowledge

    spring.datasource.url=jdbc:mysql://localhost:3306/random_knowledge_db
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update

    spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

    api.jwt.token.secret=4a1a0c1650bb8c89be56dcad5fe52349
    api.jwt.token.iss=random_knowledge_api
    api.jwt.token.expiration=1
    api.jwt.refresh.token.expiration=8
```
3. **Instale as dependências**:
   ```bash
   mvn install
   ```

### Executando a Aplicação

Para executar a aplicação, use o seguinte comando:

```bash
mvn spring-boot:run
```

A aplicação será iniciada em `http://localhost:8080`.


## Endpoints da API

**POST**

*   **/users/sign-up**: Registra um novo usuário.
*   **/auth/login**: Autentica um usuário e retorna tokens JWT.
*   **/auth/refresh-token**: Obtém um novo token JWT usando um refresh token.
*   **/categories**: Cria uma nova categoria (requer role ADMIN).
*   **/curiosities**: Cria uma nova curiosidade para uma categoria (requer role ADMIN).

**GET**

*   **/categories**: Retorna todas as categorias com paginação (requer role USER).
*   **/categories/{id}**: Retorna uma categoria específica por ID (requer role USER).
*   **/categories/{id}/curiosities**: Retorna todas as curiosidades associadas a uma categoria específica.
*   **/curiosities**: Retorna todas as curiosidades com paginação (requer role USER).
*   **/curiosities/random**: Retorna uma curiosidade aleatória.
*   **/curiosities/{id}**: Retorna uma curiosidade específica por ID (requer role USER).

**PUT**

*   **/categories/{id}**: Atualiza uma categoria existente (requer role ADMIN).
*   **/curiosities/{id}**: Atualiza uma curiosidade existente (requer role ADMIN).

**DELETE**

*   **/categories/{id}**: Exclui uma categoria (requer role ADMIN).
*   **/curiosities/{id}**: Exclui uma curiosidade (requer role ADMIN).

Todos os endpoints estão documentados e podem ser visualizados através do Swagger. Para acessar a documentação interativa, inicie a aplicação e navegue até: `http://localhost:8080/swagger-ui/index.html`

## Autenticação

A API utiliza autenticação baseada em JWT. Os usuários podem se registrar e obter tokens de acesso através dos endpoints disponíveis.

Um usuário administrador padrão de nome: `John Doe`, email: `john.doe@example.com` e senha: `pwd123` é configurado automaticamente na inicialização da API, caso ainda não exista nenhum usuário com esse email no banco de dados. Este usuário possui a role `ADMIN`, permitindo realizar operações de gerenciamento de categorias e curiosidades.

## Estrutura do Projeto

```
random_knowledge/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── api/
│   │   │           └── random_knowledge/
│   │   │               ├── configs/
│   │   │               │   └── security/
│   │   │               ├── controllers/
│   │   │               ├── dtos/
│   │   │               ├── enums/
│   │   │               ├── exceptions/          
│   │   │               ├── models/
│   │   │               ├── repositories/
│   │   │               └── services/
│   │   │                   └── impl/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

