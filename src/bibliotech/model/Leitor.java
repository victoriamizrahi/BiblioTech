/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.model;

/**
 *
 * @author 55219
 */
public class Leitor {
    private int idLeitor;
    private String nome;
    private String cpf;
    private String telefone;
    private String endereco;
    private int emprestimosAtivos; // quantos empréstimos em andamento 

    public Leitor(int idLeitor, String nome, String cpf, String telefone, String endereco) {
        this.idLeitor = idLeitor;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public int getIdLeitor() { return idLeitor; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public int getEmprestimosAtivos() { return emprestimosAtivos; }

    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setEmprestimosAtivos(int emprestimosAtivos) { this.emprestimosAtivos = emprestimosAtivos; }
}