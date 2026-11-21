/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import bibliotech.dados.Sessao;
import bibliotech.dao.UsuarioDAO;
import bibliotech.model.Usuario;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.sql.SQLException;

/**
 *
 * @author 55219
 */
public class TelaLogin extends JFrame {

    private final JTextField txtUsuario = Estilo.campo();
    private final JPasswordField txtSenha = Estilo.campoSenha();
    private final JLabel lblErro = Estilo.faixa();

    public TelaLogin() {
        setTitle("BiblioTech – Sistema de Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel fundo = new JPanel(null);
        fundo.setBackground(Estilo.FUNDO);
        fundo.setPreferredSize(new Dimension(1280, 684));
        setContentPane(fundo);

        // cartão branco no centro
        JPanel cartao = new JPanel(null);
        cartao.setBackground(Estilo.BRANCO);
        cartao.setBorder(BorderFactory.createLineBorder(Estilo.BORDA, 2));
        cartao.setBounds(420, 44, 440, 580);
        fundo.add(cartao);

        // logo "B"
        JLabel logo = new JLabel("B", SwingConstants.CENTER);
        logo.setFont(Estilo.F_SECAO);
        logo.setForeground(Estilo.BRANCO);
        logo.setOpaque(true);
        logo.setBackground(Estilo.INDIGO_ESCURO);
        logo.setBounds(200, 27, 40, 40);
        cartao.add(logo);

        JLabel nome = Estilo.titulo("BiblioTech");
        nome.setFont(Estilo.F_TITULO.deriveFont(32f));
        nome.setHorizontalAlignment(SwingConstants.CENTER);
        nome.setBounds(40, 72, 360, 44);
        cartao.add(nome);

        JLabel sub = Estilo.dica("Sistema de gerenciamento de biblioteca");
        sub.setFont(Estilo.F_TEXTO);
        sub.setHorizontalAlignment(SwingConstants.CENTER);
        sub.setBounds(40, 118, 360, 24);
        cartao.add(sub);

        JLabel lblUsuario = Estilo.negrito("Usuário");
        lblUsuario.setBounds(40, 173, 360, 22);
        cartao.add(lblUsuario);
        txtUsuario.setBounds(40, 201, 360, 44);
        cartao.add(txtUsuario);
        Estilo.acessivel(lblUsuario, txtUsuario, "Digite seu nome de usuário");

        JLabel lblSenha = Estilo.negrito("Senha");
        lblSenha.setBounds(40, 260, 360, 22);
        cartao.add(lblSenha);
        txtSenha.setBounds(40, 287, 360, 44);
        cartao.add(txtSenha);
        Estilo.acessivel(lblSenha, txtSenha, "Digite sua senha");

        lblErro.setBounds(40, 359, 360, 46);
        cartao.add(lblErro);

        JButton btnEntrar = Estilo.botao("Entrar", Estilo.PRIMARIO);
        btnEntrar.setBounds(40, 429, 360, 52);
        btnEntrar.setMnemonic(KeyEvent.VK_E);
        btnEntrar.setToolTipText("Entrar no sistema (ou aperte Enter)");
        btnEntrar.addActionListener(e -> entrar());
        cartao.add(btnEntrar);
        getRootPane().setDefaultButton(btnEntrar); // Enter aciona o botão Entrar

        JLabel perfis = Estilo.dica("Perfis: Bibliotecário e Atendente");
        perfis.setFont(Estilo.F_TEXTO);
        perfis.setHorizontalAlignment(SwingConstants.CENTER);
        perfis.setBounds(40, 498, 360, 24);
        cartao.add(perfis);

        JLabel atalho = Estilo.dica("Atalho: Enter para entrar");
        atalho.setFont(Estilo.F_TEXTO);
        atalho.setHorizontalAlignment(SwingConstants.CENTER);
        atalho.setBounds(40, 526, 360, 24);
        cartao.add(atalho);

        pack();
        setLocationRelativeTo(null);
        txtUsuario.requestFocusInWindow();
    }

   private void entrar() {
        String login = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            Estilo.exibir(lblErro, "Preencha usuário e senha.", true);
            return;
        }

        try {
            Usuario usuario = new UsuarioDAO().autenticar(login, senha);
            if (usuario == null) {
                Estilo.exibir(lblErro, "Usuário ou senha incorretos.", true);
                txtSenha.setText("");
                txtSenha.requestFocusInWindow();
                return;
            }
            Sessao.usuarioLogado = usuario;
            dispose(); // fecha o login
            new TelaPrincipal().setVisible(true); // abre o sistema
        } catch (SQLException ex) {
            Estilo.erroBanco(this, ex);
        }
    }
}