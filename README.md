# BiblioTech
Sistema desktop de gerenciamento de bibliotecas. Em desenvolvimento.

## Objetivo do software

O BiblioTech serve para pequenas e médias bibliotecas (escola, bairro ou acervo pessoal) substituírem cadernos e planilhas por um controle centralizado do acervo, dos leitores e dos empréstimos. Ele resolve a perda de livros e a falta de organização, mostrando quem está com cada livro e quando ele deve ser devolvido.

## Time de desenvolvedores

- Victória dos Santos Mizrahi

## Tecnologias
- Java (JDK) com interface gráfica 
- NetBeans 
- MySQL Workbench
- JDBC(MySQL Connector/J) com o padrão de projeto DAO
- Figma (protótipos das telas)
- Git e GitHub 

Logins de teste: `helena` / `123456` (Bibliotecário) e `pedro` / `123456` (Atendente).

## Funcionalidades do sistema (requisitos)

- **RF001 – Cadastro de livros:** inserir, editar e excluir livros do acervo.
- **RF002 – Cadastro de leitores:** registrar os dados dos leitores que retiram livros.
- **RF003 – Registro de empréstimo:** vincular um livro a um leitor, com a data prevista de devolução calculada automaticamente (14 dias).
- **RF004 – Consulta de acervo:** pesquisar livros por título, autor ou categoria.
- **RF005 – Baixa de empréstimo:** marcar um empréstimo como devolvido, liberando o livro para o próximo leitor.
- **RF006 – Relatório de atrasos:** listar os empréstimos vencidos e ainda não devolvidos (somente Bibliotecário).
- **Login com dois perfis:** Bibliotecário (acesso total) e Atendente (consulta, empréstimos e devoluções).
- **Confiabilidade (RNF004):** um livro que já está emprestado não pode ser emprestado novamente.

## Organização do código
- `model`: classes do sistema (Livro, Leitor, Emprestimo, Usuario)

- `dao`: acesso ao banco de dados (um DAO para cada tabela)

- `dados`: conexão com o MySQL e sessão do usuário logado

- `telas`: interface gráfica (login, janela principal e telas internas)

- `sql`: script de criação do banco de dados

