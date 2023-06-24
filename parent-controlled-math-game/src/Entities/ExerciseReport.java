package Entities;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;


public class ExerciseReport implements Serializable {
    private static final long serialVersionUID = 1L;
    private Exercise exercise; // Alıştırma raporunun ait olduğu alıştırma
    private LocalDateTime exerciseDateTime; // Alıştırmanın yapılış zamanı
    private Child exerciseOwner; // Alıştırma raporunun sahibi olan kullanıcı
    private ArrayList<AnswerDetails> answers; // Cevaplar ( Detaylı olarak AnswerDetails tipinde)
    private int score; // Toplam skor
    private int time; // Toplam süre


    // Constructor
    public ExerciseReport(Exercise exercise, LocalDateTime exerciseDateTime, Child exerciseOwner) {
        this.exercise = exercise;
        this.exerciseDateTime = exerciseDateTime;
        this.exerciseOwner = exerciseOwner;
    }


    /////////// Getter ve Setterlar  ///////////
    public Exercise getExercise() {
        return exercise;
    }

    public void setAnswers(ArrayList<AnswerDetails> answers) {
        this.answers = answers;
    }

    public ArrayList<AnswerDetails> getAnswers() {
        return answers;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Child getExerciseOwner() {
        return exerciseOwner;
    }

    //////////////////////////////////////////////


    // yıl-ay-gün:saat:dakika:saniye olarak tarih-zaman verisi dönüştürüşür.
    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd/HH:mm:ss");
        String formattedDateTime = exerciseDateTime.format(formatter);
        return formattedDateTime;
    }

    // Raporlama ekranı için gereken verileri döndüren fonksiyon
    public Object[] getReportObject() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd/HH:mm:ss");
        String formattedDateTime = exerciseDateTime.format(formatter); // Tarih formatlanır.
        // Sayı aralıkları String'e dönüştürülür.
        String intervalA = "[" + exercise.aMin + "," + exercise.aMax + "]";
        String intervalB = "[" + exercise.bMin + "," + exercise.bMax + "]";

        // String[] exerciseColumns = {"KULLANICI ADI","ALIŞTIRMA ADI","SORU SAYISI","ARALIK 1","ARALIK 2","BAŞLANGIÇ TARİHİ","SÜRE","SKOR"};
        Object[] data = {exerciseOwner.getUsername(),exercise.getExerciseName(),exercise.getNuOfQuestion(),intervalA,intervalB,formattedDateTime,time,score};
        return data;
    }
}
