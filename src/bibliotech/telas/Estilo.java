/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
/**
 *
 * @author 55219
 */
public class Estilo {
    

    // ---------- CORES ----------
    public static final Color FUNDO = new Color(0xFA, 0xFA, 0xF7);
    public static final Color BRANCO = Color.WHITE;
    public static final Color TEXTO = new Color(0x1F, 0x29, 0x37);
    public static final Color TEXTO_FRACO = new Color(0x4B, 0x55, 0x63);
    public static final Color BORDA = new Color(0x9C, 0xA3, 0xAF);
    public static final Color VERDE = new Color(0x22, 0xC5, 0x5E);
    public static final Color TEXTO_SOBRE_VERDE = new Color(0x05, 0x2E, 0x16);
    public static final Color VERDE_ESCURO = new Color(0x15, 0x80, 0x3D);
    public static final Color INDIGO = new Color(0x4F, 0x46, 0xE5);
    public static final Color INDIGO_ESCURO = new Color(0x31, 0x2E, 0x81);
    public static final Color MENU_FUNDO = new Color(0x11, 0x18, 0x27);
    public static final Color SELECIONADO = new Color(0xE0, 0xE7, 0xFF);
    public static final Color CINZA_CLARO = new Color(0xF3, 0xF4, 0xF6);
    public static final Color CAMPO_BLOQUEADO = new Color(0xEE, 0xEE, 0xEE);
    public static final Color VERMELHO_ESCURO = new Color(0xB9, 0x1C, 0x1C);
    
 // ---------- FONTES ----------
    public static final Font F_TITULO = new Font("SansSerif", Font.BOLD, 28);
    public static final Font F_SECAO = new Font("SansSerif", Font.BOLD, 20);
    public static final Font F_TEXTO = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font F_NEGRITO = new Font("SansSerif", Font.BOLD, 16);
    public static final Font F_BOTAO = new Font("SansSerif", Font.BOLD, 17);
    public static final Font F_DICA = new Font("SansSerif", Font.PLAIN, 14);

    // ---------- ÍCONES ----------
    public static final String ICONE_OK = "\u2713 ";        // ✓
    public static final String ICONE_EMPRESTADO = "\u25C9 "; // ◉
    public static final String ICONE_ALERTA = "\u26A0 ";    // ⚠

    // botão
    public static final int PRIMARIO = 0;
    public static final int SECUNDARIO = 1;
    public static final int PERIGO = 2;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String data(LocalDate d) {
        return d == null ? "" : d.format(FORMATO_DATA);
    }

    // ---------- TEXTOS ----------
    private static JLabel rotuloBase(String texto, Font fonte, Color cor) {
        JLabel l = new JLabel(texto);
        l.setFont(fonte);
        l.setForeground(cor);
        return l;
    }

    public static JLabel titulo(String t) { return rotuloBase(t, F_TITULO, TEXTO); }
    public static JLabel secao(String t) { return rotuloBase(t, F_SECAO, TEXTO); }
    public static JLabel negrito(String t) { return rotuloBase(t, F_NEGRITO, TEXTO); }
    public static JLabel texto(String t) { return rotuloBase(t, F_TEXTO, TEXTO); }
    public static JLabel dica(String t) { return rotuloBase(t, F_DICA, TEXTO_FRACO); }

