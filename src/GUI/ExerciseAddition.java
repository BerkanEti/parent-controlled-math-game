package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

import Entities.*;
import FileOperations.ExerciseOperations;

public class ExerciseAddition extends JFrame {

    // Alıştırma verileri için TextField'lar //
    private JTextField exerciseNameField;
    private JTextField minAField;
    private JTextField maxAField;
    private JTextField minBField;
    private JTextField maxBField;
    private JTextField nuOfQField;

    ///////////////////////////////////////////

    ////// Labellar ////////
    private JLabel minALabel;
    private JLabel maxALabel;
    private JLabel minBLabel;
    private JLabel nuOfQLabel;
    private JLabel exerciseNameLabel;
    private JLabel maxBLabel;

    /////////////////////////

    private JPanel exerciseAdditionPanel; // Ana panel
    private JButton submitButton; // Kaydetme butonu

    public ExerciseAddition() {
        setTitle("Exercise Addition");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(750,200);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalanır.
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );
        setContentPane(exerciseAdditionPanel);
        setVisible(true);

        // Kadet butona basıldıysa ...
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{

                    // Herhangi bir alan boş ise uyarı mesajı verilir.
                    if (exerciseNameField.getText().isEmpty() || nuOfQField.getText().isEmpty() || minAField.getText().isEmpty() ||
                            maxAField.getText().isEmpty() || minBField.getText().isEmpty() || maxBField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(exerciseAdditionPanel, "Hata: Lütfen tüm giriş alanlarını doldurun.", "Hata", JOptionPane.ERROR_MESSAGE);
                    }
                    else {
                        // TextField'lar uygun formatta okunur ve bir değişkene atanır.
                        String exerciseName = exerciseNameField.getText();
                        int nuOfQuestions = Integer.parseInt(nuOfQField.getText());
                        int minA = Integer.parseInt(minAField.getText());
                        int maxA = Integer.parseInt(maxAField.getText());
                        int minB = Integer.parseInt(minBField.getText());
                        int maxB = Integer.parseInt(maxBField.getText());

                        // Okunan değerlere göre bir Exercise instance'ı oluşturulur.
                        Exercise exercise = new Exercise(exerciseName,minA,maxA,minB,maxB,nuOfQuestions);

                        // Aralıklada mantıksal bir hata olması durumunda uyarı verilir.
                        if(minA >= maxA || minB >= maxB) {
                            JOptionPane.showMessageDialog(exerciseAdditionPanel, "Hata: Sayı aralıklarını kontrol ediniz. ", "Hata", JOptionPane.ERROR_MESSAGE);

                        }

                        // ExerciseOperations.addExercise(exercise) gelen veriye göre alıştırma eklenir.
                        // Eğer alıştırma ismi daha önce kullanılmadıysa : True
                        else if(ExerciseOperations.addExercise(exercise)) {
                            JOptionPane.showMessageDialog(exerciseAdditionPanel, "Alıştırma Başarıyla Kaydedildi!");
                            dispose(); // Frame sonlanır.
                        }

                        // Aynı alıştırma ismi sistemde bulunuyor ise uyarı mesajı verilir.
                        else {
                            JOptionPane.showMessageDialog(exerciseAdditionPanel, "Hata: Aynı isime sahip başka bir alıştırma bulunmaktadır. ", "Hata", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }catch (NumberFormatException err) {
                    // Formatlama hatası varsa (Alanlar uygun verilerle (Tam sayı yerine String vb.) uyarı mesajı verilir.
                    JOptionPane.showMessageDialog(exerciseAdditionPanel, "Hata: Lütfen tüm giriş alanlarını UYGUN verilerle doldurun.", "Hata", JOptionPane.ERROR_MESSAGE);
                    err.printStackTrace();
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}