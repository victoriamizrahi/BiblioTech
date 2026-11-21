/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.dao;

import bibliotech.dados.Conexao;
import bibliotech.model.Livro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 55219
 */
/**
 * Acesso à tabela "livro": cadastrar, consultar, atualizar e excluir.
 * Um livro está "disponível" quando NÃO existe empréstimo ativo dele.
 */
public class LivroDAO {

    // traz o livro e calcula se ele está disponível
    private static final String SELECT_BASE
            = "SELECT l.id_livro, l.titulo, l.autor, l.isbn, l.ano_publicacao, l.categoria, "
            + "NOT EXISTS (SELECT 1 FROM emprestimo e WHERE e.livro_id_livro = l.id_livro "
            + "AND e.status_emprestimo = 'Ativo') AS disponivel FROM livro l ";

    private Livro ler(ResultSet rs) throws SQLException {
        Livro l = new Livro(rs.getInt("id_livro"), rs.getString("titulo"), rs.getString("autor"),
                rs.getString("isbn"), rs.getInt("ano_publicacao"), rs.getString("categoria"));
        l.setDisponivel(rs.getBoolean("disponivel"));
        return l;
    }

    private List<Livro> consultar(String sqlCompleto, String... parametros) throws SQLException {
        List<Livro> lista = new ArrayList<>();
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sqlCompleto)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setString(i + 1, parametros[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(ler(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Lista os livros cujo título ou autor contém o texto buscado ; tela de Livros.
     *
     * @param termo texto da busca (vazio traz todos)
     * @return lista de livros em ordem alfabética
     * @throws SQLException se houver problema com o banco
     */
    public List<Livro> listar(String termo) throws SQLException {
        String padrao = "%" + termo + "%";
        return consultar(SELECT_BASE + "WHERE l.titulo LIKE ? OR l.autor LIKE ? ORDER BY l.titulo", padrao, padrao);
    }

    /**
     * Busca para a tela de Consulta de acervo.
     *
     * @param tipo 0 = título, 1 = autor, 2 = categoria
     * @param termo texto da busca (vazio traz todos)
     * @return lista de livros em ordem alfabética
     * @throws SQLException se houver problema com o banco
     */
    public List<Livro> buscarPor(int tipo, String termo) throws SQLException {
        // o nome da coluna vem de uma lista fixa (nunca do que o usuário digitou)
        String coluna = tipo == 0 ? "l.titulo" : tipo == 1 ? "l.autor" : "l.categoria";
        return consultar(SELECT_BASE + "WHERE " + coluna + " LIKE ? ORDER BY l.titulo", "%" + termo + "%");
    }

    /**
     * Lista só os livros que podem ser emprestados agora.
     *
     * @return livros disponíveis em ordem alfabética
     * @throws SQLException se houver problema com o banco
     */
    public List<Livro> listarDisponiveis() throws SQLException {
        return consultar("SELECT * FROM (" + SELECT_BASE + ") t WHERE t.disponivel = 1 ORDER BY t.titulo");
    }

    /**
     * Cadastra um livro novo 
     *
     * @param l o livro a cadastrar
     * @throws SQLException se houver problema com o banco
     */
    public void inserir(Livro l) throws SQLException {
        String sql = "INSERT INTO livro (titulo, autor, isbn, ano_publicacao, categoria) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setInt(4, l.getAnoPublicacao());
            ps.setString(5, l.getCategoria());
            ps.executeUpdate();
        }
    }

    /**
     * Atualiza os dados de um livro que já existe.
     *
     * @param l o livro com os dados novos (o id diz qual linha mudar)
     * @throws SQLException se houver problema com o banco
     */
    public void atualizar(Livro l) throws SQLException {
        String sql = "UPDATE livro SET titulo = ?, autor = ?, isbn = ?, ano_publicacao = ?, categoria = ? "
                + "WHERE id_livro = ?";
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setString(3, l.getIsbn());
            ps.setInt(4, l.getAnoPublicacao());
            ps.setString(5, l.getCategoria());
            ps.setInt(6, l.getIdLivro());
            ps.executeUpdate();
        }
    }

    /**
     * Exclui um livro. Se ele tiver empréstimos registrados, o banco recusa
     *
     * @param idLivro o id do livro
     * @throws SQLException se houver problema com o banco
     */
    public void excluir(int idLivro) throws SQLException {
        String sql = "DELETE FROM livro WHERE id_livro = ?";
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idLivro);
            ps.executeUpdate();
        }
    }

    /**
     * Conta quantos livros existem no acervo.
     *
     * @return o total de livros
     * @throws SQLException se houver problema com o banco
     */
    public int contar() throws SQLException {
        try (Connection c = Conexao.getConexao();
                PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM livro");
                ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}