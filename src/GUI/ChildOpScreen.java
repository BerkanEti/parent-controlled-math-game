package GUI;

import Entities.DataBase;
import Entities.Child;
import FileOperations.ChildOperations;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ChildOpScreen extends JFrame{
    private JPanel mainChildPanel; // Ana panel
    private JPanel childTablePanel; // JTable için panel
    private JPanel childOperationPanel; // Butonlar için panel
    private JButton additionButton; // Ekleme işlemi için buton
    private JButton deletionButton; // Silme işlemi için buton
    private JTable childTable; // Listeleme için JTable

    public ChildOpScreen() throws URISyntaxException {
        setTitle("Child Operations");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900,700);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalanır
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );
        setContentPane(mainChildPanel);

        createChildList(); // JTable oluşturulur ve listelenir.
        childAddition(); // Çocuk ekleme işlemi
        childDeletion(); // Silme işlemi
        setVisible(true);

    }

    public void createChildList() throws URISyntaxException {
        ArrayList<Child> childList = DataBase.getInstance().getChilds();
        String childColumns[] = {"KULLANICI ADI","ŞİFRE"}; // Sütun isimleri
        DefaultTableModel childModel = new DefaultTableModel(childColumns,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Table hücreleri değiştirilemez.
            }
        };

        childTable = new JTable(childModel);
        JScrollPane childScrollPane = new JScrollPane(childTable);
        childTablePanel.setLayout(new BorderLayout());
        childTablePanel.add(childScrollPane,BorderLayout.CENTER);
        for(Child list : childList) {
            childModel.addRow(list.getObjects()); // childList üzerinde gezinerek objeler table'a eklenir.
        }

    }
    public void childAddition() {
        // Butona basılıdığı zaman ekleme işlemi için yeni ekran (ChildRegistrationScreen) açılır.
        additionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChildRegistrationScreen crs = new ChildRegistrationScreen();
                // Ekran kapandığı anda table yeniden güncellenir.
                crs.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        try {
                            updateChildTable();
                        } catch (URISyntaxException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        });
    }

    public void childDeletion() {
        // Butona basıldığı zaman seçilen satıra göre silme işlemi gerçekleşir.
        // Silme işlemi için ChildOperations.deleteChild kullanılır.
        deletionButton.addActionListener(e -> {
            int selectedRow = childTable.getSelectedRow(); // Seçilen satır
            if(selectedRow != -1) {
                // Butona basıldığı zaman kullanıcıdan onay istenir.
                int result = JOptionPane.showConfirmDialog(childTable, "Kullanıcıyı silmek istediğinize emin misiniz?", "Alıştırma Silme", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    String username = (String) childTable.getValueAt(selectedRow, 0); // kullanıcı adı
                    try {
                        ChildOperations.deleteChild(username, () -> {
                            SwingUtilities.invokeLater(() -> {
                                try {
                                    updateChildTable(); // Table güncellenir.
                                } catch (URISyntaxException ex) {
                                    throw new RuntimeException(ex);
                                }
                            });
                        });
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(mainChildPanel, "Kullanıcı Başarıyla Silindi!");
                }
            }
        });
    }

    // childList'de değişiklik olduğu zaman çalışır ve içindeki verileri JTable'da tekrar listeler.
    public void updateChildTable() throws URISyntaxException {
        DefaultTableModel tableModel = (DefaultTableModel) childTable.getModel();
        tableModel.setRowCount(0);

        ArrayList<Child> childList = DataBase.getInstance().getChilds();
        for (Child child : childList) {
            tableModel.addRow(child.getObjects());
        }
    }
}
