/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import bibliotech.dados.Sessao;
import bibliotech.model.Usuario;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author 55219
 */
public class TelaPrincipal extends JFrame {

    private final Usuario usuario = Sessao.usuarioLogado;

    private final JPanel menu = new JPanel(null);
    private final CardLayout cartas = new CardLayout();
    private final JPanel area = new JPanel(cartas);
    private final JLabel lblTitulo = Estilo.titulo("");

    // guardam as telas e os botões do menu pelo nome
    private final Map<String, PainelBase> paineis = new LinkedHashMap<>();
    private final Map<String, String> titulos = new LinkedHashMap<>();
    private final Map<String, JButton> botoesMenu = new LinkedHashMap<>();

    public TelaPrincipal() {
        setTitle("BiblioTech – Sistema de Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setPreferredSize(new Dimension(1280, 684));
        setContentPane(raiz);

        montarMenu();
        raiz.add(menu, BorderLayout.WEST);

        JPanel direita = new JPanel(new BorderLayout());
        direita.add(montarCabecalho(), BorderLayout.NORTH);
        direita.add(area, BorderLayout.CENTER);
        raiz.add(direita, BorderLayout.CENTER);

        montarTelas();
        montarBotaoSair();

        pack();
        setLocationRelativeTo(null);
        irPara("Início");
    }

   
    private void montarTelas() {
        boolean bib = usuario.isBibliotecario();

        registrar("Início", "Início", new PainelInicio(this));
        if (bib) {
            registrar("Livros", "Cadastro de livros", new PainelLivros(this));
            registrar("Leitores", "Cadastro de leitores", new PainelLeitores(this));
        }
        registrar("Empréstimos", "Empréstimos e devoluções", new PainelEmprestimos(this));
        registrar("Consulta de acervo", "Consulta de acervo", new PainelConsulta(this));
        if (bib) {
            registrar("Relatório de atrasos", "Relatório de atrasos", new PainelAtrasos(this));
        }
    }


    // ------------------------------------------------------------
    // Menu lateral
    // ------------------------------------------------------------
    private void montarMenu() {
        menu.setPreferredSize(new Dimension(230, 684));
        menu.setBackground(Estilo.MENU_FUNDO);

        JLabel logo = new JLabel("B", SwingConstants.CENTER);
        logo.setFont(Estilo.F_SECAO);
        logo.setForeground(Estilo.TEXTO_SOBRE_VERDE);
        logo.setOpaque(true);
        logo.setBackground(Estilo.VERDE);
        logo.setBounds(19, 20, 40, 40);
        menu.add(logo);

        JLabel nome = new JLabel("BiblioTech");
        nome.setFont(Estilo.F_SECAO.deriveFont(22f));
        nome.setForeground(Color.WHITE);
        nome.setBounds(70, 20, 150, 40);
        menu.add(nome);
    }

    private JButton criarBotaoMenu(String texto, int y, int tecla) {
        JButton b = new JButton(texto);
        b.setFont(Estilo.F_TEXTO);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setMnemonic(tecla); // atalho Alt + letra
        b.setToolTipText("Abrir: " + texto + " (Alt + " + KeyEvent.getKeyText(tecla) + ")");
        b.setBounds(12, y, 206, 44);
        pintarMenu(b, false);
        // contorno branco de 3 px quando o teclado está nele
        b.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                b.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.WHITE, 3),
                        BorderFactory.createEmptyBorder(0, 13, 0, 0)));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                b.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
            }
        });
        menu.add(b);
        return b;
    }

    private void pintarMenu(JButton b, boolean ativo) {
        b.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        b.setBackground(ativo ? Estilo.VERDE : Estilo.MENU_FUNDO);
        b.setForeground(ativo ? Estilo.TEXTO_SOBRE_VERDE : Color.WHITE);
        b.setFont(ativo ? Estilo.F_NEGRITO : Estilo.F_TEXTO);
    }

    private int posicaoMenu = 0;

    private void registrar(String nome, String titulo, PainelBase painel) {
        int tecla = KeyEvent.VK_I;
        switch (nome) {
            case "Início": tecla = KeyEvent.VK_I; break;
            case "Livros": tecla = KeyEvent.VK_L; break;
            case "Leitores": tecla = KeyEvent.VK_E; break;
            case "Empréstimos": tecla = KeyEvent.VK_M; break;
            case "Consulta de acervo": tecla = KeyEvent.VK_C; break;
            case "Relatório de atrasos": tecla = KeyEvent.VK_R; break;
            default: break;
        }
        paineis.put(nome, painel);
        titulos.put(nome, titulo);
        area.add(painel, nome);

        JButton b = criarBotaoMenu(nome, 88 + posicaoMenu * 52, tecla);
        b.addActionListener(e -> irPara(nome));
        botoesMenu.put(nome, b);
        posicaoMenu++;
    }

    private void montarBotaoSair() {
        JPanel linha = new JPanel();
        linha.setBackground(Color.WHITE);
        linha.setBounds(20, 612, 190, 1);
        menu.add(linha);

        JButton sair = criarBotaoMenu("Sair", 624, KeyEvent.VK_S);
        sair.addActionListener(e -> sair());
    }

    private void sair() {
        Sessao.usuarioLogado = null;
        dispose();
        new TelaLogin().setVisible(true);
    }

    // ------------------------------------------------------------
    // Cabeçalho (título da tela + usuário logado)
    // ------------------------------------------------------------
    private JPanel montarCabecalho() {
        JPanel cab = new JPanel(null);
        cab.setPreferredSize(new Dimension(1050, 64));
        cab.setBackground(Color.WHITE);
        cab.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Estilo.BORDA));

        lblTitulo.setBounds(30, 0, 700, 62);
        cab.add(lblTitulo);

        String inicial = usuario.getNome().substring(0, 1).toUpperCase();
        JLabel avatar = new JLabel(inicial, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setOpaque(false);
        avatar.setBackground(Estilo.INDIGO_ESCURO);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(Estilo.F_NEGRITO);
        avatar.setBounds(776, 12, 36, 36);
        cab.add(avatar);

        JLabel quem = Estilo.texto(usuario.getNome() + " · " + usuario.getPerfil());
        quem.setBounds(820, 0, 210, 62);
        cab.add(quem);
        return cab;
    }

    // ------------------------------------------------------------
    // Navegação
    // ------------------------------------------------------------
    public void irPara(String nome) {
        PainelBase painel = paineis.get(nome);
        if (painel == null) {
            return;
        }
        painel.atualizar();
        cartas.show(area, nome);
        lblTitulo.setText(titulos.get(nome));
        for (Map.Entry<String, JButton> par : botoesMenu.entrySet()) {
            pintarMenu(par.getValue(), par.getKey().equals(nome));
        }
    }

    public PainelBase getPainel(String nome) {
        return paineis.get(nome);
    }
}