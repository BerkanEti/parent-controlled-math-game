package Entities;
import java.io.Serializable;

public class Admin implements Serializable{
    private static final long serialVersionUID = 1L;
    private String username; // kullanıcı adı
    private String password; // şifre

    public void setUsername(String username) {
        this.username = username;
    } // kullanıcı adı için setter

    public String getPassword() {
        return password;
    } // şifre için getter/setter

    public void setPassword(String password) {
        this.password = password;
    }


}
