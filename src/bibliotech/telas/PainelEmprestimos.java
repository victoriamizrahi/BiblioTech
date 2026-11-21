/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import bibliotech.dao.EmprestimoDAO;
import bibliotech.dao.LeitorDAO;
import bibliotech.dao.LivroDAO;
import bibliotech.model.Emprestimo;
import bibliotech.model.Leitor;
import bibliotech.model.Livro;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author 55219
 */
public class PainelEmprestimos extends PainelBase {

    // conversa com o banco
    private final LeitorDAO leitorDAO = new LeitorDAO();
    private final LivroDAO livroDAO = new LivroDAO();
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    private final JComboBox<String> cbLeitor = Estilo.escolha();
    private final JComboBox<String> cbLivro = Estilo.escolha();
    private final JTextField txtSaida = Estilo.campoBloqueado();
    private final JTextField txtPrevista = Estilo.campoBloqueado();
    private final JLabel faixa = Estilo.faixa();

    private final DefaultTableModel modelo = Estilo.modelo("Leitor", "Livro", "Saída", "Prevista", "Situação");
    private final JTable tabela = Estilo.tabela(modelo);

    // listas paralelas aos itens das caixas e da tabela 
    private final ArrayList<Leitor> leitoresLista = new ArrayList<>();
    private final ArrayList<Livro> livrosLista = new ArrayList<>();
    private final ArrayList<Emprestimo> ativos = new ArrayList<>();

    public PainelEmprestimos(TelaPrincipal principal) {
        colocar(Estilo.secao("Novo empréstimo"), 30, 20, 300, 28);

        JLabel lblLeitor = colocar(Estilo.negrito("Leitor"), 30, 60, 300, 22);
        colocar(cbLeitor, 30, 86, 300, 44);
        Estilo.acessivel(lblLeitor, cbLeitor, "Escolha o leitor que vai retirar o livro");

        JLabel lblLivro = colocar(Estilo.negrito("Livro (somente disponíveis)"), 349, 60, 341, 22);
        colocar(cbLivro, 349, 86, 341, 44);
        Estilo.acessivel(lblLivro, cbLivro, "Escolha o livro. Só aparecem livros disponíveis");

        JButton btnRegistrar = colocar(Estilo.botao("Registrar empréstimo", Estilo.PRIMARIO), 709, 86, 311, 44);
        btnRegistrar.setMnemonic(KeyEvent.VK_G);
        btnRegistrar.setToolTipText("Registrar o empréstimo (Alt + G)");
        btnRegistrar.addActionListener(e -> registrar());

        JLabel lblSaida = colocar(Estilo.negrito("Data de saída"), 30, 140, 200, 22);
        colocar(txtSaida, 30, 162, 200, 44);
        Estilo.acessivel(lblSaida, txtSaida, "Data de hoje, preenchida automaticamente");

        JLabel lblPrevista = colocar(Estilo.negrito("Devolução prevista"), 250, 140, 200, 22);
        colocar(txtPrevista, 250, 162, 200, 44);
        Estilo.acessivel(lblPrevista, txtPrevista, "Data calculada automaticamente: 14 dias após a saída");

        colocar(Estilo.dica("Calculada automaticamente (14 dias)"), 470, 175, 400, 22);

        colocar(faixa, 30, 232, 990, 44);

        colocar(Estilo.secao("Empréstimos ativos"), 30, 303, 400, 28);
        Estilo.larguras(tabela, 230, 279, 151, 165, 165);
        colocar(Estilo.rolagem(tabela), 30, 336, 990, 194);

        JButton btnDevolver = colocar(Estilo.botao("Registrar devolução do selecionado", Estilo.SECUNDARIO), 30, 540, 360, 50);
        btnDevolver.setMnemonic(KeyEvent.VK_D);
        btnDevolver.setToolTipText("Dar baixa no empréstimo escolhido na tabela (Alt + D)");
        btnDevolver.addActionListener(e -> devolver());
    }

    // recarrega do banco >> caixas, datas e tabela
    private void carregar() {
        try {
            cbLeitor.removeAllItems();
            leitoresLista.clear();
            for (Leitor l : leitorDAO.listar("")) {
                leitoresLista.add(l);
                cbLeitor.addItem(l.getNome());
            }

            cbLivro.removeAllItems();
            livrosLista.clear();
            for (Livro l : livroDAO.listarDisponiveis()) { // so livros disponiveis 
                livrosLista.add(l);
                cbLivro.addItem(l.getTitulo());
            }

            LocalDate hoje = LocalDate.now();
            txtSaida.setText(Estilo.data(hoje));
            txtPrevista.setText(Estilo.data(hoje.plusDays(14)));

            ativos.clear();
            modelo.setRowCount(0);
            for (Emprestimo e : emprestimoDAO.listarAtivos()) {
                ativos.add(e);
                modelo.addRow(new Object[]{
                    e.getLeitor().getNome(),
                    e.getLivro().getTitulo(),
                    Estilo.data(e.getDataSaida()),
                    Estilo.data(e.getDataPrevistaDevolucao()),
                    PainelInicio.textoSituacao(e)
                });
            }
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }

    private void registrar() {
        int iLeitor = cbLeitor.getSelectedIndex();
        int iLivro = cbLivro.getSelectedIndex();
        if (iLeitor < 0) {
            Estilo.exibir(faixa, "Cadastre um leitor antes de registrar um empréstimo.", true);
            return;
        }
        if (iLivro < 0) {
            Estilo.exibir(faixa, "Não há livros disponíveis no momento.", true);
            return;
        }
        Leitor leitor = leitoresLista.get(iLeitor);
        Livro livro = livrosLista.get(iLivro);

        try {
            boolean registrou = emprestimoDAO.registrar(leitor.getIdLeitor(), livro.getIdLivro());
            carregar();
            if (registrou) {
                Estilo.exibir(faixa, "Empréstimo registrado com sucesso: " + leitor.getNome()
                        + " – " + livro.getTitulo() + ".", false);
            } else { // RNF004>>> alguem emprestou este livro enquanto a tela estava aberta
                Estilo.exibir(faixa, "Este livro já está emprestado.", true);
            }
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }

    private void devolver() {
        int i = tabela.getSelectedRow();
        if (i < 0 || i >= ativos.size()) {
            Estilo.exibir(faixa, "Clique em um empréstimo da tabela para registrar a devolução.", true);
            return;
        }
        Emprestimo e = ativos.get(i);
        try {
            boolean devolveu = emprestimoDAO.devolver(e.getIdEmprestimo()); // RF005
            carregar();
            if (devolveu) {
                Estilo.exibir(faixa, "Devolução registrada: " + e.getLivro().getTitulo()
                        + " (" + e.getLeitor().getNome() + ").", false);
            } else {
                Estilo.exibir(faixa, "Este empréstimo já havia sido devolvido.", true);
            }
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }

    // usado pela tela de consulta > j deixa o livro escolhido na caixa
    public void preSelecionarLivro(Livro livro) {
        for (int i = 0; i < livrosLista.size(); i++) {
            if (livrosLista.get(i).getIdLivro() == livro.getIdLivro()) {
                cbLivro.setSelectedIndex(i);
                break;
            }
        }
    }

    @Override
    public void atualizar() {
        faixa.setVisible(false);
        carregar();
    }
}