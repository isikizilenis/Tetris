import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartGui implements KeyListener {

    private JFrame startWindow;
    private JPanel mainPanel; // Hauptpanel für Layout
    BufferedImage arrowsImage = ImageIO.read(new File("/Users/isikizilenis/Documents/java/projects/Tetris/src/arrows.png"));
    Image scaledArrowsImage = arrowsImage.getScaledInstance(150, 105, Image.SCALE_SMOOTH);
    JLabel arrows = new JLabel(new ImageIcon(scaledArrowsImage));

    public StartGui() throws IOException {
        // Hauptfenster einrichten
        startWindow = new JFrame("Tetris");
        startWindow.setSize(450, 370);
        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.setResizable(false);
        startWindow.setLocationRelativeTo(null);

        startWindow.addKeyListener(this);

        // Hauptpanel mit BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        // Willkommenstext oben hinzufügen
        JLabel welcomeTextLabel = new JLabel("Welcome to Tetris!");
        welcomeTextLabel.setForeground(Color.ORANGE);
        welcomeTextLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(welcomeTextLabel, BorderLayout.NORTH);

        JLabel pressSpaceLabel = new JLabel("Press SPACE to Pause or Start");
        pressSpaceLabel.setForeground(Color.RED);
        pressSpaceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        pressSpaceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(pressSpaceLabel, BorderLayout.SOUTH);

        // Mittleres Panel für die Pfeiltasten und Beschreibungen
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();

        // Anpassbare Abstände
        gbc.insets = new Insets(0, 0, 0, 0);

        // Oben: "Up: Rotate" mit HTML
        JLabel upDesc = new JLabel("<html><div style='text-align: center;'>UP:<br>Rotate</div></html>");
        upDesc.setForeground(Color.WHITE);
        upDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0); // Mehr Abstand nach unten
        centerPanel.add(upDesc, gbc);

        // Links: "Left: Move Left"
        JLabel leftDesc = new JLabel("<html><div style='text-align: center;'>LEFT:<br>Move Left</div></html>");
        leftDesc.setForeground(Color.WHITE);
        leftDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(50, 0, 0, 10); // Mehr Abstand nach unten und rechts
        centerPanel.add(leftDesc, gbc);

        // Mittig: Pfeilbild
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0); // Standardabstand
        centerPanel.add(arrows, gbc);

        // Rechts: "Right: Move Right"
        JLabel rightDesc = new JLabel("<html><div style='text-align: center;'>RIGHT:<br>Move Right</div></html>");
        rightDesc.setForeground(Color.WHITE);
        rightDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(50, 10, 0, 0); // Mehr Abstand nach unten und links
        centerPanel.add(rightDesc, gbc);

        // Unten: "Down: Speed Up"
        JLabel downDesc = new JLabel("<html><div style='text-align: center;'>DOWN:<br>Speed Up</div></html>");
        downDesc.setForeground(Color.WHITE);
        downDesc.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0); // Mehr Abstand nach oben
        centerPanel.add(downDesc, gbc);

        // Mittleres Panel hinzufügen
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Hauptpanel dem Fenster hinzufügen
        startWindow.add(mainPanel);

        startWindow.setVisible(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            Gui.run();
            startWindow.dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) throws IOException {
        new StartGui();
    }
}
