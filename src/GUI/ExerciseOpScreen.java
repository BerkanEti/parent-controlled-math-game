package GUI;

import Entities.DataBase;
import Entities.Exercise;
import FileOperations.ExerciseOperations;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ExerciseOpScreen extends JFrame {

    private JPanel mainExercisePanel; // Ana panel
    private JPanel exerciseTablePanel; // Listeme işlemi için JTable'ın bulunduğu panel
    private JPanel exerciseOperationPanel; // Alıştırma işlemlerinin ( Ekleme - Silme ) Panel
    private JButton additionButton; // Ekleme butonu
    private JButton deletionButton; // Silme butonu
    private JTable exerciseTable; // Liste için JTable

    public ExerciseOpScreen() throws URISyntaxException {
        setTitle("Exercise Operations");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900,700);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalanır.
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );
        setContentPane(mainExercisePanel);
        //   frame.pack();

        createExerciseList(); // Listeleme
        exerciseAddition(); // Ekleme
        exerciseDeletion(); // Silme
        setVisible(true);
    }
    public void createExerciseList() throws URISyntaxException {
        ArrayList<Exercise> exerciseList = DataBase.getInstance().getExercises(); // Alıştırmaların bulunduğu ArrayList
        String exerciseColumns[] = {"ALIŞTIRMA ADI","SORU SAYISI","ARALIK 1","ARALIK 2"}; // Sütun isimleri
        DefaultTableModel exerciseModel = new DefaultTableModel(exerciseColumns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hücreler değiştirilemez.
            }
        };

        exerciseTable = new JTable(exerciseModel);
        JScrollPane exerciseScrollPane = new JScrollPane(exerciseTable);
        exerciseTablePanel.setLayout(new BorderLayout());
        exerciseTablePanel.add(exerciseScrollPane,BorderLayout.CENTER);
        // Liste üzerinde dolaşılarak her iterasyonda satıra ArrayList'teki alıştırmanın objeleri eklenir.
        for(Entities.Exercise list : exerciseList) {
            exerciseModel.addRow(list.getObjects());
        }
    }

    public void exerciseAddition() {
        // Ekleme butonuna basılmış ise...
        additionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ekleme işlemi için ExerciseAddition çalıştırılır.
                ExerciseAddition ea = new ExerciseAddition();
                ea.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        try {
                            updateExerciseTable(); // ExerciseAddition frame'i kapatıldığında JTable güncellenir.
                        } catch (URISyntaxException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        });
    }
    public void exerciseDeletion() {
        // Silme butonuna basılmış ise...
        deletionButton.addActionListener(e -> {
            int selectedRow = exerciseTable.getSelectedRow(); // Seçilmiş satırın konumu integer'a eşitlenir.
            if(selectedRow != -1) { // Eğer bir satır seçilmiş ise...
                // Kullanıcıdan onay alınır.
                int result = JOptionPane.showConfirmDialog(mainExercisePanel, "Alıştırmayı silmek istediğinize emin misiniz?", "Alıştırma Silme", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    // Seçilen satırda bulunan alıştırmanın adı bir String'e eşitlenir.
                    String exerciseName = (String) exerciseTable.getValueAt(selectedRow, 0);
                    try {
                        // ExerciseOperations.deleteExercise ile işlem gerçekleşir.
                        ExerciseOperations.deleteExercise(exerciseName, () -> {
                                SwingUtilities.invokeLater(() -> {
                                    try {
                                        updateExerciseTable(); // JTable güncellenir.
                                    } catch (URISyntaxException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                });
                        });
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(mainExercisePanel, "Alıştırma Başarıyla Silindi!");
                }
            }
        });
    }

    // Ekleme/Silme işlemlerinden sonra tablodaki değişiklerin yapılması için fonksiyon
    public void updateExerciseTable() throws URISyntaxException {
        DefaultTableModel tableModel = (DefaultTableModel) exerciseTable.getModel();
        tableModel.setRowCount(0);

        ArrayList<Exercise> exerciseList = DataBase.getInstance().getExercises();
        for (Exercise exercise : exerciseList) {
            tableModel.addRow(exercise.getObjects());
        }
    }
}
