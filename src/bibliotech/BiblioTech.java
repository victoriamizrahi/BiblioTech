/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package bibliotech;

import bibliotech.telas.TelaLogin;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author 55219
 */
public class BiblioTech {

    public static void main(String[] args) {
        try {
            
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Não foi possível aplicar o visual padrão: " + e.getMessage());
        }
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
 