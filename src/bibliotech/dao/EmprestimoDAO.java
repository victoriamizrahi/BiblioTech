/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.dao;

import bibliotech.dados.Conexao;
import bibliotech.model.Emprestimo;
import bibliotech.model.Leitor;
import bibliotech.model.Livro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 55219
 */
/**
 * Acesso à tabela "emprestimo": registrar, devolver e consultar.
 */
public class EmprestimoDAO {

    // junta empréstimo + leitor + livro em uma só consulta (JOIN)
    private static final String SELECT_BASE
            = "SELECT e.id_emprestimo, e.data_saida, e.data_prevista_devolucao, e.data_devolucao, "
            + "e.status_emprestimo, l.id_leitor, l.nome, l.cpf, l.telefone, l.endereco, "
            + "b.id_livro, b.titulo, b.autor, b.isbn, b.ano_publicacao, b.categoria "
            + "FROM emprestimo e "
            + "JOIN leitor l ON l.id_leitor = e.leitor_id_leitor "
            + "JOIN livro b ON b.id_livro = e.livro_id_livro ";

    private Emprestimo ler(ResultSet rs) throws SQLException {
        Leitor leitor = new Leitor(rs.getInt("id_leitor"), rs.getString("nome"), rs.getString("cpf"),
                rs.getString("telefone"), rs.getString("endereco"));
        Livro livro = new Livro(rs.getInt("id_livro"), rs.getString("titulo"), rs.getString("autor"),
                rs.getString("isbn"), rs.getInt("ano_publicacao"), rs.getString("categoria"));
        String status = rs.getString("status_emprestimo");
        livro.setDisponivel(!"Ativo".equals(status));
        return new Emprestimo(rs.getInt("id_emprestimo"), leitor, livro,
                rs.getObject("data_saida", LocalDate.class),
                rs.getObject("data_prevista_devolucao", LocalDate.class),
                rs.getObject("data_devolucao", LocalDate.class),
                status);
    }

    private List<Emprestimo> consultar(String sqlCompleto, Object... parametros) throws SQLException {
        List<Emprestimo> lista = new ArrayList<>();
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sqlCompleto)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
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
     * Lista os empréstimos que ainda não foram devolvidos (mais recentes primeiro).
     *
     * @return empréstimos ativos
     * @throws SQLException se houver problema com o banco
     */
    public List<Emprestimo> listarAtivos() throws SQLException {
        return consultar(SELECT_BASE + "WHERE e.status_emprestimo = 'Ativo' "
                + "ORDER BY e.data_saida DESC, e.id_emprestimo DESC");
    }

    /**
     * Lista os empréstimos mais recentes (ativos ou devolvidos), para a tela Início.
     *
     * @param limite quantos empréstimos trazer
     * @return os últimos empréstimos
     * @throws SQLException se houver problema com o banco
     */
    public List<Emprestimo> listarRecentes(int limite) throws SQLException {
        return consultar(SELECT_BASE + "ORDER BY e.data_saida DESC, e.id_emprestimo DESC LIMIT ?", limite);
    }

    /**
     * Lista os empréstimos ativos cuja data prevista já passou 
     *
     * @return empréstimos atrasados
     * @throws SQLException se houver problema com o banco
     */
    public List<Emprestimo> listarAtrasados() throws SQLException {
        return consultar(SELECT_BASE + "WHERE e.status_emprestimo = 'Ativo' AND e.data_prevista_devolucao < ? "
                + "ORDER BY e.data_prevista_devolucao ASC", LocalDate.now());
    }

    /**
     * Registra um empréstimo novo, com saída hoje e devolução prevista em 14 dias.
     * @param idLeitor id do leitor que vai retirar o livro
     * @param idLivro id do livro
     * @return true se registrou; false se o livro já estava emprestado
     * @throws SQLException se houver problema com o banco
     */
    public boolean registrar(int idLeitor, int idLivro) throws SQLException {
        String sql = "INSERT INTO emprestimo (data_saida, data_prevista_devolucao, status_emprestimo, "
                + "leitor_id_leitor, livro_id_livro) "
                + "SELECT ?, ?, 'Ativo', ?, ? FROM DUAL "
                + "WHERE NOT EXISTS (SELECT 1 FROM emprestimo WHERE livro_id_livro = ? AND status_emprestimo = 'Ativo')";
        LocalDate hoje = LocalDate.now();
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, hoje);
            ps.setObject(2, hoje.plusDays(14));
            ps.setInt(3, idLeitor);
            ps.setInt(4, idLivro);
            ps.setInt(5, idLivro);
            return ps.executeUpdate() == 1; // 1 = inseriu, 0 = livro já emprestado
        }
    }

    /**
     * Dá baixa em um empréstimo (RF005): marca como Devolvido e grava a data de hoje.
     *
     * @param idEmprestimo id do empréstimo
     * @return true se deu baixa; false se o empréstimo já estava devolvido
     * @throws SQLException se houver problema com o banco
     */
    public boolean devolver(int idEmprestimo) throws SQLException {
        String sql = "UPDATE emprestimo SET status_emprestimo = 'Devolvido', data_devolucao = ? "
                + "WHERE id_emprestimo = ? AND status_emprestimo = 'Ativo'";
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, LocalDate.now());
            ps.setInt(2, idEmprestimo);
            return ps.executeUpdate() == 1;
        }
    }

    private int contar(String sql, Object... parametros) throws SQLException {
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    /**
     * Conta os empréstimos ativos (= livros emprestados agora).
     *
     * @return total de empréstimos ativos
     * @throws SQLException se houver problema com o banco
     */
    public int contarAtivos() throws SQLException {
        return contar("SELECT COUNT(*) FROM emprestimo WHERE status_emprestimo = 'Ativo'");
    }

    /**
     * Conta os empréstimos ativos que já passaram da data prevista.
     *
     * @return total de empréstimos atrasados
     * @throws SQLException se houver problema com o banco
     */
    public int contarAtrasados() throws SQLException {
        return contar("SELECT COUNT(*) FROM emprestimo WHERE status_emprestimo = 'Ativo' "
                + "AND data_prevista_devolucao < ?", LocalDate.now());
    }
}