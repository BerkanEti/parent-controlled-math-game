package FileOperations;
import Entities.*;

import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AdminRegistration {

    // isRegistered() Main'de çağırılır. Eğer daha önce admistrator.dat oluşturulmuşsa True ,
    // Oluşturulmamışsa False döndürülür.
    public static boolean isRegistered() throws URISyntaxException {
        String filePath = getFilePath();
        File file = new File(filePath);
        return file.exists();
    }

    // Admin classı için verileri dosyaya yazan fonksiyon
    public  static void writeAdminData(char[] password) throws URISyntaxException, IOException {
        String filePath = getFilePath(); // Dosya dizini
        File file = new File(filePath);
        file.createNewFile(); // Yeni dosya oluşturulur.
        // (!) Eğer "administrator.dat" bulunuyor ise bu fonksiyon çalışmaz. Yani dosyanın var olmadığı kesindir.

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(String.valueOf(password));; // Admin classı için kullanıcıdan alınan şifre set edilir.

        // Veriler dosyaya yazılır.
        // Tek bir yönetici bulunduğu için kaydedilmesi gereken counter bilgisi bulunmamaktadır.
        try{
            ObjectOutputStream printer = new ObjectOutputStream(new FileOutputStream(filePath));
            printer.writeObject(admin);
            printer.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gerekli dosya dizinini String olarak döndürür.
    private static String getFilePath() throws URISyntaxException {
        String jarPath = AdminRegistration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String jarDirectory = new File(jarPath).getParent();
        return jarDirectory + File.separator + "administrator.dat";
    }
}