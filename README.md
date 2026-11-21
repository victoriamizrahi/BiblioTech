# BiblioTech
Sistema desktop de gerenciamento de bibliotecas.

## Tecnologias
- Java (JDK) com interface gráfica 
- NetBeans 
- MySQL Workbench
- JDBC(MySQL Connector/J) com o padrão de projeto DAO
- Figma (protótipos das telas)
- Git e GitHub 


Logins de teste: `helena` / `123456` (Bibliotecário) e `pedro` / `123456` (Atendente).



## Organização do código
- `model`: classes do sistema (Livro, Leitor, Emprestimo, Usuario)

- `dao`: acesso ao banco de dados (um DAO para cada tabela)

- `dados`: conexão com o MySQL e sessão do usuário logado

- `telas`: interface gráfica (login, janela principal e telas internas)

- `sql`: script de criação do banco de dados

