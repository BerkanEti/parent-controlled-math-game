package FileOperations;

import Entities.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ChildOperations {

    // Yeni çocuk eklemek için fonksiyon
    public static boolean addChildren(Child aChild) throws URISyntaxException {
        if(aChild.getUsername().equals("admin")) // Alınan kullanıcı adı Yönetici kullanıcı adına ("admin")'e eşit olamaz.
            return false; // Eğer eşitse yeni çocuk sisteme eklenemez.

        ArrayList<Child> childList = DataBase.getInstance().getChilds(); // Sistemde var olan çocukları içeren ArrayList
        for (Child currentUser : childList) {
            if(currentUser.getUsername().equals(aChild.getUsername())) { // Kullanıcı adı, kayıtlı başka bir kullanıcınkiyle de aynı olamaz.
                return false; // Eğer eşitse yeni çocuk sisteme eklenemez.
            }
        }
        childList.add(aChild); // 2 durum dışındaki durumlarda yeni kullanıcı (aChild) sisteme eklenir.
        return true;
    }

    // Kayıtlı çocuk bilgilerini sistemden silmek için fonksiyon
    public static void deleteChild(String name, ChildOperations.DeleteChildCallBack callBack) throws URISyntaxException {
        ArrayList<Child> childList = DataBase.getInstance().getChilds(); // Sistemde var olan çocukları içeren ArrayList
        int i=0;
        boolean deleted = false; // deleted false olarak initialize edilir ve çocuk silinirse değiştirilir.
        while(i< childList.size() && !deleted) {
            if(childList.get(i).getUsername().equals(name)) {
                childList.remove(i);
                deleted = true;
            }
            i++;
        }

        callBack.onChildDeleted(); // GUI'de düzenleme için yardımcı fonksiyon
    }
    public interface DeleteChildCallBack {
        void onChildDeleted();
    }


    // child için ArrayList'de bulunan verileri güncelleyen fonksiyon
    // child, bir alıştırma yaptıktan sonra çalışır.
    public static void updateChild(Child child) throws URISyntaxException {
        ArrayList<Child> childList = DataBase.getInstance().getChilds(); // Sistemde var olan çocukları içeren ArrayList
        int i=0;
        boolean updated = false; // false olarak initialize edilir ve döngü içinde veri güncellendikten sonra değiştirilir
        while(i< childList.size() && !updated) {
            if(childList.get(i).getUsername().equals(child.getUsername())) { // Eğer gerekli indis bulunduysa...
                childList.set(i,child); // bulunulan indisteki veri yenisiyle değiştirilir.
                updated = true;
            }
            i++;
        }
    }

    // Kullanıcı adı / Şifre kontrolü
    public static boolean loginControl(String username , String password) throws URISyntaxException {
        ArrayList<Child> childList = DataBase.getInstance().getChilds(); // Sistemde var olan çocukları içeren ArrayList
        for (Child currentUser : childList) {
            // currentUser'ın kullanıcı adı / şifre verileri döngü içinde parametreler eşit olursa true döndürülür.
            if(currentUser.getUsername().equals(username) && currentUser.getPassword().equals(password)) {
                return true;
            }
        }

        // Döngü sonlandıysa false döndürülür.
        return false;
    }

    // Listedeki verileri dosyaya yazan fonksiyon
    public static void writeChildData(ArrayList<Child> childList) {
        try {
            ObjectOutputStream printer = new ObjectOutputStream(new FileOutputStream(getChildFilePath())); // dosya dizini
            printer.writeObject(childList.size()); // önce liste size'ı yazılır.
            for (Child currentChild : childList) {
                printer.writeObject(currentChild); // Her iterasyonda bir kullanıcı çocuğa ait veri dosyaya kaydedilir.
            }
            printer.close();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // dosya dizinini String olarak döndüren fonksiyon
    private static String getChildFilePath() throws URISyntaxException {
        String jarPath = AdminRegistration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String jarDirectory = new File(jarPath).getParent();
        return jarDirectory + File.separator + "childs.dat";
    }
}
