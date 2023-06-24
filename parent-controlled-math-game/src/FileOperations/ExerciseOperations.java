package FileOperations;

import Entities.*;
import GUI.ExerciseOpScreen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class ExerciseOperations {


    // Yeni alıştırma eklemek için fonksiyon
    public static boolean addExercise(Exercise exercise) throws URISyntaxException {
        ArrayList<Exercise> exerciseList = DataBase.getInstance().getExercises(); // Sistemde kayıtlı alıştırmalar
        for(Exercise currentExercise : exerciseList) {
            // Eğer alıştırma adı daha önce kullanıldıysa yeni alıştırma eklenemez
            if(currentExercise.getExerciseName().equals(exercise.getExerciseName())) {
                return false;
            }
        }

        // Alıştırma adı daha önce alınmadıysa ekleme işlemi yapılır
        exerciseList.add(exercise);
        return true;
    }

    // Kayıtlı bir alıştırmayı silmek için fonksiyon
    public static void deleteExercise(String name, DeleteExerciseCallBack callBack) throws URISyntaxException {
        ArrayList<Exercise> exerciseList = DataBase.getInstance().getExercises(); // Sistemde kayıtlı alıştırmalar

        int i=0;
        boolean deleted = false; // false olarak initialize edilir ve alıştırma silindiği zaman değiştirilir
        while(i< exerciseList.size() && !deleted) {
            if(exerciseList.get(i).getExerciseName().equals(name)) {
                exerciseList.remove(i);
                deleted = true;
            }
            i++;
        }

        // Alıştırmayı daha önce yapmış olan çocuklara ait Alıştırma sonuçları ( ExerciseRepor) verileri de silinir.

        ArrayList<Child> childList = DataBase.getInstance().getChilds(); // Sisteme kayıtlı çocuklar
        for(Child tempChild : childList) {
            ArrayList<ExerciseReport> tempExerciseResults = tempChild.getExerciseResults(); // tempChild'ın alıştırma sonuçları
            ArrayList<ExerciseReport> exercisesToRemove = new ArrayList<>(); // Silinmesi gerek alıştırmalar -eğer varsa-
            HashMap<String,ArrayList<Integer>> tempScores = tempChild.getScores(); // tempChild'ın skorları
            HashMap<String,ArrayList<Integer>> tempTimes = tempChild.getTimes(); // tempChild'ın süreleri
            for(ExerciseReport tempExerciseResult : tempExerciseResults) {
                if(tempExerciseResult.getExercise().getExerciseName().equals(name)) {
                    exercisesToRemove.add(tempExerciseResult); // silinmesi gereken sonuçlar exercisesToRemove'a elenir.
                }
            }
            tempExerciseResults.removeAll(exercisesToRemove); // Silinmesi gereken veriler removeAll ile beraber topluca silinr.
            tempScores.remove(name); // Alıştırmaya dair kaydedilmiş skor bilgileri silinir.
            tempTimes.remove(name); // Alıştırmaya dair kaydedilmiş zaman verileri silinir.

        }


        callBack.onExerciseDeleted(); // GUI'de düzenleme için yardımcı fonksiyon

    }
    public interface DeleteExerciseCallBack {
        void onExerciseDeleted();
    }


    // Alıştırma verilerini dosyaya yazmak için fonksiyon
    public static void writeExerciseData(ArrayList<Exercise> exerciseList) {
        try{
            ObjectOutputStream printer = new ObjectOutputStream(new FileOutputStream(getExerciseFilePath())); // dosya dizini
            printer.writeObject(exerciseList.size()); // İlk olarak Kayıtlı alıştırmaların size'ı yazılır
            for(Exercise currentExercise : exerciseList) {
                printer.writeObject(currentExercise); // Her iterasyonda ArrayList'deki bir exercise dosyaya yazılır
            }
            printer.close();
        }catch (IOException e ) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    // Dosya dizinini String olarak döndüren fonksiyon
    private static String getExerciseFilePath() throws URISyntaxException {
        String jarPath = AdminRegistration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        String jarDirectory = new File(jarPath).getParent();
        return jarDirectory + File.separator + "exercises.dat";
    }
}
