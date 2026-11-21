/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import bibliotech.dao.LivroDAO;
import bibliotech.model.Livro;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author 55219
 */
public class PainelConsulta extends PainelBase {

    private final TelaPrincipal principal;
    private final LivroDAO dao = new LivroDAO(); // conversa com o banco

    private final JComboBox<String> cbTipo = Estilo.escolha();
    private final JTextField txtTermo = Estilo.campo();
    private final DefaultTableModel modelo = Estilo.modelo("Título", "Autor", "Categoria", "Ano", "Situação");
    private final JTable tabela = Estilo.tabela(modelo);
    private final JLabel lblContagem = Estilo.dica("");
    private final ArrayList<Livro> exibidos = new ArrayList<>();

    public PainelConsulta(TelaPrincipal principal) {
        this.principal = principal;

        JLabel lblTipo = colocar(Estilo.negrito("Buscar por"), 30, 30, 220, 22);
        cbTipo.addItem("Título");
        cbTipo.addItem("Autor");
        cbTipo.addItem("Categoria");
        colocar(cbTipo, 30, 58, 220, 44);
        Estilo.acessivel(lblTipo, cbTipo, "Escolha se a busca é por título, autor ou categoria");

        JLabel lblTermo = colocar(Estilo.negrito("Termo de busca"), 270, 30, 300, 22);
        colocar(txtTermo, 270, 58, 561, 44);
        Estilo.acessivel(lblTermo, txtTermo, "Digite o que você procura. Deixe vazio para ver todos os livros");
        txtTermo.addActionListener(e -> buscar()); // enter busca

        JButton btnBuscar = colocar(Estilo.botao("Buscar", Estilo.PRIMARIO), 850, 58, 170, 44);
        btnBuscar.setMnemonic(KeyEvent.VK_B);
        btnBuscar.setToolTipText("Buscar no acervo (Alt + B)");
        btnBuscar.addActionListener(e -> buscar());

        Estilo.larguras(tabela, 290, 219, 180, 110, 191);
        colocar(Estilo.rolagem(tabela), 30, 147, 990, 240);
        colocar(lblContagem, 30, 393, 600, 22);

        JButton btnEmprestar = colocar(Estilo.botao("Emprestar o livro selecionado", Estilo.PRIMARIO), 30, 425, 400, 50);
        btnEmprestar.setMnemonic(KeyEvent.VK_T);
        btnEmprestar.setToolTipText("Ir para a tela de empréstimos com este livro (Alt + T)");
        btnEmprestar.addActionListener(e -> emprestar());
    }

    // busca no banco (titulo, autor ou categoria) e mostra na tabela
    private void buscar() {
        try {
            List<Livro> lista = dao.buscarPor(cbTipo.getSelectedIndex(), txtTermo.getText().trim());
            exibidos.clear();
            modelo.setRowCount(0);
            for (Livro l : lista) {
                exibidos.add(l);
                int ano = l.getAnoPublicacao();
                modelo.addRow(new Object[]{l.getTitulo(), l.getAutor(), l.getCategoria(),
                    ano < 0 ? (-ano) + " a.C." : String.valueOf(ano), PainelLivros.situacao(l)});
            }
            int n = exibidos.size();
            lblContagem.setText(n + (n == 1 ? " livro encontrado" : " livros encontrados"));
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }

    private void emprestar() {
        int i = tabela.getSelectedRow();
        if (i < 0 || i >= exibidos.size()) {
            JOptionPane.showMessageDialog(this, "Clique em um livro da tabela para emprestar.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Livro livro = exibidos.get(i);
        if (!livro.isDisponivel()) {
            JOptionPane.showMessageDialog(this, "Este livro está emprestado no momento.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        principal.irPara("Empréstimos");
        ((PainelEmprestimos) principal.getPainel("Empréstimos")).preSelecionarLivro(livro);
    }

    @Override
    public void atualizar() {
        txtTermo.setText("");
        cbTipo.setSelectedIndex(0);
        buscar();
    }
}