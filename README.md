# 💳 TalitaCode Digital Wallet

Projeto de **carteira digital** desenvolvido em **Java 21** com **Spring Boot 3**, utilizando **CQRS** e **Redis** para cache.  
Implementa autenticação com **JWT**, operações de **depósito**, **pagamento** e consulta de **saldo** com histórico de transações.

---

## ⚙️ Tecnologias

- Java 21
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA (PostgreSQL)
- Spring Data Redis
- Lombok
- Docker (Postgres + Redis)

---

## 🚀 Executando o projeto

### Pré-requisitos
- Docker + Docker Compose
- Java 21
- Maven

#### Subindo os serviços
```bash
docker-compose up -d
```

#### Rodando a aplicação
```bash
mvn spring-boot:run
```

#### Autenticação
```bash
POST /api/auth/register   # Registrar usuário
POST /api/auth/login      # Autenticar e gerar token JWT
```
#### Operações
```bash
POST /api/accounts/{accountId}/transactions/deposit – Realizar depósito
POST /api/accounts/{accountId}/transactions/pay – Realizar pagamento
GET /api/accounts/{accountId}/summary – Consultar saldo e histórico
```

#### Testes
Para executar os testes unitários:

```bash
mvn test
```