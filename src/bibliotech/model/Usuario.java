/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.model;

/**
 *
 * @author 55219
 */
public class Usuario {
public static final String BIBLIOTECARIO = "Bibliotecário";
    public static final String ATENDENTE = "Atendente";

    private String login;
    private String senha;
    private String nome;
    private String perfil;

    public Usuario(String login, String senha, String nome, String perfil) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.perfil = perfil;
    }

    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public String getNome() { return nome; }
    public String getPerfil() { return perfil; }

    public boolean isBibliotecario() {
        return BIBLIOTECARIO.equals(perfil);
    }
}