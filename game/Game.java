package game;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Image;

public class Game extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Game frame = new Game();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 360, 640); // Set window size to 360x640
        setLocationRelativeTo(null); // Center window on screen
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Title label
        JLabel lblMode = new JLabel("CHOOSE YOUR GAMEMODE");
        lblMode.setHorizontalAlignment(SwingConstants.CENTER);
        lblMode.setForeground(new Color(255, 215, 0));
        lblMode.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblMode.setBounds(10, 80, 324, 43); // Adjust position
        contentPane.add(lblMode);

        // Survival button
        JButton btnSurvive = new JButton("SURVIVAL");
        btnSurvive.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnSurvive.setBounds(100, 217, 140, 50); // Adjust position
        contentPane.add(btnSurvive);
        btnSurvive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SurvivalMode survival = new SurvivalMode();
                survival.setVisible(true);
                dispose();
            }
        });

        // Story button
        JButton btnStory = new JButton("STORY");
        btnStory.setFont(new Font("Times New Roman", Font.BOLD, 18));
        btnStory.setBounds(100, 366, 140, 50); // Adjust position
        contentPane.add(btnStory);

        // Back button
        JButton btnExit = new JButton("BACK");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainMenu menu = new MainMenu();
                menu.setVisible(true);
                dispose();
            }
        });
        btnExit.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnExit.setBounds(10, 567, 89, 23); // Adjust position
        contentPane.add(btnExit);

        // Background image
        JLabel lblBackground = new JLabel();
        ImageIcon originalIcon = loadImage("/assets/Background2.png");
        if (originalIcon != null) {
            Image resizedImage = originalIcon.getImage().getScaledInstance(360, 640, Image.SCALE_SMOOTH);
            lblBackground.setIcon(new ImageIcon(resizedImage));
        } else {
            System.err.println("Could not find file: assets/Background2.png");
        }
        lblBackground.setBounds(0, 0, 360, 640); // Ensure background fills the frame
        contentPane.add(lblBackground);
    }

    private ImageIcon loadImage(String resourcePath) {
        java.net.URL resourceUrl = getClass().getResource(resourcePath);
        if (resourceUrl != null) {
            return new ImageIcon(resourceUrl);
        }

        String filePath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
        java.io.File imageFile = new java.io.File(filePath);
        if (!imageFile.isFile()) {
            imageFile = new java.io.File("..", filePath);
        }
        return imageFile.isFile() ? new ImageIcon(imageFile.getPath()) : null;
    }
}