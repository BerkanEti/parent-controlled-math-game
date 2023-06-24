package GUI;

import javax.swing.*;
import java.awt.*;
import java.net.URISyntaxException;

public class AdminScreen extends JFrame {
    private JPanel adminScreenPanel; // ana panel
    private JButton childOperationsButton; // çocuk işlemleri görüntelemek için buton
    private JButton reportButton; // raporlama ekranı için buton
    private JButton exerciseOperationsButton; // alıştırma işlemlerini görüntelemek için buton

    public AdminScreen() {
        setTitle("Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 150);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalanır.
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );
        setContentPane(adminScreenPanel);
        setVisible(true);

        // Butonlara işlevlerine göre listenerlar eklenir.
        exerciseOperationsButton.addActionListener(e -> {
            try {
                new ExerciseOpScreen(); // Alıştırma işlemleri için ekran
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
        childOperationsButton.addActionListener(e -> {
            try {
                new ChildOpScreen(); // Kullanıcı -çocuk- işlemleri için ekran
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
        reportButton.addActionListener(e -> {
            try {
                new GeneralReportingScreen(); // Rapor görünteleme / Kaydetme için ekran
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
