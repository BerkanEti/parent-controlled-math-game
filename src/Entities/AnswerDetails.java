package Entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AnswerDetails implements Serializable { // Cevap Detayları için class yapısı.
    private static final long serialVersionUID = 1L;
    private int questionNumber; // Bulunulan sorunun alıştırmadaki sırası
    private boolean isCorrect; // Doğru / Yanlış
    private LocalDateTime answeredDateTime; // Cevaplandığı zaman
    private int duration; // Cevaplanma süresi (sn)
    private int aValue; // ilk çarpan
    private int bValue; // ikinci çarpan
    private int submittedResult; // Kullanıcının verdiği cevap
    private int result; // Doğru cevap

    // Constructor
    public AnswerDetails(int questionNumber, boolean isCorrect, LocalDateTime answeredDateTime, int duration, int aValue, int bValue,int submittedResult) {
        this.questionNumber = questionNumber;
        this.isCorrect = isCorrect;
        this.answeredDateTime = answeredDateTime;
        this.duration = duration;
        this.aValue = aValue;
        this.bValue = bValue;
        this.submittedResult = submittedResult;
        result = aValue*bValue;

    }

    // JTable için gerekli değişkenleri obje dizisi olarak döndüren fonksiyon
    public  Object[] getDetailedObject() {
        // String[] detailedReportColumns = {"ID","1.ÇARPAN","2.ÇARPAN","KAYDEDİLEN CEVAP","DOĞRU CEVAP","DURUM","CEVAPLANMA SÜRESİ"};
        String status = (isCorrect) ? "DOGRU" : "YANLIS";
        Object[] data = {questionNumber,aValue,bValue,submittedResult,result,status,duration};
        return data;

    }

}
