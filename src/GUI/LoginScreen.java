package GUI;

import Entities.*;
import FileOperations.ChildOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;


public class LoginScreen extends JFrame {

    private JPanel loginScreenPanel; // Ana panel
    private JTextField usernameField; // Kullanıcı adı girdisi için textfield
    private JButton newChildButton; // Yeni kayıt ekranı için buton
    private JButton submitButton; // Giriş butonu
    private JButton visiblePassword; // Şifre Göster/Gizle
    private JPasswordField passwordField; // Şifre giridisi için passwordfield
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private boolean passwordVisible = false; // Şifre anlık olarak görünür ise : true

    public LoginScreen() {

        setTitle("Login Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 150);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalanır.
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );
        setContentPane(loginScreenPanel);
        setVisible(true);

        // Şifre Göster/Gizle butonuna basıldıysa...
        visiblePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordVisible = !passwordVisible;
                passwordField.setEchoChar(passwordVisible ? '\u0000' : '•'); // passwordVisible'a göre şifre gizlenir/gösterilir.
                visiblePassword.setText(passwordVisible ? "Şifreyi Gizle" : "Şifreyi Göster"); // Buton üstündeki yazı güncellenir.
            }
        });

        // FONT DÜZENLEMELERİ //
        Font usernameLabelFont = usernameLabel.getFont();
        Font passwordLabelFont = passwordLabel.getFont();
        Font boldUsernameFont = new Font(usernameLabelFont.getFontName(), Font.BOLD,usernameLabelFont.getSize());
        Font boldPasswordFont = new Font(passwordLabelFont.getFontName(),Font.BOLD,passwordLabelFont.getSize());
        usernameLabel.setFont(boldUsernameFont);
        passwordLabel.setFont(boldPasswordFont);
        ////////////////////////

        // Yeni Kayıt butonuna basıldıysa...
        newChildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ChildRegistrationScreen frame'i başlatılır.
                setVisible(false);
                ChildRegistrationScreen crs = new ChildRegistrationScreen();
                // Sonlandığı anda LoginScreen tekrar görünür olarak ayarlanır.
                crs.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        setVisible(true);
                    }
                });
            }
        });

        // Giriş Yap butonuna basıldıysa...
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kullanıcı adı ve şifre girdileri
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                // Boş girdi bulunuyor ise uyarı mesajı verilir.
                if(username.isEmpty() || password.length == 0){
                    JOptionPane.showMessageDialog(loginScreenPanel, "Hata: Şifre veya Kullanıcı adı girilmedi. Tekrar deneyiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                }


                else {
                    try {
                        // username = "admin" ise yönetici girişi için işlem gerçekleşir.
                        if(username.equals("admin") && adminLogin(password)) {
                            AdminScreen as = new AdminScreen();
                            dispose(); // eğer giriş başarılı olmuş ise koşula girer ve LoginScreen frame'i sonlanır.
                        }

                        // Username "admin" değilse kullanıcı (çocuk) girişi için işlem yapılır
                        // ChildOperations.loginControl() Çalıştırılıp gerekli kontroller yapılır.
                        else if(ChildOperations.loginControl(username,String.valueOf(password))) {
                            try {
                                // Eğer giriş başarılı ise oyun için gerekli frame (GameMainScreen) açılır.
                                GameMainScreen gs = new GameMainScreen(username);
                            } catch (URISyntaxException ex) {
                                throw new RuntimeException(ex);
                            }
                            dispose(); // LoginScreen sonlanır
                        }

                        // Giriş yapılamadıysa uyarı mesajı verilir.
                        else {
                            JOptionPane.showMessageDialog(loginScreenPanel, "Hata: Şifre veya Kullanıcı adı hatalı. Tekrar deneyiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });
    }

    // admin girişi için şifre kontrolü yapan fonksiyon
    public boolean adminLogin(char[] password) throws URISyntaxException {
        Admin admin = DataBase.getInstance().getAdmin();
        if(admin.getPassword().equals(String.valueOf(password))) {
            return true;
        }
        return false;
    }
}
