package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;

import FileOperations.AdminRegistration;

public class WelcomeScreen extends JFrame{
    private JPanel welcomeScreenPanel; // Ana panel
    private JButton submitButton; // Onay butonu
    private JButton visiblePassword; // Şifre Göster / Gizle
    private JPasswordField passwordField; // Şifre girdisi içi passwordfield

    /////// Labellar ///////
    private JLabel welcomeLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel adminLabel;

    /////////////////////////
    private boolean passwordVisible = false; // Şifre görünür modda ise true olarak güncellenir.

    public WelcomeScreen() {
        setTitle("Welcome Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 150);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalanır.
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );
        setContentPane(welcomeScreenPanel);
        setVisible(true);

        // Şife Göster / Gizle butonuna basıldıysa...
        visiblePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordVisible = !passwordVisible;
                passwordField.setEchoChar(passwordVisible ? '\u0000' : '•');  //  passwordVisible'a göre şifre gizlenir/gösterilir.
                visiblePassword.setText(passwordVisible ? "Şifreyi Gizle" : "Şifreyi Göster"); // Buton üstündeki yazı güncellenir.
            }
        });

        ///////// FONT DÜZENLEMELERİ /////////
        Font usernameLabelFont = usernameLabel.getFont();
        Font passwordLabelFont = passwordLabel.getFont();
        Font boldUsernameFont = new Font(usernameLabelFont.getFontName(), Font.BOLD,usernameLabelFont.getSize());
        Font boldPasswordFont = new Font(passwordLabelFont.getFontName(),Font.BOLD,passwordLabelFont.getSize());
        usernameLabel.setFont(boldUsernameFont);
        passwordLabel.setFont(boldPasswordFont);
        adminLabel.setForeground(Color.RED);
        //////////////////////////////////////

        // Onayla / Kayıt butonuna basıldıysa...
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] password = passwordField.getPassword(); // Şifre okunur.

                // Şifre alanı boş ise uyarı mesajı verilir.
                if(password.length == 0) {
                    JOptionPane.showMessageDialog(welcomeScreenPanel, "Hata: Şifre alanı boş kalamaz. Tekrar deneyiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                }

                // Şifre alınmış işse
                else {
                    try {
                        // yönetici bilgisi (Şifre) dosyaya yazılır.
                        AdminRegistration.writeAdminData(password);
                    } catch (URISyntaxException | IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    dispose(); // frame sonlanır.
                    LoginScreen ls = new LoginScreen(); // LoginScreen başlatılır.
                }
            }
        });
    }
}
