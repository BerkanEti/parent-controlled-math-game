package Entities;

import java.io.Serializable;
import java.util.*;

public class Exercise implements Serializable {
    private static final long serialVersionUID = 1L;
    private String exerciseName; // Alıştırma adı
    int aMin; // İlk çarpanın alabileceği min değer
    int aMax; // İlk çarpanın alabileceği max değer
    int bMin; // İkinci çarpanın alabileceği min değer
    int bMax; // İkinci çarpanın alabileceği max değer
    int nuOfQuestion; // Soru sayısı

    // Constructor
    public Exercise(String exerciseName, int aMin, int aMax, int bMin, int bMax, int nuOfQuestion) {
        this.exerciseName = exerciseName;
        this.aMin = aMin;
        this.aMax = aMax;
        this.bMin = bMin;
        this.bMax = bMax;
        this.nuOfQuestion = nuOfQuestion;
    }

    /////////// Getterlar ///////////
    public String getExerciseName() {
        return exerciseName;
    }

    public int getNuOfQuestion() {
        return nuOfQuestion;
    }

    public int getaMin() {
        return aMin;
    }

    public int getaMax() {
        return aMax;
    }

    public int getbMin() {
        return bMin;
    }

    public int getbMax() {
        return bMax;
    }

    /////////////////////////////////////

    // Alıştırma görüntüleme ekranındaki JTable için gerekli verileri döndüren fonksiyon
    public Object[] getObjects() {
        // Çarpan değerleri okunur biçimde aralık olarak String formatında kaydedilir.
        String intervalA = "[" + aMin + "," + aMax + "]";
        String intervalB = "[" + bMin + "," + bMax + "]";
        Object[] data = {exerciseName,nuOfQuestion,intervalA,intervalB};
        return data;
    }

    // Oyun ekranındaki veriler için gerekli verileri döndüren fonksiyon
    public Object[] getGameObjects(Child child) { // child : anlık olarak oyunu oynayacak kullanıcı
        HashMap<String, ArrayList<Integer>> scores = child.getScores(); // Kullanıcının önceki skorları
        HashMap<String,ArrayList<Integer>> times = child.getTimes(); // Kullanıının önceki bitirme süreleri
        int bestScore = 0;
        int bestTime = 0;
        if(scores.containsKey(exerciseName)) {
            bestScore = Collections.max(scores.get(exerciseName)); // alıştırma için en iyi skor
        }
        if(times.containsKey(exerciseName)) {
            bestTime = Collections.min(times.get(exerciseName)); // alıştırma için en iyi süre
        }
        String bestTimeEdited = String.format("%02d:%02d",bestTime/60,bestTime%60); // en iyi süre dakika:saniye formatına dönüştürüşür.

        Object[] exerciseData = getObjects();
        Object[] data = Arrays.copyOf(exerciseData,exerciseData.length + 2);
        data[data.length-2] = bestScore;
        data[data.length-1] = bestTimeEdited;
        return data;

    }
}
