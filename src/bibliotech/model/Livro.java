/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.model;

/**
 *
 * @author 55219
 */
public class Livro {
 private int idLivro;
    private String titulo;
    private String autor;
    private String isbn;
    private int anoPublicacao;
    private String categoria;
    private boolean disponivel = true; // todo livro novo começa disponível

    public Livro(int idLivro, String titulo, String autor, String isbn, int anoPublicacao, String categoria) {
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.categoria = categoria;
    }

    public int getIdLivro() { return idLivro; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public String getCategoria() { return categoria; }
    public boolean isDisponivel() { return disponivel; }

    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }
}