/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 *
 * @author 55219
 */
public class Emprestimo {
    private int idEmprestimo;
    private LocalDate dataSaida;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucao;
    private String status;
    private Leitor leitor;
    private Livro livro;

    public Emprestimo(int idEmprestimo, Leitor leitor, Livro livro, LocalDate dataSaida,
            LocalDate dataPrevistaDevolucao, LocalDate dataDevolucao, String status) {
        this.idEmprestimo = idEmprestimo;
        this.leitor = leitor;
        this.livro = livro;
        this.dataSaida = dataSaida;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataDevolucao = dataDevolucao;
        this.status = status;
    }

    public boolean isAtivo() {
        return "Ativo".equals(status);
    }

    // atrasado = ainda não devolvido e a data prevista já passou
    public boolean isAtrasado() {
        return isAtivo() && dataPrevistaDevolucao.isBefore(LocalDate.now());
    }

    public long getDiasAtraso() {
        if (!isAtrasado()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dataPrevistaDevolucao, LocalDate.now());
    }

    //  coluna situação: Ativo, Devolvido ou Atrasado
    public String getSituacao() {
        if (isAtrasado()) {
            return "Atrasado";
        }
        return status;
    }

    public int getIdEmprestimo() { return idEmprestimo; }
    public LocalDate getDataSaida() { return dataSaida; }
    public LocalDate getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public LocalDate getDataDevolucao() { return dataDevolucao; }
    public String getStatus() { return status; }
    public Leitor getLeitor() { return leitor; }
    public Livro getLivro() { return livro; }
}