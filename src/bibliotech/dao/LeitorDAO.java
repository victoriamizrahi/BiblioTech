/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.dao;

import bibliotech.dados.Conexao;
import bibliotech.model.Leitor;
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
 * Acesso à tabela "leitor": cadastrar, consultar, atualizar e excluir.
 */
public class LeitorDAO {

    private Leitor ler(ResultSet rs) throws SQLException {
        Leitor l = new Leitor(rs.getInt("id_leitor"), rs.getString("nome"), rs.getString("cpf"),
                rs.getString("telefone"), rs.getString("endereco"));
        l.setEmprestimosAtivos(rs.getInt("ativos"));
        return l;
    }

    /**
     * Lista os leitores cujo nome ou CPF contém o texto buscado.
     *
     * @param termo texto da busca (vazio traz todos)
     * @return lista de leitores em ordem alfabética
     * @throws SQLException se houver problema com o banco
     */
    public List<Leitor> listar(String termo) throws SQLException {
        String sql = "SELECT l.id_leitor, l.nome, l.cpf, l.telefone, l.endereco, "
                + "(SELECT COUNT(*) FROM emprestimo e WHERE e.leitor_id_leitor = l.id_leitor "
                + "AND e.status_emprestimo = 'Ativo') AS ativos "
                + "FROM leitor l WHERE l.nome LIKE ? OR l.cpf LIKE ? ORDER BY l.nome";
        List<Leitor> lista = new ArrayList<>();
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            String padrao = "%" + termo + "%";
            ps.setString(1, padrao);
            ps.setString(2, padrao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(ler(rs));
                }
            }
        }
        return lista;
    }

    /**
     * Cadastra um leitor novo 
     *
     * @param l o leitor a cadastrar
     * @throws SQLException se houver problema ; ex.: CPF repetido)
     */
    public void inserir(Leitor l) throws SQLException {
        String sql = "INSERT INTO leitor (nome, cpf, telefone, endereco) VALUES (?, ?, ?, ?)";
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getNome());
            ps.setString(2, l.getCpf());
            ps.setString(3, l.getTelefone());
            ps.setString(4, l.getEndereco());
            ps.executeUpdate();
        }
    }

    /**
     * Atualiza os dados de um leitor que já existe.
     *
     * @param l o leitor com os dados novos (o id diz qual linha mudar)
     * @throws SQLException se houver problema com o banco
     */
    public void atualizar(Leitor l) throws SQLException {
        String sql = "UPDATE leitor SET nome = ?, cpf = ?, telefone = ?, endereco = ? WHERE id_leitor = ?";
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, l.getNome());
            ps.setString(2, l.getCpf());
            ps.setString(3, l.getTelefone());
            ps.setString(4, l.getEndereco());
            ps.setInt(5, l.getIdLeitor());
            ps.executeUpdate();
        }
    }

    /**
     * Exclui um leitor. Se ele tiver empréstimos registrados, o banco recusa
     *
     * @param idLeitor o id do leitor
     * @throws SQLException se houver problema com o banco
     */
    public void excluir(int idLeitor) throws SQLException {
        String sql = "DELETE FROM leitor WHERE id_leitor = ?";
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idLeitor);
            ps.executeUpdate();
        }
    }

    /**
     * Verifica se já existe OUTRO leitor com este CPF.
     *
     * @param cpf o CPF a conferir
     * @param ignorarId id do leitor que está sendo editado (use 0 para um leitor novo)
     * @return true se o CPF já está em uso
     * @throws SQLException se houver problema com o banco
     */
    public boolean cpfJaExiste(String cpf, int ignorarId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM leitor WHERE cpf = ? AND id_leitor <> ?";
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ps.setInt(2, ignorarId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Conta quantos leitores existem.
     *
     * @return o total de leitores
     * @throws SQLException se houver problema com o banco
     */
    public int contar() throws SQLException {
        try (Connection c = Conexao.getConexao();
                PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM leitor");
                ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
