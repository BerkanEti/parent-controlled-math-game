package GUI;

import FileOperations.ChildOperations;

import Entities.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

public class ChildRegistrationScreen extends  JFrame{
    private JPanel childRegistrationPanel; // Ana panel
    private JTextField usernameField; // Kullanıcı adı için textfield
    private JButton submitButton; // Onay butonu
    private JPasswordField passwordField; // şifre için passwordfield
    private JButton visiblePassword; // Şifreyi göster/gizle butonu

    // Labellar //
    private JLabel passwordLabel;
    private JLabel infoLabel;
    private JLabel usernameLabel;

    //////////////
    private boolean passwordVisible = false; // Eğer şifre görünür modda ise true olarak değiştirilir.

    public ChildRegistrationScreen() {
        setTitle("User Addition Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 150);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ekrana göre ortalanır.
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );
        setContentPane(childRegistrationPanel);
        setVisible(true);

        // Şifreyi Göster / Gizle butonuna basılırsa ...
        visiblePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordVisible = !passwordVisible;
                passwordField.setEchoChar(passwordVisible ? '\u0000' : '•'); // Anlık duruma göre field güncellenir.
                visiblePassword.setText(passwordVisible ? "Şifreyi Gizle" : "Şifreyi Göster"); // Buton üstündeki yazı değişir.
            }
        });

        // Font Ayarlamaları //
        Font usernameLabelFont = usernameLabel.getFont();
        Font passwordLabelFont = passwordLabel.getFont();
        Font boldUsernameFont = new Font(usernameLabelFont.getFontName(), Font.BOLD,usernameLabelFont.getSize());
        Font boldPasswordFont = new Font(passwordLabelFont.getFontName(),Font.BOLD,passwordLabelFont.getSize());
        usernameLabel.setFont(boldUsernameFont);
        passwordLabel.setFont(boldPasswordFont);
        infoLabel.setForeground(Color.red);
        ////////////////////////

        // Kayıt / Onaylama Butonuna basılırsa ...
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword(); // Şifre ve kullanıcı adı fieldlardan okunur.
                // Eğer ikisinden biri boşsa uyarı mesajı verilir.
                if(password.length == 0 ||username.isEmpty()) {
                    JOptionPane.showMessageDialog(childRegistrationPanel, "Hata: Şifre veya Kullanıcı adı girilmedi. Tekrar deneyiniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    Child aChild = new Child(username,String.valueOf(password)); // Alınan verilere göre aChild oluşturulur.
                    try {
                        // Eğer Kullanıcı adı daha önce kullanılmamışsa şifre oluşturulur.
                        if(ChildOperations.addChildren(aChild)) {
                            JOptionPane.showMessageDialog(childRegistrationPanel, "Kullanıcı başarıyla kaydedildi.","Message", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }

                        // Kullanılmışsa tekrar input istenir.
                        else {
                            JOptionPane.showMessageDialog(childRegistrationPanel, "Hata: Bu kullanıcı adına sahip başka bir kullanıcı bulunuyor.", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
}
