package game;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import java.awt.Image;

public class MainMenu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private final JLabel lblNewLabel_1 = new JLabel("");

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainMenu frame = new MainMenu();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainMenu() {
        setTitle("X-Wing: A Star Wars Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 360, 640);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("X-Wing");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setForeground(Color.ORANGE);
        lblNewLabel.setBounds(0, 11, 360, 85);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
        contentPane.add(lblNewLabel);

        JButton btnNewButton = new JButton("PLAY");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Game game = new Game();
                game.setVisible(true);
                dispose();
            }
        });
        btnNewButton.setBounds(116, 228, 115, 55);
        btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        contentPane.add(btnNewButton);

        JLabel lblNewLabel_2 = new JLabel("A Star Wars Game");
        lblNewLabel_2.setForeground(Color.YELLOW);
        lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblNewLabel_2.setBounds(72, 81, 215, 17);
        contentPane.add(lblNewLabel_2);

        JButton btnNewButton_1 = new JButton("SETTINGS");
        btnNewButton_1.setBounds(116, 315, 115, 55);
        btnNewButton_1.setFont(new Font("Times New Roman", Font.BOLD, 12));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainMenu.this, 
                    "Settings coming soon! \n(Volume: MAX, Graphics: ULTRA)", 
                    "Game Settings", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("CREDITS");
        btnNewButton_2.setBounds(116, 400, 115, 55);
        btnNewButton_2.setFont(new Font("Times New Roman", Font.BOLD, 15));
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainMenu.this, 
                    "Created by: [Your Name Here]\nPowered by Java Swing!", 
                    "Credits", 
                    JOptionPane.PLAIN_MESSAGE);
            }
        });
        contentPane.add(btnNewButton_2);

        ImageIcon originalIcon = loadImage("/assets/Background.png");
        if (originalIcon != null) {
            Image resizedImage = originalIcon.getImage().getScaledInstance(360, 640, Image.SCALE_SMOOTH);
            lblNewLabel_1.setIcon(new ImageIcon(resizedImage));
        } else {
            System.err.println("Could not find file: assets/Background.png");
        }

        lblNewLabel_1.setBounds(0, 0, 360, 640);
        contentPane.add(lblNewLabel_1);
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