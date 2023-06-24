package Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Child implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username; // kullanıcı adı
    private String password; // şifre
    private ArrayList<ExerciseReport> exerciseResults; // Çocuğun daha önce yaptığı alıştırmaların raporları
    private HashMap<String,ArrayList<Integer>> scores;  // Yapılan alıştırmaların skorları (alıştırma adı -> skorlar)
    private HashMap<String,ArrayList<Integer>> times; // Yapılan alıştırmaların bitirilme süreleri (alıştırma adı -> süreler)


    // Constructor
    public Child(String username, String password) {
        scores = new HashMap<String,ArrayList<Integer>>();
        times = new HashMap<String,ArrayList<Integer>>();
        exerciseResults = new ArrayList<ExerciseReport>();
        this.username = username;
        this.password = password;
    }

    /////////// Getter ve Setterlar  ///////////
    public HashMap<String, ArrayList<Integer>> getScores() {
        return scores;
    }

    public HashMap<String, ArrayList<Integer>> getTimes() {
        return times;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<ExerciseReport> getExerciseResults() {
        return exerciseResults;
    }
    /////////////////////////////////////////////

    // alıştırma sonlandığında alıştırma raporunu kaydetmek için fonksiyon
    public void addExerciseResult(ExerciseReport exerciseReport) {
        exerciseResults.add(exerciseReport);
    }


    // Skoru HashMap'e ekleyen (Var olan ArrayList'e veya yeni bir key oluşturarak) fonksiyon
    public void addScore(Exercise exercise, int score) {
        String exerciseName = exercise.getExerciseName();
        ArrayList<Integer> exerciseScores = scores.getOrDefault(exerciseName, new ArrayList<>());
        exerciseScores.add(score);
        scores.put(exerciseName, exerciseScores);
    }

    // Bitirme süresini HashMap'e ekleyen (Var olan ArrayList'e veya yeni bir key oluşturarak) fonksiyon
    public void addTime(Exercise exercise, int time) {
        String exerciseName = exercise.getExerciseName();
        ArrayList<Integer> exerciseTimes = times.getOrDefault(exerciseName, new ArrayList<>());
        exerciseTimes.add(time);
        times.put(exerciseName, exerciseTimes);
    }


    // JTable için gerekli değişkenleri obje dizisi olarak döndüren fonksiyon
    public Object[] getObjects() {
        Object[] data = {username,password};
        return data;
    }
}
