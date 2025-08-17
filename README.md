
# VisualPet

> Sistema completo para gestão de pet shops: controle de atendimentos, vendas, estoque, clientes, pets, produtos, caixa, relatórios e muito mais.

## � Principais Funcionalidades

- Cadastro e gerenciamento de clientes, pets, produtos e serviços
- Controle de atendimentos, prontuários e vacinas
- Gestão de vendas, caixa, pacotes e estoque
- Relatórios gerenciais e dashboards
- Controle de usuários e permissões (Spring Security)
- Interface web responsiva (Thymeleaf)

## 🆕 Novidades na versão 3.0

- Atualização para Java 22 e Spring Boot 3.5.4
- Novos relatórios e gráficos
- Melhorias de performance e segurança
- Novos módulos e telas (produtos, caixa, relatórios)
- Código reorganizado e modularizado

## 📋 Pré-requisitos

- Java 22+
- Maven 3+
- MySQL 8+ (ou compatível)

## 🔧 Instalação e Execução

1. Clone o repositório:
	```bash
	git clone https://github.com/jonhrabit/visualpet.git
	cd visualpet
	```
2. Configure o banco de dados MySQL e ajuste o arquivo `src/main/resources/application.properties` conforme necessário.
3. Compile o projeto:
	```bash
	mvn clean package
	```
4. Execute o JAR gerado:
	```bash
	java -jar target/VisualPet-3.0-exec.jar
	```
5. Acesse a aplicação em [http://localhost:8080](http://localhost:8080)

## ⚙️ Configuração

Edite o arquivo `application.properties` para definir as credenciais do banco, porta, e demais parâmetros:

```
spring.datasource.url=jdbc:mysql://localhost:3306/veterinaria
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

## 🛠️ Tecnologias Utilizadas

- Java 22
- Spring Boot 3.5.4 (Web, Data JPA, Security, Validation, DevTools)
- Thymeleaf
- MySQL
- Lombok
- WebJars (DataTables, Moment.js)

## � Estrutura do Projeto

- `src/main/java/px/main/` — Código-fonte principal (controllers, services, models)
- `src/main/resources/templates/` — Templates HTML (Thymeleaf)
- `src/main/resources/static/` — Arquivos estáticos (CSS, JS, imagens)
- `src/main/resources/application.properties` — Configurações da aplicação

## 👤 Autor

João Felipe Xavier

---
Projeto open source. Contribuições são bem-vindas!
