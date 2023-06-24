package TestCases;

import Entities.*;
import junit.framework.TestCase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


public class TestChild extends TestCase {
    private Child testChild;
    private Exercise exercise;
    private ExerciseReport exerciseReport;


    protected void setUp()  {
        testChild = new Child("testUsername","testPassword");
        exercise = new Exercise("testExercise",1,10,5,15,10);
        exerciseReport = new ExerciseReport(exercise,LocalDateTime.now(),testChild);
    }

    public void testGetUsername() {
        String username = testChild.getUsername();
        assertEquals("testUsername",username);
    }

    public void testGetPassword() {
        String password = testChild.getPassword();
        assertEquals("testPassword",password);
    }

    public void testGetExerciseResults() {
        ArrayList<ExerciseReport> exerciseResults = testChild.getExerciseResults();
        assertNotNull(exerciseResults);
        assertTrue(exerciseResults.isEmpty());
    }

    public void testAddExerciseResult() {
        testChild.addExerciseResult(exerciseReport);
        ArrayList<ExerciseReport> exerciseResults = testChild.getExerciseResults();
        assertEquals(1, exerciseResults.size());
        assertEquals(exerciseReport, exerciseResults.get(0));
    }

    public void testAddScore() {
        testChild.addScore(exercise,10);
        HashMap<String, ArrayList<Integer>> scores = testChild.getScores();
        assertEquals(1, scores.size());

        ArrayList<Integer> exerciseScores = scores.get(exercise.getExerciseName());
        assertNotNull(exerciseScores);
        assertEquals(1, exerciseScores.size());
        assertEquals(10,exerciseScores.get(0).intValue());
    }

    public void testAddTime() {
        testChild.addTime(exercise,77);
        HashMap<String,ArrayList<Integer>> times = testChild.getTimes();
        assertEquals(1,times.size());

        ArrayList<Integer> exerciseTimes = times.get(exercise.getExerciseName());
        assertNotNull(exerciseTimes);
        assertEquals(1,exerciseTimes.size());
        assertEquals(77,exerciseTimes.get(0).intValue());
    }

    public void testGetObjects() {
        Object[] objects = testChild.getObjects();
        assertEquals(2,objects.length);
        assertEquals("testUsername",objects[0]);
        assertEquals("testPassword",objects[1]);
    }
}
