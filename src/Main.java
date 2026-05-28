import javax.swing.SwingUtilities;

import view.AutoHubFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AutoHubFrame tela = new AutoHubFrame();
            tela.setVisible(true);
        });
    }
}