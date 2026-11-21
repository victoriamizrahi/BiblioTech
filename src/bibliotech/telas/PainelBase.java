/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bibliotech.telas;

import javax.swing.JComponent;
import javax.swing.JPanel;


/**
 *
 * @author 55219
 */
public abstract class PainelBase extends JPanel {

    public PainelBase() {
        setLayout(null);
        setBackground(Estilo.FUNDO);
    }

    // coloca o componente na tela: x, y, largura, altura
    protected <T extends JComponent> T colocar(T c, int x, int y, int w, int h) {
        c.setBounds(x, y, w, h);
        add(c);
        return c;
    }

    // chamado sempre que a tela é aberta, para recarregar os dados
    public abstract void atualizar();
}
