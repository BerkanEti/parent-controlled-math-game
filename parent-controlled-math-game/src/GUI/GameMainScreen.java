package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;
import java.util.ArrayList;

import Entities.*;

public class GameMainScreen extends JFrame {
    private JPanel mainPanel; // Ana panel
    private JButton submitButton; // Onay (Oyuna başlama) Butonu
    private JTable exerciseTable; // Alıştırma listesinin bulunduğu JTable
    private JPanel submissionPanel; // Seçim onaylama paneli
    private JPanel exerciseListPanel; // JTable'ın bulunduğu panel

    public GameMainScreen(String currentUsername) throws URISyntaxException {
        setTitle("Game Mainmenu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,300);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalalanır
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );
        setContentPane(mainPanel);

        // Parametreye göre Child oluşturulur.
        // DataBase içindeki findChild(String username) metodu kullanılır.
        Child child = DataBase.getInstance().findChild(currentUsername);

        createExerciseTable(child); // Oyunu oynayacak kullanıcıya göre alıştırmalar listelenir.
        // Her kullanıcıya aynı alıştırmalar listelenir ancak en iyi skor/zamanlar farklılık gösterir.

        // Eğer oyuna başlamak için butona basılmış ise..
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = exerciseTable.getSelectedRow(); // JTable'dan seçilen satır
                if(selectedRow != -1){ // Eğer bir satır seçilmiş ise

                    // Seçilen satırdaki alıştırma adı bir Stringe atanır.
                    // alıştırma adına göre alıştırma bulunur. ( findExercise(String exerciseName) metodu ile )
                    String chosenExerciseName = (String) exerciseTable.getValueAt(selectedRow,0);
                    Exercise chosenExercise = null;
                    try {
                        chosenExercise = DataBase.getInstance().findExercise(chosenExerciseName);
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Kullanıcı ve alıştırmayı parametre olarak alan GamePlayScreen başlatılır.
                    GamePlayScreen gps = new GamePlayScreen(child,chosenExercise);
                    gps.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            try {
                                updateExerciseTable(child); // GamePlayScreen sonlandığı anda liste güncellenir.
                            } catch (URISyntaxException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    });
                }
            }
        });
        setVisible(true);
    }

    public void createExerciseTable(Child child) throws URISyntaxException {
        ArrayList<Exercise> exerciseList = DataBase.getInstance().getExercises();
        // Sütun isimleri
        String exerciseColumns[] = {"ALIŞTIRMA ADI","SORU SAYISI","ARALIK 1","ARALIK 2","EN İYİ SKOR","EN İYİ ZAMAN"};
        DefaultTableModel exerciseModel = new DefaultTableModel(exerciseColumns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hücreler değiştirlemez.
            }
        };

        exerciseTable = new JTable(exerciseModel);
        JScrollPane exerciseScrollPane = new JScrollPane(exerciseTable);
        exerciseListPanel.setLayout(new BorderLayout());
        exerciseListPanel.add(exerciseScrollPane,BorderLayout.CENTER);
        // Table model'e getGameObjects() metodu ile beraber gerekli objeler eklenir.
        for(Entities.Exercise list : exerciseList) {
            exerciseModel.addRow(list.getGameObjects(child));
        }
    }

    // Oyun sonlandığı anda çalışır ve JTable'ı yeniler.
    public void updateExerciseTable(Child child) throws URISyntaxException {
        DefaultTableModel tableModel = (DefaultTableModel) exerciseTable.getModel();
        tableModel.setRowCount(0);

        ArrayList<Exercise> exerciseList = DataBase.getInstance().getExercises();
        for (Exercise exercise : exerciseList) {
            tableModel.addRow(exercise.getGameObjects(child));
        }
    }
}