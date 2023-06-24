package GUI;
import Entities.*;
import FileOperations.ChildOperations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class GamePlayScreen extends JFrame {

    private JPanel gamePlayPanel; // Ana panel
    private JButton nextButton; // İlerleme/Başlama butonu
    private JTextField resultField; // Sonucun alındığı textfield

    /////// Labellar ///////
    private JLabel aValue;
    private JLabel xLabel;
    private JLabel bValue;
    private JLabel timeCounter;

    /////////////////////////
    private boolean isStarted = false; // "Başla !"'ya basıldığı anda true olur.
    private Timer timer; // sürenin tutulduğu değişken
    private int minute = 0; // dk
    private int second = 0; // sn
    private int currentA; // İlk çarpan
    private int currentB; // İkinci çarpan
    private int currentResult; // Sonuç
    private int currentQuestion; // Bulunulan sorunun sırası
    private int lastDuration; // Son cevaplanma süresi
    private int currentScore; // Anlık skor

    public GamePlayScreen(Child child, Exercise exercise) {
        setTitle("Multiplication Game");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700,125);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // Pencere ortalanır.
        setLocation((int) ( d.getWidth() - this.getWidth()) / 2 ,(int) (d.getHeight() - this.getHeight()) / 2 );
        setContentPane(gamePlayPanel);

        ////////////// FONT DÜZENLEMELERİ //////////////
        aValue.setForeground(Color.RED);
        bValue.setForeground(Color.RED);
        timeCounter.setForeground(Color.RED);
        timeCounter.setFont(timeCounter.getFont().deriveFont(Font.BOLD, 16f));
        nextButton.setFont(nextButton.getFont().deriveFont(nextButton.getFont().getStyle() | Font.BOLD, nextButton.getFont().getSize() + 2));
        resultField.setFont(resultField.getFont().deriveFont(resultField.getFont().getStyle() | Font.BOLD));
        /////////////////////////////////////////////////

        // Başlama zamanı olarak anlık zaman alınır.
        LocalDateTime currentTime = LocalDateTime.now();
        // Yeni ArrayList oluşturulur ve cevap detayları tutulur.
        ArrayList<AnswerDetails> answerDetails = new ArrayList<AnswerDetails>();
        // Yeni alıştırma raporu oluşturulur.
        ExerciseReport exerciseReport = new ExerciseReport(exercise,currentTime,child);

        // Başla / İlerle butonuna basıldıysa...
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isStarted) { // Oyun henüz başlatılmadıysa
                    startTimer(); // timer başlatılır.
                    nextButton.setText("İlerle"); // Buton üzerindeki yazı güncellenir (Başlangıçta : "Oyuna Başla")
                    generateNumbers(exercise.getaMin(),exercise.getaMax(),exercise.getbMin(),exercise.getbMax());
                    isStarted = true; // isStarted güncellenir
                    currentQuestion = 1; // Soru sırası : 1
                    lastDuration = 0;
                    currentScore = 0;
                }
                // Cevaplanan soru son soru değil ise...
                else if(currentQuestion < exercise.getNuOfQuestion()){
                    // checkResult fonksiyonundan gelen veriler answerDetails'e kaydedilir.
                    answerDetails.add(checkResult(Integer.parseInt(resultField.getText())));
                    // Parametrelere göre yeni soru üretilir.
                    generateNumbers(exercise.getaMin(),exercise.getaMax(),exercise.getbMin(),exercise.getbMax());
                }

                // Son soru ise...
                else {
                    // checkResult fonksiyonundan gelen veriler answerDetails'e kaydedilir.
                    answerDetails.add(checkResult(Integer.parseInt(resultField.getText())));
                    // Oyun sırasında oluşan verilere göre alıştırma raporu ve çocuğun bilgileri güncellenir.
                    exerciseReport.setAnswers(answerDetails);
                    exerciseReport.setScore(currentScore);
                    exerciseReport.setTime(lastDuration);
                    child.addExerciseResult(exerciseReport);
                    child.addScore(exercise,currentScore);
                    child.addTime(exercise,lastDuration);
                    try {
                        ChildOperations.updateChild(child); // ArrayList'teki "child" verisi güncellenir.
                    } catch (URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                    // Bilgilendirme mesajı
                    JOptionPane.showMessageDialog(gamePlayPanel,"Oyun Sonlandı !\nSkor: " + currentScore +"/" + exercise.getNuOfQuestion() + "\n" + "Zaman(sn): " + lastDuration);
                    dispose(); // frame sonlanır.
                }
            }
        });
        setVisible(true);
    }
   private void startTimer() {
        // Timer oluşturulur.
        timer = new Timer();
        TimerTask task = new TimerTask() {
           @Override
           public void run() {
               second++;
               if (second == 60) {
                   minute++;
                   second = 0;
               }
               // Dakika:Saniye olarak formatlanır.
               String x = String.format("%02d:%02d",minute,second);
               timeCounter.setText(x);
           }
       };
        // saniyelik gecikme ayarlanır.
       timer.scheduleAtFixedRate(task, 1000, 1000);
   }

    private void generateNumbers(int aMin, int aMax, int bMin, int bMax) {
        // Sayı aralıklarına göre random olarak çarpanlar oluşturulur.
        currentA = ThreadLocalRandom.current().nextInt(aMin, aMax + 1);
        currentB = ThreadLocalRandom.current().nextInt(bMin, bMax + 1);
        // Doğru sonuç
        currentResult = GamePlayScreen.this.currentA*GamePlayScreen.this.currentB;
        // Labellar güncellenir.
        aValue.setText(Integer.toString(GamePlayScreen.this.currentA));
        bValue.setText(Integer.toString(GamePlayScreen.this.currentB));
        resultField.setText("");

    }

    private AnswerDetails checkResult(int submittedResult) {
        boolean isCorrect;
        LocalDateTime answeredDateTime = LocalDateTime.now(); // Cevap zamanı.
        int currentDuration = minute*60 + second - lastDuration; // Cevaplanma süresi
        // Eğer verilen cevap doğru ise currentScore arttırılır.
        if(submittedResult == currentResult){
            isCorrect = true;
            currentScore++;
        }
        // Yanlış ise...
        else
            isCorrect = false;

        // Cevap detayı için AnswerDetails tipi bir instance oluşturulur.
        AnswerDetails currentAnswer = new AnswerDetails(currentQuestion,isCorrect,answeredDateTime,currentDuration,currentA,currentB,submittedResult);
        lastDuration = currentDuration + lastDuration; // Toplam süre (Anlık süre + Son sorunun cevaplanma süresi)
        currentQuestion++; // Anlık soru sayısı

        return currentAnswer;
    }
}