    // ---------- BORDAS ----------
    public static Border bordaCampo() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDA, 2),
                BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }

    public static Border bordaFoco() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INDIGO, 3),
                BorderFactory.createEmptyBorder(0, 9, 0, 9));
    }

    // troca a borda quando o componente ganha ou perde o foco do teclado
    public static void aplicarFoco(final JComponent c, final Border normal, final Border foco) {
        c.setBorder(normal);
        c.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                c.setBorder(foco);
            }

            @Override
            public void focusLost(FocusEvent e) {
                c.setBorder(normal);
            }
        });
    }

    // ---------- CAMPOS ----------
    private static void estilizarCampo(JTextField c) {
        c.setFont(F_TEXTO);
        c.setForeground(TEXTO);
        c.setBackground(BRANCO);
        aplicarFoco(c, bordaCampo(), bordaFoco());
    }

    public static JTextField campo() {
        JTextField c = new JTextField();
        estilizarCampo(c);
        return c;
    }

    public static JPasswordField campoSenha() {
        JPasswordField c = new JPasswordField();
        estilizarCampo(c);
        return c;
    }

    // campo que só mostra o valor 
    public static JTextField campoBloqueado() {
        JTextField c = campo();
        c.setEditable(false);
        c.setBackground(CAMPO_BLOQUEADO);
        return c;
    }

    public static JComboBox<String> escolha() {
        JComboBox<String> c = new JComboBox<>();
        c.setFont(F_TEXTO);
        c.setForeground(TEXTO);
        c.setBackground(BRANCO);
        return c;
    }

    // liga o rótulo ao campo, dá nome acessível e dica 
    public static void acessivel(JLabel rotulo, JComponent campo, String dica) {
        rotulo.setLabelFor(campo);
        campo.getAccessibleContext().setAccessibleName(rotulo.getText());
        campo.setToolTipText(dica);
    }

    // ---------- BOTÕES ----------
    public static JButton botao(String texto, int tipo) {
        JButton b = new JButton(texto);
        b.setFont(F_BOTAO);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        if (tipo == PRIMARIO) {
            b.setBackground(VERDE);
            b.setForeground(TEXTO_SOBRE_VERDE);
        } else if (tipo == PERIGO) {
            b.setBackground(VERMELHO_ESCURO);
            b.setForeground(Color.WHITE);
        } else {
            b.setBackground(BRANCO);
            b.setForeground(TEXTO);
        }
        aplicarFoco(b, BorderFactory.createLineBorder(BORDA, 2), BorderFactory.createLineBorder(INDIGO, 3));
        return b;
    }

    // ---------- FAIXA DE MENSAGEM (sucesso ou erro, com ícone + texto) ----------
    public static JLabel faixa() {
        JLabel l = new JLabel();
        l.setFont(F_NEGRITO);
        l.setOpaque(true);
        l.setVisible(false);
        return l;
    }

    public static void exibir(JLabel faixa, String texto, boolean erro) {
        faixa.setText((erro ? ICONE_ALERTA : ICONE_OK) + texto);
        faixa.setBackground(erro ? new Color(0xFE, 0xE2, 0xE2) : new Color(0xDC, 0xFC, 0xE7));
        faixa.setForeground(erro ? new Color(0x7F, 0x1D, 0x1D) : new Color(0x14, 0x53, 0x2D));
        faixa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(erro ? VERMELHO_ESCURO : VERDE_ESCURO, 2),
                BorderFactory.createEmptyBorder(0, 12, 0, 12)));
        faixa.setVisible(true);
    }

    // ---------- TABELAS ----------
    // tabela em que o usuário só seleciona (não edita as células)
    public static DefaultTableModel modelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };
    }

    public static JTable tabela(DefaultTableModel modelo) {
        JTable t = new JTable(modelo);
        t.setRowHeight(48);
        t.setFont(F_TEXTO);
        t.setForeground(TEXTO);
        t.setBackground(BRANCO);
        t.setSelectionBackground(SELECIONADO);
        t.setSelectionForeground(TEXTO);
        t.setGridColor(new Color(0xD1, 0xD5, 0xDB));
        t.setShowVerticalLines(false);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setFillsViewportHeight(true);
        JTableHeader h = t.getTableHeader();
        h.setFont(F_NEGRITO);
        h.setBackground(CINZA_CLARO);
        h.setForeground(TEXTO);
        h.setPreferredSize(new Dimension(0, 44));
        h.setReorderingAllowed(false);
        return t;
    }

    public static void larguras(JTable t, int... larguras) {
        for (int i = 0; i < larguras.length; i++) {
            t.getColumnModel().getColumn(i).setPreferredWidth(larguras[i]);
        }
    }

    public static JScrollPane rolagem(JTable t) {
        JScrollPane s = new JScrollPane(t);
        s.setBorder(BorderFactory.createLineBorder(BORDA, 2));
        s.getViewport().setBackground(BRANCO);
        return s;
    }

    // ---------- CONFIRMAÇÃO DE EXCLUSÃO ----------
    // o foco começa em "Cancelar" (opção segura); Esc tbm cancela
    public static boolean confirmarExclusao(java.awt.Component pai, String titulo, String mensagem) {
        String[] opcoes = {"Cancelar", "Sim, excluir"};
        int r = JOptionPane.showOptionDialog(pai, ICONE_ALERTA + mensagem + "\nEsta ação não pode ser desfeita.",
                titulo, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, opcoes, opcoes[0]);
        return r == 1;
    }
    // ---------- ERRO DE BANCO DE DADOS ----------
    public static void erroBanco(java.awt.Component pai, java.sql.SQLException ex) {
        ex.printStackTrace(); // detalhe técnico na aba Output (para o programador)
        JOptionPane.showMessageDialog(pai,
                "Não foi possível acessar o banco de dados.\n"
                + "Confira se o MySQL está ligado e se a senha na classe Conexao está certa.\n\n"
                + "Detalhe: " + ex.getMessage(),
                "Erro de banco de dados", JOptionPane.ERROR_MESSAGE);
    }
}
    

