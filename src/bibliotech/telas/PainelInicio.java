/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import bibliotech.dados.Sessao;
import bibliotech.dao.EmprestimoDAO;
import bibliotech.dao.LeitorDAO;
import bibliotech.dao.LivroDAO;
import bibliotech.model.Emprestimo;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 55219
 */
public class PainelInicio extends PainelBase {

    private final TelaPrincipal principal;

    private final LivroDAO livroDAO = new LivroDAO();
    private final LeitorDAO leitorDAO = new LeitorDAO();
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    private final JLabel numLivros = new JLabel();
    private final JLabel numEmprestados = new JLabel();
    private final JLabel numAtrasos = new JLabel();
    private final JLabel numLeitores = new JLabel();
    private final DefaultTableModel modelo = Estilo.modelo("Leitor", "Livro", "Saída", "Prevista", "Situação");

    public PainelInicio(TelaPrincipal principal) {
        this.principal = principal;

        // 4 cartoes de resumo
        criarCartao(numLivros, "Livros no acervo", 30, false);
        criarCartao(numEmprestados, "Livros emprestados", 281, false);
        criarCartao(numAtrasos, "Empréstimos em atraso", 532, true);
        criarCartao(numLeitores, "Leitores cadastrados", 783, false);

        // açoes rápidas
        colocar(Estilo.secao("Ações rápidas"), 30, 166, 300, 28);

        JButton btnNovo = colocar(Estilo.botao("Novo empréstimo", Estilo.PRIMARIO), 30, 201, 300, 64);
        btnNovo.setMnemonic(KeyEvent.VK_N);
        btnNovo.setToolTipText("Registrar um novo empréstimo (Alt + N)");
        btnNovo.addActionListener(e -> principal.irPara("Empréstimos"));

        JButton btnDevolver = colocar(Estilo.botao("Registrar devolução", Estilo.SECUNDARIO), 348, 201, 300, 64);
        btnDevolver.setMnemonic(KeyEvent.VK_D);
        btnDevolver.setToolTipText("Dar baixa em uma devolução (Alt + D)");
        btnDevolver.addActionListener(e -> principal.irPara("Empréstimos"));

        JButton btnCadastrar = colocar(Estilo.botao("Cadastrar livro", Estilo.SECUNDARIO), 666, 201, 354, 64);
        btnCadastrar.setMnemonic(KeyEvent.VK_A);
        btnCadastrar.setToolTipText("Cadastrar um novo livro (Alt + A)");
        btnCadastrar.addActionListener(e -> principal.irPara("Livros"));
        // só o Bibliotecário vê este botão
        btnCadastrar.setVisible(Sessao.usuarioLogado.isBibliotecario());

        // ultimos emprestimos
        colocar(Estilo.secao("Últimos empréstimos"), 30, 303, 400, 28);
        JTable tabela = Estilo.tabela(modelo);
        Estilo.larguras(tabela, 238, 271, 151, 149, 181);
        colocar(Estilo.rolagem(tabela), 30, 336, 990, 194);
    }

    private void criarCartao(JLabel numero, String rotulo, int x, boolean alerta) {
        JPanel c = new JPanel(null);
        c.setBackground(Estilo.BRANCO);
        c.setBorder(BorderFactory.createLineBorder(alerta ? Estilo.VERMELHO_ESCURO : Estilo.BORDA, alerta ? 3 : 2));
        c.setToolTipText(rotulo);

        numero.setFont(new Font("SansSerif", Font.BOLD, 40));
        numero.setForeground(Estilo.TEXTO);
        numero.setBounds(16, 12, 150, 50);
        c.add(numero);

        JLabel r = Estilo.texto(rotulo);
        r.setBounds(16, 66, 205, 28);
        c.add(r);

        if (alerta) {
            JLabel icone = new JLabel("\u26A0");
            icone.setFont(Estilo.F_SECAO);
            icone.setForeground(Estilo.VERMELHO_ESCURO);
            icone.setBounds(195, 8, 30, 30);
            c.add(icone);
        }
        colocar(c, x, 24, 233, 112);
    }

    @Override
    public void atualizar() {
        try {
            // numeros dos cartoes: consultas no banco
            numLivros.setText(String.valueOf(livroDAO.contar()));
            numEmprestados.setText(String.valueOf(emprestimoDAO.contarAtivos()));
            numAtrasos.setText(String.valueOf(emprestimoDAO.contarAtrasados()));
            numLeitores.setText(String.valueOf(leitorDAO.contar()));

            // 3 emprestimos mais recentes
            List<Emprestimo> recentes = emprestimoDAO.listarRecentes(3);
            modelo.setRowCount(0);
            for (Emprestimo e : recentes) {
                modelo.addRow(new Object[]{
                    e.getLeitor().getNome(),
                    e.getLivro().getTitulo(),
                    Estilo.data(e.getDataSaida()),
                    Estilo.data(e.getDataPrevistaDevolucao()),
                    textoSituacao(e)
                });
            }
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }

    // icon + texto
    static String textoSituacao(Emprestimo e) {
        switch (e.getSituacao()) {
            case "Atrasado": return Estilo.ICONE_ALERTA + "Atrasado";
            case "Devolvido": return Estilo.ICONE_OK + "Devolvido";
            default: return Estilo.ICONE_EMPRESTADO + "Ativo";
        }
    }
}