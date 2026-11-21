/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.dao;

import bibliotech.dados.Conexao;
import bibliotech.model.Usuario;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author 55219
 */
/**
 * login do sistema .
 */
public class UsuarioDAO {

    /**
     * Transforma a senha em um código SHA-256 (64 letras/números).
     * @param senha a senha digitada
     * @return o código SHA-256 da senha
     */
    public static String hash(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 indisponível", ex);
        }
    }

    /**
     * Confere login e senha no banco.
     *
     * @param login o usuário digitado
     * @param senha a senha digitada
     * @return o Usuario, ou null se login/senha estiverem errados
     * @throws SQLException se houver problema com o banco
     */
    public Usuario autenticar(String login, String senha) throws SQLException {
       
        String sql = "SELECT login, nome, perfil FROM usuario WHERE login = ? AND senha_hash = ?";
        try (Connection c = Conexao.getConexao(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, hash(senha));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(rs.getString("login"), "", rs.getString("nome"), rs.getString("perfil"));
                }
                return null;
            }
        }
    }
}