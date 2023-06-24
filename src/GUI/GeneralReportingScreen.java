package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import Entities.*;
import FileOperations.AdminRegistration;

public class GeneralReportingScreen extends JFrame{
    private JTable reportTable; // Raporların bulunduğu liste için JTable
    private JButton detailedSubmissionButton; // Raporu detaylı görüntüleme işlemi için buton
    private JTextField fileNameField; // Dosya adı girdisi için textfield
    private JButton submitButton; // Onay / Kayıt butonu
    private JComboBox filterBox; // Filtreleme işlemi için combobox ( Kullanıcı adlarını içerir )
    private JPanel filterPanel; // Filtreleme işlemi için panel
    private JPanel mainReportingPanel; // Ana panel
    private JPanel tablePanel; // Liste (JTable) için panel
    private JPanel submissionPanel; // Dosya kayıt ve detaylı görüntüleme işlemleri için bulunan textfield / butonlar için panel
    private JLabel fileNameLabel;
    private JButton filteringButton; // filtreleme butonu

    private ArrayList<ExerciseReport> currentReports = new ArrayList<>();  // Anlık olarak listelenen raporlar

    public GeneralReportingScreen() throws URISyntaxException {
        setTitle("Reporting Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainReportingPanel);
         setSize(1100,500);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalanır.
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );

        doFilter(); // Filtreleme işlemi
        createList("Filtre Yok"); // Başlangıçta filtre bulunmadan raporlar listelenir.
        detailAndSave(); // kayıt ve raporu detaylı görünteleme işlemleri
        setVisible(true);
    }

    public void createList(String filteringOption) throws URISyntaxException {
        // Sütun isimleri
        String[] reportColumnns = {"KULLANICI ADI","ALISTIRMA ADI","SORU SAYISI","ARALIK 1","ARALIK 2","BASLANGIC TARIHI","SURE","SKOR"};
        DefaultTableModel reportModel = new DefaultTableModel(reportColumnns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            } // Hücreler değiştirilemez.
        };

        reportTable = new JTable(reportModel);
        JScrollPane reportsScrollPane = new JScrollPane(reportTable);
        tablePanel.setLayout(new BorderLayout());
        tablePanel.removeAll(); // başlangıçta panel verileri silinir. (Filtreleme işlemi yapılmış olabilir)
        tablePanel.add(reportsScrollPane,BorderLayout.CENTER);

        currentReports.clear();
        if(filteringOption.equals("Filtre Yok")) { // Filtre yoksa bütün çocuklara ait ExerciseResults verilerinin objeleri listelenir.
            for(Child child : DataBase.getInstance().getChilds()) {
                for(ExerciseReport report : child.getExerciseResults()) {
                    reportModel.addRow(report.getReportObject());
                    currentReports.add(report);
                }
            }
        }

        else { // Filtreye göre spesifik olarak (filteringOption'a göre) listeleme yapılır.
            Child child = DataBase.getInstance().findChild(filteringOption);
            for(ExerciseReport report : child.getExerciseResults()) {
                reportModel.addRow(report.getReportObject());
                currentReports.add(report);
            }
        }

        // Ana panele tablePanel uygun konumda eklenir.
        mainReportingPanel.add(tablePanel, BorderLayout.CENTER);
        mainReportingPanel.revalidate();
        mainReportingPanel.repaint();
    }

    public void doFilter() throws URISyntaxException {
        ArrayList<String> filtering = new ArrayList<>(); // filtreleme seçenekleri için ArrayList
        filtering.add("Filtre Yok"); // "Filtre Yok" seçeneği
        ArrayList<Child> childList = DataBase.getInstance().getChilds();
        for(Child child : childList) {
            filtering.add(child.getUsername()); // Sisteme kayıtlı bütün çocukların kullanıcı adları eklenir.
        }

        // combobox'a ArrayList uygun formatta aktarılır.
        filterBox.setModel(new DefaultComboBoxModel<>(filtering.toArray(new String[0])));

        // Ana panele filterPanel uygun konumda eklenir.
        mainReportingPanel.setLayout(new BorderLayout());
        mainReportingPanel.add(filterPanel, BorderLayout.NORTH);

        // Eğer "Filtrele" butonuna basılırsa.
        filteringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) filterBox.getSelectedItem(); // combobox'da seçilen seçenek selected'a atanır.
                assert selected != null;
                try {
                    createList(selected); // selected'a göre JTable tekrar listelenir.
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void detailAndSave() {
        // submissionPanel ana panele uygun konumda eklenir.
        mainReportingPanel.add(submissionPanel, BorderLayout.SOUTH);

        // Dosyaya Kaydet butonuna basılırsa
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = fileNameField.getText(); // Dosya adı verisi
                if(!fileName.isEmpty()) { // eğer bir girdi alındıysa işlem devam eder.
                    saveData(fileName); // saveData fonksiyonu ile kayıt işlemi gerçekleşir.
                    JOptionPane.showMessageDialog(mainReportingPanel,"Veriler Başarıyla Kaydedildi.");
                }
                // Dosya adı kısmı boş işe uyarı mesajı verilir.
                else {
                    JOptionPane.showMessageDialog(mainReportingPanel,"Lütfen Dosya Adı Giriniz.");
                }

            }
        });

        // Detaylı listele butonuna basılırsa
        detailedSubmissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = reportTable.getSelectedRow(); // Table'dan seçilen satıra ait veri alınır.
                if(selectedRow != -1) {
                    ExerciseReport selectedReport = currentReports.get(selectedRow); // Seçilen rapor
                    // // DetailedReportingScreen ile işlem sonlanır.
                    DetailedReportingScreen drs = new DetailedReportingScreen(selectedReport);
                }
            }
        });
    }

    public void saveData(String fileName) {
        // (!) Varsayılan excel ayracı ";" olarak kabul edilmiştir.
        try{
            // getFilePath(fileName) fileName'e göre dizini belirler. (Jar dosyasına göre belirlenir.)
            // fileName, .csv formatındadır.
            FileWriter fileWriter = new FileWriter(getFilePath(fileName));
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            DefaultTableModel defaultTableModel = (DefaultTableModel) reportTable.getModel(); // Table'daki veriler

            // Veriler üzerinde sütunlar dolaşılır ve önce sütun isimleri yazdırılır.
            for(int i = 0; i < defaultTableModel.getColumnCount(); i++) {
                bufferedWriter.write(defaultTableModel.getColumnName(i));
                if(i != defaultTableModel.getColumnCount() -1) {
                    bufferedWriter.write(";");
                }
            }

            // Yeni satır
            bufferedWriter.newLine();


            // Bütün satırlar için sütunlardaki veriler okunur. ve dosyaya yazdırılır.
            for(int row = 0; row < defaultTableModel.getRowCount(); row++) {
                for(int col = 0; col < defaultTableModel.getColumnCount(); col++) {
                    bufferedWriter.write(defaultTableModel.getValueAt(row, col).toString());
                    if (col != defaultTableModel.getColumnCount() - 1) {
                        bufferedWriter.write(";");
                    }
                }
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            fileWriter.close();

        }catch (IOException | URISyntaxException e ) {
            e.printStackTrace();
        }
    }

    // Dosya dizini ve ismine göre String döndüren fonksiyon
    private static String getFilePath(String filePath) throws URISyntaxException {
        String jarPath = AdminRegistration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String jarDirectory = new File(jarPath).getParent();
        return jarDirectory + File.separator + filePath + ".csv";
    }
}
