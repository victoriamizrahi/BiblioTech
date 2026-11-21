/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.dados;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author 55219
 */
public class Conexao {

    // endereço do banco: localhost, porta (3306) e banco 
    private static final String URL = "jdbc:mysql://localhost:3306/bibliotech"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo&characterEncoding=UTF-8";

    // usuário e senha do MySQL 
    private static final String USUARIO = "xxxxxxx";
    private static final String SENHA = "COLOQUE_SUA_SENHA_AQUI";

    /**
     * Abre uma conexão nova com o banco.
     *
     * @return a conexão aberta 
     * @throws SQLException se o MySQL estiver desligado, a senha estiver errada etc.
     */
    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
