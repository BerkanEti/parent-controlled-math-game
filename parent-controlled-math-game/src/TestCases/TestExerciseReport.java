package TestCases;

import Entities.*;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TestExerciseReport extends TestCase {
    private ExerciseReport testExerciseReport;
    private Exercise exercise;
    private Child aChild;
    private LocalDateTime exerciseDateTime;

    protected void setUp() {
        exercise = new Exercise("exercise 1",1,5,3,7,20);
        aChild = new Child("etiberkan","1223334444");
        exerciseDateTime = LocalDateTime.now();
        testExerciseReport = new ExerciseReport(exercise,exerciseDateTime,aChild);
    }

    public void testGetExercise() {
        Exercise exercise1 = testExerciseReport.getExercise();
        assertEquals("exercise 1",exercise1.getExerciseName());
    }

    public void testGetAnswers() {
        ArrayList<AnswerDetails> answerDetails = new ArrayList<>();
        testExerciseReport.setAnswers(answerDetails);
        assertEquals(answerDetails,testExerciseReport.getAnswers());
    }

    public void testGetExerciseOwner() {
        Child exerciseOwner = testExerciseReport.getExerciseOwner();
        assertEquals("etiberkan", exerciseOwner.getUsername());
    }

    public void testGetFormattedDateTime() {
        String formatted = exerciseDateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd/HH:mm:ss"));
        assertEquals(formatted,testExerciseReport.getFormattedDateTime());
    }

    public void testGetReportObjects() {
        testExerciseReport.setTime(150);
        testExerciseReport.setScore(14);
        Object[] reportObject = testExerciseReport.getReportObject();
        assertEquals(8, reportObject.length);
        assertEquals("etiberkan", reportObject[0]);
        assertEquals("exercise 1", reportObject[1]);
        assertEquals(20, reportObject[2]);
        assertEquals("[1,5]", reportObject[3]);
        assertEquals("[3,7]", reportObject[4]);
        assertNotNull(reportObject[5]);
        assertEquals(150, reportObject[6]);
        assertEquals(14, reportObject[7]);

    }
}
