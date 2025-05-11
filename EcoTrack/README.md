# EcoTrack - Sistema de Gestão de Resíduos e Reciclagem

O EcoTrack é um sistema desenvolvido para facilitar a gestão de resíduos e reciclagem, com foco em ESG (Environmental, Social, and Governance). A plataforma permite rastreamento da coleta seletiva, notificações para usuários sobre descarte correto e alertas automáticos para coleta quando os níveis de materiais atingem limites críticos.

## Principais Funcionalidades

### 1. Rastreamento da Coleta Seletiva
- Registro de pontos de coleta
- Cadastro de materiais recicláveis com dicas de descarte
- Registro e validação de descartes de resíduos
- Monitoramento de níveis de materiais em cada ponto de coleta

### 2. Alertas Automáticos
- Geração de alertas quando o nível de materiais atinge limites críticos
- Notificação para coletores sobre pontos que precisam de coleta
- Agendamento de coletas
- Monitoramento de alertas ativos

### 3. Notificações de Descarte Correto
- Envio de dicas para usuários sobre como descartar corretamente
- Notificações de confirmação de descarte
- Informações sobre pontos de coleta próximos

## Tecnologias Utilizadas

- **Backend**: Java com Spring Boot 3.1
  - Spring Web para API RESTful
  - Spring Data JPA para persistência
  - Spring Security com JWT para autenticação e autorização
  - Flyway para migrações de banco de dados
  - SpringDoc OpenAPI para documentação da API

- **Banco de Dados**: Oracle Database
  - Tabelas relacionais para armazenar dados do sistema
  - Migrações automatizadas com Flyway

- **Conteinerização**: Docker e Docker Compose
  - Contêineres isolados para a aplicação e banco de dados
  - Configuração pronta para desenvolvimento e produção

## Endpoints da API

O sistema expõe diversos endpoints RESTful que permitem acesso às funcionalidades:

### Autenticação
- `POST /api/auth/login`: Autentica um usuário e retorna um token JWT
- `POST /api/auth/registro/cidadao`: Registra um novo usuário cidadão
- `POST /api/auth/registro/empresa`: Registra um novo usuário empresa

### Pontos de Coleta
- `GET /api/pontos-coleta`: Lista todos os pontos de coleta
- `GET /api/pontos-coleta/{id}`: Busca ponto de coleta por ID
- `GET /api/pontos-coleta/material/{materialId}`: Busca pontos que aceitam determinado material
- `GET /api/pontos-coleta/cidade/{cidade}`: Busca pontos em uma cidade
- `GET /api/pontos-coleta/proximos`: Busca pontos próximos a coordenadas geográficas
- `POST /api/pontos-coleta`: Cria novo ponto de coleta
- `PUT /api/pontos-coleta/{id}`: Atualiza um ponto de coleta
- `DELETE /api/pontos-coleta/{id}`: Remove um ponto de coleta (marca como inativo)
- `POST /api/pontos-coleta/{pontoId}/materiais`: Adiciona material ao ponto de coleta
- `PUT /api/pontos-coleta/{pontoId}/materiais/{materialId}/nivel`: Atualiza nível do material

### Materiais
- `GET /api/materiais`: Lista todos os materiais
- `GET /api/materiais/{id}`: Busca material por ID
- `GET /api/materiais/ponto-coleta/{pontoColetaId}`: Busca materiais aceitos em um ponto
- `POST /api/materiais`: Cria novo material
- `PUT /api/materiais/{id}`: Atualiza um material
- `DELETE /api/materiais/{id}`: Remove um material

### Registros de Descarte
- `GET /api/registros-descarte`: Lista todos os registros de descarte
- `GET /api/registros-descarte/{id}`: Busca registro por ID
- `GET /api/registros-descarte/usuario/{usuarioId}`: Busca registros de um usuário
- `GET /api/registros-descarte/ponto-coleta/{pontoColetaId}`: Busca registros de um ponto
- `GET /api/registros-descarte/material/{materialId}`: Busca registros de um material
- `POST /api/registros-descarte`: Registra um novo descarte
- `PUT /api/registros-descarte/{id}/validar`: Valida um registro de descarte

### Alertas
- `GET /api/alertas`: Lista todos os alertas
- `GET /api/alertas/{id}`: Busca alerta por ID
- `GET /api/alertas/ponto-coleta/{pontoColetaId}`: Busca alertas de um ponto
- `GET /api/alertas/ativos`: Busca alertas não resolvidos
- `GET /api/alertas/nao-lidos`: Busca alertas não lidos
- `PUT /api/alertas/{id}/ler`: Marca alerta como lido
- `PUT /api/alertas/{id}/resolver`: Marca alerta como resolvido

### Notificações
- `GET /api/notificacoes/usuario/{usuarioId}`: Lista notificações de um usuário
- `GET /api/notificacoes/usuario/{usuarioId}/nao-lidas`: Lista notificações não lidas
- `GET /api/notificacoes/{id}`: Busca notificação por ID
- `PUT /api/notificacoes/{id}/ler`: Marca notificação como lida
- `POST /api/notificacoes/dica-descarte`: Envia dica de descarte para usuário
- `POST /api/notificacoes/lote`: Envia notificações em lote para vários usuários

## Como Executar

### Pré-requisitos
- Docker e Docker Compose instalados
- Java 17+ (apenas para desenvolvimento local)
- Maven (apenas para desenvolvimento local)

### Executando com Docker

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/ecotrack.git
cd ecotrack
```

2. Execute o sistema com Docker Compose:
```bash
docker-compose up -d
```

3. O sistema estará disponível em:
- API: http://localhost:8080/api
- Documentação Swagger: http://localhost:8080/api/swagger-ui.html
- Oracle Database: localhost:1521 (SID: XE)

### Executando Localmente para Desenvolvimento

1. Configure um banco Oracle e atualize as configurações em `application.properties`

2. Execute a aplicação:
```bash
mvn spring-boot:run
```

## Usuários Padrão para Testes

O sistema já vem com alguns usuários cadastrados para facilitar os testes:

| Tipo      | Email                  | Senha  |
|-----------|------------------------|--------|
| Admin     | admin@ecotrack.com     | 123456 |
| Coletor   | coletor@ecotrack.com   | 123456 |
| Cidadão   | cidadao@ecotrack.com   | 123456 |
| Empresa   | empresa@ecotrack.com   | 123456 |

## Estrutura do Projeto

```
EcoTrack/
│
├── src/main/
│   ├── java/br/com/fiap/ecotrack/
│   │   ├── config/                  # Configurações do Spring Boot
│   │   ├── controller/              # Controladores REST
│   │   ├── dto/                     # DTOs para transferência de dados
│   │   ├── exception/               # Classes de exceção personalizadas
│   │   ├── model/                   # Entidades JPA
│   │   ├── repository/              # Interfaces do Spring Data JPA
│   │   ├── security/                # Classes de segurança e JWT
│   │   ├── service/                 # Serviços de negócio
│   │   └── EcoTrackApplication.java # Classe principal
│   │
│   └── resources/
│       ├── db/migration/            # Scripts SQL para migrações Flyway
│       └── application.properties   # Configurações da aplicação
│
├── Dockerfile                       # Configuração para build do Docker
├── docker-compose.yml               # Configuração do Docker Compose
└── pom.xml                          # Configuração do Maven
```

## Próximos Passos

- Implementação de dashboard com estatísticas
- Aplicativo móvel para facilitar registro de descartes
- Integração com sistemas de gamificação para incentivar reciclagem
- Implementação de QR Code para identificação de pontos de coleta
- Sistema de recompensas para usuários que reciclam regularmente
