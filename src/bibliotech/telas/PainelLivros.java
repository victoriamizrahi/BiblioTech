/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import bibliotech.dao.LivroDAO;
import bibliotech.model.Livro;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author 55219
 */
public class PainelLivros extends PainelBase {

    private final LivroDAO dao = new LivroDAO(); // banco

    private final JTextField txtTitulo = Estilo.campo();
    private final JTextField txtAutor = Estilo.campo();
    private final JTextField txtIsbn = Estilo.campo();
    private final JTextField txtAno = Estilo.campo();
    private final JComboBox<String> cbCategoria = Estilo.escolha();

    private final JTextField txtBusca = Estilo.campo();
    private final DefaultTableModel modelo = Estilo.modelo("Título", "Autor", "Categoria", "Situação");
    private final JTable tabela = Estilo.tabela(modelo);
    private final JLabel lblContagem = Estilo.dica("");

    private final ArrayList<Livro> exibidos = new ArrayList<>(); // livros que aparecem na tabela
    private Livro selecionado = null;                            // livro - edição 

    public PainelLivros(TelaPrincipal principal) {
        // ---------- form ----------
        colocar(Estilo.secao("Dados do livro"), 30, 24, 320, 28);
        linha("Título", txtTitulo, 60, "Digite o título do livro");
        linha("Autor", txtAutor, 135, "Digite o nome do autor");
        linha("ISBN", txtIsbn, 210, "Somente números e hífens. Exemplo: 978-0000000001");
        linha("Ano de publicação", txtAno, 285, "Digite o ano com números. Exemplo: 1954");

        JLabel lblCategoria = colocar(Estilo.negrito("Categoria"), 30, 360, 320, 22);
        String[] categorias = {"Literatura Brasileira", "Fantasia", "Ficção Científica", "Estratégia",
            "Tecnologia", "Romance", "História", "Outra"};
        for (String c : categorias) {
            cbCategoria.addItem(c);
        }
        cbCategoria.setEditable(true); // deixa digitar uma categoria nova
        colocar(cbCategoria, 30, 386, 320, 44);
        Estilo.acessivel(lblCategoria, cbCategoria, "Escolha uma categoria ou digite uma nova");

        JButton btnSalvar = colocar(Estilo.botao("Salvar", Estilo.PRIMARIO), 30, 465, 153, 50);
        btnSalvar.setMnemonic(KeyEvent.VK_A);
        btnSalvar.setToolTipText("Salvar o livro (Alt + A)");
        btnSalvar.addActionListener(e -> salvar());

        JButton btnLimpar = colocar(Estilo.botao("Limpar", Estilo.SECUNDARIO), 197, 465, 153, 50);
        btnLimpar.setMnemonic(KeyEvent.VK_P);
        btnLimpar.setToolTipText("Limpar o formulário (Alt + P)");
        btnLimpar.addActionListener(e -> limpar());

        JButton btnExcluir = colocar(Estilo.botao("Excluir livro selecionado", Estilo.PERIGO), 30, 526, 320, 50);
        btnExcluir.setMnemonic(KeyEvent.VK_X);
        btnExcluir.setToolTipText("Excluir o livro escolhido na tabela (Alt + X)");
        btnExcluir.addActionListener(e -> excluir());

        // ---------- busca e tabela  ----------
        JLabel lblBusca = colocar(Estilo.negrito("Buscar livro"), 380, 26, 200, 22);
        colocar(txtBusca, 380, 52, 440, 44);
        Estilo.acessivel(lblBusca, txtBusca, "Digite parte do título ou do autor");
        txtBusca.addActionListener(e -> filtrar()); // enter busca

        JButton btnBuscar = colocar(Estilo.botao("Buscar", Estilo.SECUNDARIO), 832, 52, 188, 44);
        btnBuscar.setMnemonic(KeyEvent.VK_B);
        btnBuscar.setToolTipText("Buscar livros (Alt + B)");
        btnBuscar.addActionListener(e -> filtrar());

        Estilo.larguras(tabela, 190, 150, 160, 140);
        colocar(Estilo.rolagem(tabela), 380, 122, 640, 340);
        colocar(lblContagem, 380, 470, 640, 22);

        // clicar numa linha preenche o form para editar
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int i = tabela.getSelectedRow();
                if (i >= 0 && i < exibidos.size()) {
                    selecionado = exibidos.get(i);
                    preencher();
                }
            }
        });
    }

    private void linha(String rotulo, JTextField campo, int y, String dica) {
        JLabel l = colocar(Estilo.negrito(rotulo), 30, y, 320, 22);
        colocar(campo, 30, y + 26, 320, 44);
        Estilo.acessivel(l, campo, dica);
    }

    static String situacao(Livro l) {
        return l.isDisponivel() ? Estilo.ICONE_OK + "Disponível" : Estilo.ICONE_EMPRESTADO + "Emprestado";
    }

    // busca no banco e mostra o resultado na tabela
    private void filtrar() {
        try {
            List<Livro> lista = dao.listar(txtBusca.getText().trim());
            exibidos.clear();
            modelo.setRowCount(0);
            for (Livro l : lista) {
                exibidos.add(l);
                modelo.addRow(new Object[]{l.getTitulo(), l.getAutor(), l.getCategoria(), situacao(l)});
            }
            int n = exibidos.size();
            lblContagem.setText(n + (n == 1 ? " livro encontrado" : " livros encontrados")
                    + " · Clique em uma linha para editar");
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }

    private void preencher() {
        txtTitulo.setText(selecionado.getTitulo());
        txtAutor.setText(selecionado.getAutor());
        txtIsbn.setText(selecionado.getIsbn());
        txtAno.setText(String.valueOf(selecionado.getAnoPublicacao()));
        cbCategoria.setSelectedItem(selecionado.getCategoria());
    }

    private void limparCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtAno.setText("");
        cbCategoria.setSelectedIndex(0);
        selecionado = null;
        tabela.clearSelection();
    }

    private void limpar() {
        limparCampos();
        txtTitulo.requestFocusInWindow();
    }

    // mostra uma mensagem de erro e coloca o cursor no campo com problema
    private void erro(String mensagem, JComponent campo) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
        campo.requestFocusInWindow();
    }

    private void salvar() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtIsbn.getText().trim();
        String anoTexto = txtAno.getText().trim();
        Object cat = cbCategoria.getSelectedItem();
        String categoria = cat == null ? "" : cat.toString().trim();

        // valida o que o usuario digitou
        if (titulo.isEmpty()) {
            erro("Informe o título do livro.", txtTitulo);
            return;
        }
        if (autor.isEmpty()) {
            erro("Informe o autor do livro.", txtAutor);
            return;
        }
        if (!isbn.matches("[0-9-]{10,17}")) {
            erro("O ISBN deve ter só números e hífens (de 10 a 17 caracteres).\nExemplo: 978-0000000001", txtIsbn);
            return;
        }
        if (!anoTexto.matches("-?[0-9]{1,4}")) {
            erro("O ano de publicação deve ser um número.\nExemplo: 1954", txtAno);
            return;
        }
        int ano = Integer.parseInt(anoTexto);
        if (ano > LocalDate.now().getYear()) {
            erro("O ano de publicação não pode ser maior que o ano atual.", txtAno);
            return;
        }
        if (categoria.isEmpty()) {
            erro("Informe a categoria do livro.", cbCategoria);
            return;
        }

        // gravar no banco
        try {
            if (selecionado == null) {
                dao.inserir(new Livro(0, titulo, autor, isbn, ano, categoria)); // o banco cria o id
            } else {
                selecionado.setTitulo(titulo);
                selecionado.setAutor(autor);
                selecionado.setIsbn(isbn);
                selecionado.setAnoPublicacao(ano);
                selecionado.setCategoria(categoria);
                dao.atualizar(selecionado);
            }
            JOptionPane.showMessageDialog(this, "Livro salvo com sucesso!", "BiblioTech", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            filtrar();
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }

    private void excluir() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Clique em um livro da tabela para excluir.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!selecionado.isDisponivel()) {
            JOptionPane.showMessageDialog(this,
                    "Este livro está emprestado e não pode ser excluído.\nRegistre a devolução primeiro.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Estilo.confirmarExclusao(this, "Excluir livro",
                "O livro \"" + selecionado.getTitulo() + "\" será removido do acervo.")) {
            try {
                dao.excluir(selecionado.getIdLivro());
                limparCampos();
                filtrar();
            } catch (SQLIntegrityConstraintViolationException ex) {
                // o banco recusa qnd o livro tem emprestimos registrados 
                JOptionPane.showMessageDialog(this,
                        "Este livro não pode ser excluído porque tem empréstimos registrados\n(mesmo já devolvidos).",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
            } catch (SQLException ex) {
                Estilo.erroBanco(this, ex);
            }
        }
    }

    @Override
    public void atualizar() {
        txtBusca.setText("");
        limparCampos();
        filtrar();
    }
}