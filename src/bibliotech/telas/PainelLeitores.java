/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import bibliotech.dao.LeitorDAO;
import bibliotech.model.Leitor;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
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
public class PainelLeitores extends PainelBase {

    private final LeitorDAO dao = new LeitorDAO(); // quem conversa com o banco

    private final JTextField txtNome = Estilo.campo();
    private final JTextField txtCpf = Estilo.campo();
    private final JTextField txtTelefone = Estilo.campo();
    private final JTextField txtEndereco = Estilo.campo();

    private final JTextField txtBusca = Estilo.campo();
    private final DefaultTableModel modelo = Estilo.modelo("Nome", "CPF", "Telefone", "Ativos");
    private final JTable tabela = Estilo.tabela(modelo);
    private final JLabel lblContagem = Estilo.dica("");

    private final ArrayList<Leitor> exibidos = new ArrayList<>();
    private Leitor selecionado = null;

    public PainelLeitores(TelaPrincipal principal) {
        colocar(Estilo.secao("Dados do leitor"), 30, 24, 320, 28);
        linha("Nome completo", txtNome, 60, "Digite o nome completo do leitor");
        linha("CPF", txtCpf, 135, "Formato: 000.000.000-00");
        linha("Telefone", txtTelefone, 210, "Formato: (11) 98888-1111");
        linha("Endereço", txtEndereco, 285, "Digite rua e número (opcional)");

        JButton btnSalvar = colocar(Estilo.botao("Salvar", Estilo.PRIMARIO), 30, 390, 153, 50);
        btnSalvar.setMnemonic(KeyEvent.VK_A);
        btnSalvar.setToolTipText("Salvar o leitor (Alt + A)");
        btnSalvar.addActionListener(e -> salvar());

        JButton btnLimpar = colocar(Estilo.botao("Limpar", Estilo.SECUNDARIO), 197, 390, 153, 50);
        btnLimpar.setMnemonic(KeyEvent.VK_P);
        btnLimpar.setToolTipText("Limpar o formulário (Alt + P)");
        btnLimpar.addActionListener(e -> limpar());

        JButton btnExcluir = colocar(Estilo.botao("Excluir leitor selecionado", Estilo.PERIGO), 30, 451, 320, 50);
        btnExcluir.setMnemonic(KeyEvent.VK_X);
        btnExcluir.setToolTipText("Excluir o leitor escolhido na tabela (Alt + X)");
        btnExcluir.addActionListener(e -> excluir());

        JLabel lblBusca = colocar(Estilo.negrito("Buscar leitor"), 380, 26, 200, 22);
        colocar(txtBusca, 380, 52, 440, 44);
        Estilo.acessivel(lblBusca, txtBusca, "Digite parte do nome ou do CPF");
        txtBusca.addActionListener(e -> filtrar());

        JButton btnBuscar = colocar(Estilo.botao("Buscar", Estilo.SECUNDARIO), 832, 52, 188, 44);
        btnBuscar.setMnemonic(KeyEvent.VK_B);
        btnBuscar.setToolTipText("Buscar leitores (Alt + B)");
        btnBuscar.addActionListener(e -> filtrar());

        Estilo.larguras(tabela, 220, 150, 170, 100);
        colocar(Estilo.rolagem(tabela), 380, 122, 640, 340);
        colocar(lblContagem, 380, 470, 640, 22);

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int i = tabela.getSelectedRow();
                if (i >= 0 && i < exibidos.size()) {
                    selecionado = exibidos.get(i);
                    txtNome.setText(selecionado.getNome());
                    txtCpf.setText(selecionado.getCpf());
                    txtTelefone.setText(selecionado.getTelefone());
                    txtEndereco.setText(selecionado.getEndereco());
                }
            }
        });
    }

    private void linha(String rotulo, JTextField campo, int y, String dica) {
        JLabel l = colocar(Estilo.negrito(rotulo), 30, y, 320, 22);
        colocar(campo, 30, y + 26, 320, 44);
        Estilo.acessivel(l, campo, dica);
    }

    // busca no banco e mostra o resultado na tabela
    private void filtrar() {
        try {
            List<Leitor> lista = dao.listar(txtBusca.getText().trim());
            exibidos.clear();
            modelo.setRowCount(0);
            for (Leitor l : lista) {
                exibidos.add(l);
                modelo.addRow(new Object[]{l.getNome(), l.getCpf(), l.getTelefone(),
                    String.valueOf(l.getEmprestimosAtivos())});
            }
            int n = exibidos.size();
            lblContagem.setText(n + (n == 1 ? " leitor encontrado" : " leitores encontrados")
                    + " · Clique em uma linha para editar");
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
        selecionado = null;
        tabela.clearSelection();
    }

    private void limpar() {
        limparCampos();
        txtNome.requestFocusInWindow();
    }

    private void erro(String mensagem, JComponent campo) {
        JOptionPane.showMessageDialog(this, mensagem, "Atenção", JOptionPane.WARNING_MESSAGE);
        campo.requestFocusInWindow();
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String endereco = txtEndereco.getText().trim();

        // valida o que o usuario digitou 
        if (nome.length() < 3) {
            erro("Informe o nome completo do leitor (mínimo de 3 letras).", txtNome);
            return;
        }
        if (!cpf.matches("[0-9]{3}[.][0-9]{3}[.][0-9]{3}[-][0-9]{2}")) {
            erro("O CPF deve estar no formato 000.000.000-00", txtCpf);
            return;
        }
        if (!telefone.matches("\\([0-9]{2}\\) [0-9]{4,5}-[0-9]{4}")) {
            erro("O telefone deve estar no formato (11) 98888-1111", txtTelefone);
            return;
        }

        // confere o CPF no banco e grava
        try {
            int idAtual = selecionado == null ? 0 : selecionado.getIdLeitor();
            if (dao.cpfJaExiste(cpf, idAtual)) {
                erro("Já existe um leitor cadastrado com este CPF.", txtCpf);
                return;
            }
            if (selecionado == null) {
                dao.inserir(new Leitor(0, nome, cpf, telefone, endereco)); //  banco cria o id
            } else {
                selecionado.setNome(nome);
                selecionado.setCpf(cpf);
                selecionado.setTelefone(telefone);
                selecionado.setEndereco(endereco);
                dao.atualizar(selecionado);
            }
            JOptionPane.showMessageDialog(this, "Leitor salvo com sucesso!", "BiblioTech", JOptionPane.INFORMATION_MESSAGE);
            limparCampos();
            filtrar();
        } catch (SQLIntegrityConstraintViolationException ex) {
            // banco protege > o CPF é único
            erro("Já existe um leitor cadastrado com este CPF.", txtCpf);
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }

    private void excluir() {
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Clique em um leitor da tabela para excluir.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selecionado.getEmprestimosAtivos() > 0) {
            JOptionPane.showMessageDialog(this,
                    "Este leitor ainda tem livros emprestados e não pode ser excluído.\nRegistre as devoluções primeiro.",
                    "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (Estilo.confirmarExclusao(this, "Excluir leitor",
                "O leitor \"" + selecionado.getNome() + "\" será removido do sistema.")) {
            try {
                dao.excluir(selecionado.getIdLeitor());
                limparCampos();
                filtrar();
            } catch (SQLIntegrityConstraintViolationException ex) {
                // o banco recusa qnd o leitor tem historico de emprestimos
                JOptionPane.showMessageDialog(this,
                        "Este leitor não pode ser excluído porque tem empréstimos registrados\n(mesmo já devolvidos).",
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