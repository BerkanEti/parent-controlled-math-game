import Entities.DataBase;
import FileOperations.AdminRegistration;
import GUI.LoginScreen;
import GUI.WelcomeScreen;

import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        // Eğer daha önce program açılmamışsa ve yönetici için bir şifre bilgisi alınmamışsa bu koşula girer.
        if(AdminRegistration.isRegistered()) {
            LoginScreen ls = new LoginScreen(); // LoginScreen çalışır ve yönetici (Admin) oluşturulur.
        }
        else {
            WelcomeScreen ws = new WelcomeScreen(); // Eğer daha önceden şifre alınmışsa doğrudan giriş ekranına yönlenilir.
        }

        // Program kapatıldığında çalışır..
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    DataBase.getInstance().writeAllData(); // Bütün veriler dosyalara kaydedilir.
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}