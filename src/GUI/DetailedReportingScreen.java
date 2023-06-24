package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Entities.*;
import FileOperations.AdminRegistration;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DetailedReportingScreen extends  JFrame {
    private JPanel mainDetailedPanel; // Ana panel
    private JTable detailedReportTable; // Listeleme için JTable'ın bulunduğu panel
    private JTextField fileNameField; // Dosya adı için texfield
    private JButton submitButton; // Kayıt butonu

    ////////// Labellar //////////
    private JLabel infoLabel;
    private JLabel usernameLabel;
    private JLabel exerciseNameLabel;
    private JLabel dateLabel;
    private JPanel tablePanel;
    private JPanel submissionPanel;
    private JPanel infoPanel;

    ///////////////////////////////
    private final ExerciseReport exerciseReport; // Detaylı gösterilecek alıştırma raporu

    public DetailedReportingScreen(ExerciseReport exerciseReport) {
        setTitle("Detailed Reporing Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainDetailedPanel);
        setSize(1100, 500);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalanır.
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );

        this.exerciseReport = exerciseReport; // Parametreye göre this.exerciseReport güncellenir.

        setFonts(); // Font düzenlemeleri
        createList(); // Listeleme için JTable oluşturulur.
        save(); // Dosya kayıt işlemi
        setVisible(true);

    }

    public void setFonts() {


        String exerciseName = exerciseReport.getExercise().getExerciseName(); // Alıştırma adı
        String dateTime = exerciseReport.getFormattedDateTime(); // Alıştırma tarihi
        Child child = exerciseReport.getExerciseOwner(); // Alıştırmayı yapan kullanıcı

        usernameLabel.setText(usernameLabel.getText() + " " + child.getUsername());
        exerciseNameLabel.setText(exerciseNameLabel.getText() + " " + exerciseName);
        dateLabel.setText(dateLabel.getText() + " " + dateTime);

        /////////////////////// FONT DÜZENLEMELERİ ///////////////////////
        Font boldUsernameFont = new Font(usernameLabel.getFont().getFontName(), Font.BOLD, usernameLabel.getFont().getSize());
        Font boldExerciseFont = new Font(exerciseNameLabel.getFont().getFontName(),Font.BOLD, exerciseNameLabel.getFont().getSize());
        Font boldDateFont = new Font(dateLabel.getFont().getFontName(),Font.BOLD, dateLabel.getFont().getSize());
        Font boldInfoFont = new Font(infoLabel.getFont().getFontName(),Font.BOLD, infoLabel.getFont().getSize());
        usernameLabel.setFont(boldUsernameFont);
        exerciseNameLabel.setFont(boldExerciseFont);
        dateLabel.setFont(boldDateFont);
        infoLabel.setFont(boldInfoFont);
        infoLabel.setForeground(Color.red);
        ////////////////////////////////////////////////////////////////////////

        // Ana panele infoPanel eklenir.
        mainDetailedPanel.setLayout(new BorderLayout()); //
        mainDetailedPanel.add(infoPanel,BorderLayout.NORTH);

    }

    public void createList() {
        // Alıştırmanın her cevabı için detayların bulunduğu ArrayList
        ArrayList<AnswerDetails> answers = exerciseReport.getAnswers();

        // Sütun isimleri
        String[] detailedReportColumns = {"ID","1.CARPAN","2.CARPAN","KAYDEDILEN CEVAP","DOGRU CEVAP","DURUM","CEVAPLANMA SURESI"};
        DefaultTableModel detailedReportModel = new DefaultTableModel(detailedReportColumns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } // Hücreler değiştirilemez.
        };

        detailedReportTable = new JTable(detailedReportModel);
        JScrollPane reportScrollPane = new JScrollPane(detailedReportTable);
        mainDetailedPanel.add(reportScrollPane, BorderLayout.CENTER);

        for(AnswerDetails answerDetail : answers) {  // ArrayList üzerinde gezinerek veriler JTable modeline yazılır.
            detailedReportModel.addRow(answerDetail.getDetailedObject());
        }
    }

    public void save() {
        // Ana panele kayıt paneli eklenir.
        mainDetailedPanel.add(submissionPanel,BorderLayout.SOUTH);

        // Dosyaya kaydet butonuna basılırsa ...
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = fileNameField.getText(); // Dosya ismi girdisi

                // Dosya ismi okunduysa işlem devam eder.
                if(!fileName.isEmpty()) {
                    saveData(fileName); // saveData fonksiyonu ile Dosya kaydedilir.
                    JOptionPane.showMessageDialog(mainDetailedPanel,"Veriler Başarıyla Kaydedildi.");
                }

                // TextField boş ise uyarı mesajı verilir.
                else {
                    JOptionPane.showMessageDialog(mainDetailedPanel,"Lütfen Dosya Adı Giriniz.");
                }
            }
        });
    }

    public void saveData(String fileName) {
        // (!) Varsayılan excel ayracı ";" olarak kabul edilmiştir.
        try{
            FileWriter fileWriter = new FileWriter(getFilePath(fileName)); // Dosya dizini belirlenir.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            DefaultTableModel defaultTableModel = (DefaultTableModel) detailedReportTable.getModel(); // Table'daki veriler

            // getFilePath(fileName) fileName'e göre dizini belirler. (Jar dosyasına göre belirlenir.)
            // fileName, .csv formatındadır.

            // Veriler üzerinde sütunlar dolaşılır ve önce sütun isimleri yazdırılır.
            for(int i = 0; i < defaultTableModel.getColumnCount(); i++) {
                bufferedWriter.write(defaultTableModel.getColumnName(i));
                if(i != defaultTableModel.getColumnCount() - 1) {
                    bufferedWriter.write(";");
                }
            }

            // Yeni satır
            bufferedWriter.newLine();

            // Bütün satırlar için sütunlardaki veriler okunur. ve dosyaya yazdırılır.
            for(int row = 0; row < defaultTableModel.getRowCount(); row++) {
                for(int col = 0; col < defaultTableModel.getColumnCount(); col++) {
                    bufferedWriter.write(defaultTableModel.getValueAt(row,col).toString());
                    if(col != defaultTableModel.getColumnCount() - 1) {
                        bufferedWriter.write(";");
                    }
                }
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            fileWriter.close();

        }catch (IOException e ) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // Dosya dizini ve ismine göre String döndüren fonksiyon
    private static String getFilePath(String filePath) throws URISyntaxException {
        String jarPath = AdminRegistration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String jarDirectory = new File(jarPath).getParent();
        return jarDirectory + File.separator + filePath + ".csv";
    }
}