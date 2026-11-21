/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import bibliotech.dao.EmprestimoDAO;
import bibliotech.model.Emprestimo;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 55219
 */
public class PainelAtrasos extends PainelBase {

    private final EmprestimoDAO dao = new EmprestimoDAO(); // conversa com o banco

    private final JLabel alerta = Estilo.faixa();
    private final DefaultTableModel modelo = Estilo.modelo("Leitor", "Telefone", "Livro", "Prevista", "Dias de atraso");
    private final JTable tabela = Estilo.tabela(modelo);

    public PainelAtrasos(TelaPrincipal principal) {
        alerta.setFont(Estilo.F_SECAO.deriveFont(18f));
        colocar(alerta, 30, 24, 990, 52);

        Estilo.larguras(tabela, 219, 180, 240, 150, 201);
        colocar(Estilo.rolagem(tabela), 30, 103, 990, 240);

        JButton btnImprimir = colocar(Estilo.botao("Imprimir relatório", Estilo.SECUNDARIO), 30, 359, 220, 50);
        btnImprimir.setMnemonic(KeyEvent.VK_P);
        btnImprimir.setToolTipText("Imprimir o relatório (Alt + P)");
        btnImprimir.addActionListener(e -> imprimir());

        JButton btnPdf = colocar(Estilo.botao("Exportar PDF", Estilo.SECUNDARIO), 266, 359, 221, 50);
        btnPdf.setMnemonic(KeyEvent.VK_O);
        btnPdf.setToolTipText("Salvar o relatório em PDF (Alt + O)");
        btnPdf.addActionListener(e -> exportarPdf());

        colocar(Estilo.dica("Disponível somente para o perfil Bibliotecário."), 30, 430, 600, 22);
    }

    private void imprimir() {
        try {
            tabela.print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat("Relatório de atrasos – " + Estilo.data(LocalDate.now())),
                    new MessageFormat("Página {0}"));
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "Não foi possível imprimir: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportarPdf() {
        JOptionPane.showMessageDialog(this,
                "Na próxima janela, escolha a impressora \"Microsoft Print to PDF\"\npara salvar o relatório como arquivo PDF.",
                "Exportar PDF", JOptionPane.INFORMATION_MESSAGE);
        imprimir();
    }

    @Override
    public void atualizar() {
        try {
            // o banco ja devolve so os atrasados, do mais atrasado para o menos
            List<Emprestimo> atrasados = dao.listarAtrasados();

            modelo.setRowCount(0);
            for (Emprestimo e : atrasados) {
                modelo.addRow(new Object[]{
                    e.getLeitor().getNome(),
                    e.getLeitor().getTelefone(),
                    e.getLivro().getTitulo(),
                    Estilo.data(e.getDataPrevistaDevolucao()),
                    Estilo.ICONE_ALERTA + e.getDiasAtraso() + (e.getDiasAtraso() == 1 ? " dia" : " dias")
                });
            }

            int n = atrasados.size();
            String hoje = Estilo.data(LocalDate.now());
            if (n == 0) {
                Estilo.exibir(alerta, "Nenhum empréstimo em atraso em " + hoje, false);
            } else {
                Estilo.exibir(alerta, n + (n == 1 ? " empréstimo em atraso em " : " empréstimos em atraso em ") + hoje, true);
            }
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }
}