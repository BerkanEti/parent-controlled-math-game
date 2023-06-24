package Entities;

import FileOperations.AdminRegistration;
import FileOperations.ChildOperations;
import FileOperations.ExerciseOperations;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class DataBase implements Serializable {
    private static final long serialVersionUID = 1L;
    private static DataBase single_instance = null; // Singleton yapısı için gerekli bir değişken. (Daha önce instance oluşturulmadıysa null'a eşittir.)
    private Admin admin; // Sisteme kayıtlı admin
    private ArrayList<Child> childs; // Sisteme kayıtlı çocuk(-lar)
    private ArrayList<Exercise> exercises; // Sisteme kayıtlı alıştırma(-lar)


    // Constructor
    private DataBase() throws URISyntaxException {
            // Çocuk ve alıştırmalar için ArrayList Yapıları oluşturulur.
            childs = new ArrayList<>();
            exercises = new ArrayList<>();

            // admin için veri okuması yapılır.
            // getAdminFilePath() : .jar'ın bulunduğu dosya dizininden "administrator.dat" için bir path (String) döndürür.
            File adminFile = new File(getAdminFilePath());
            if(adminFile.exists()) {
                try{
                    ObjectInputStream reader = new ObjectInputStream(new FileInputStream(getAdminFilePath()));
                    admin = (Admin)reader.readObject(); // Veri okunup admin değişkenine eşitlenir.
                }catch (IOException | ClassNotFoundException | URISyntaxException e ) {
                    e.printStackTrace();
                }
            }

            // Kayıtlı çocuk-lar için veri okuması yapılır. Daha önce dosya oluşturulmamış olabilir.
            // Bu yüzden önce .exists() ile beraber dosyanın var olup olmadığı kontrol edilir.
            // getChildFilePath() : .jar'ın bulunduğu dosya dizininden "childs.dat" için bir path (String) döndürür.
            File childFile = new File(getChildFilePath());
            if(childFile.exists()) { // Eğer dosya bulunuyor ise veri okunur.
                try{
                    ObjectInputStream reader = new ObjectInputStream(new FileInputStream(getChildFilePath()));
                    int counter = (int) reader.readObject(); // Önce eleman sayısı okunur.
                    for (int i = 0; i < counter; i++) { // Döngüyle beraber her iterasyonda bir çocuk verisi okunup listeye eklenir.
                        Child aChild = (Child) reader.readObject();
                        childs.add(aChild);
                    }

                }catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            // Kayıtlı alıştırma-lar için veri okuması yapılır. Daha önce dosya oluşturulmamış olabilir.
            // Bu yüzden önce .exists() ile beraber dosyanın var olup olmadığı kontrol edilir.
            // getExerciseFilePath() : .jar'ın bulunduğu dosya dizininden "exercises.dat" için bir path (String) döndürür.
            File exerciseFile = new File(getExerciseFilePath());
            if(exerciseFile.exists()) { // Eğer dosya bulunuyor ise veri okunur.
                try{
                    ObjectInputStream reader = new ObjectInputStream(new FileInputStream(getExerciseFilePath()));
                    int counter = (int) reader.readObject(); // Önce eleman sayısı okunur.
                    for (int i = 0; i < counter; i++) { // Döngüyle beraber her iterasyonda bir alıştırma verisi okunup listeye eklenir.
                        Exercise aExercise = (Exercise) reader.readObject();
                        exercises.add(aExercise);
                    }
                }catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            // Eğer daha önceden dosya oluşturulmamış ise listeye "Varsayılan Alıştırma" eklenir.
            else {
                exercises.add(new Exercise("Varsayilan Alistirma",1,10,1,10,10));
            }
    }

    // DataBase için instance oluşturulmak istenildiğinde çağırılan statik fonksiyon
    public static synchronized DataBase getInstance() throws URISyntaxException {
        if(single_instance == null) { // Daha önce oluşturulmadıysa Constructor çağırılır.
            single_instance = new DataBase();
        }
        return single_instance; // Daha önce oluşturulduysa hali hazırda var olan singe_instance döndürülür.
    }

    /////////// Getterlar  ///////////
    public Admin getAdmin() {
        return admin;
    }

    public ArrayList<Child> getChilds() {
        return childs;
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    /////////////////////////////////////////////


    // Alıştırma adına göre ArrayList'de gezinerek -eğer bulunduysa- alıştırmayı döndüren fonksiyon
    // Alıştırma bulunmuyor ise null döndürülür.
    public Exercise findExercise(String ExerciseName) {
        for(Exercise ex : exercises) {
            if(ex.getExerciseName().equals(ExerciseName)) {
                return ex;
            }
        }

        return null;
    }


    // Kullanıcı adına göre ArrayList'de gezinerek ilgili çocuğu arayan fonksiyon
    // Kullanıcı adı bulunamaz ise null döndürülür.
    public Child findChild(String username) {
        for(Child ch : childs) {
            if(ch.getUsername().equals(username)) {
                return ch;
            }
        }
        return null;
    }


    // Main'den çağrılır.
    // Program sonlanınca gerekli bütün verileri dosyalara yazar.
    public void writeAllData() {
        ChildOperations.writeChildData(childs); // Child class'ındaki verileri yazmak için fonksiyon
        ExerciseOperations.writeExerciseData(exercises); // Exercise class'ındaki verileri yazmak için fonksiyon
    }

    // "administrator.dat" için dosya yolunu String olarak döndüren fonksiyon
    private static String getAdminFilePath() throws URISyntaxException {
        String jarPath = AdminRegistration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String jarDirectory = new File(jarPath).getParent();
        return jarDirectory + File.separator + "administrator.dat";
    }


    // "childs.dat" için dosya yolunu String olarak döndüren fonksiyon
    private static String getChildFilePath() throws URISyntaxException {
        String jarPath = AdminRegistration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String jarDirectory = new File(jarPath).getParent();
        return jarDirectory + File.separator + "childs.dat";
    }

    // "exercises.dat" için dosya yolunu String olarak döndüren fonksiyon
    private static String getExerciseFilePath() throws URISyntaxException {
        String jarPath = AdminRegistration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String jarDirectory = new File(jarPath).getParent();
        return jarDirectory + File.separator + "exercises.dat";
    }
}
