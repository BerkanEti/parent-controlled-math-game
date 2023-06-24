package TestCases;

import Entities.*;
import junit.framework.TestCase;

public class TestExercise extends TestCase {
    private Exercise testExercise;
    private Child aChild;

    protected void setUp() {
        testExercise = new Exercise("testExerciseName",1,11,2,22,15);
        aChild = new Child("berkanEti","123456");
    }

    public void testGetExerciseName() {
        String exerciseName = testExercise.getExerciseName();
        assertEquals("testExerciseName",exerciseName);
    }

    public void testGetAValues() {
        int aMin = testExercise.getaMin();
        int aMax = testExercise.getaMax();
        assertEquals(1,aMin);
        assertEquals(11,aMax);
    }

    public void testBValues() {
        int bMin = testExercise.getbMin();
        int bMax = testExercise.getbMax();
        assertEquals(2,bMin);
        assertEquals(22,bMax);
    }

    public void testGetNuOfQuestion() {
        int num = testExercise.getNuOfQuestion();
        assertEquals(15,num);
    }

    public void testGetObjects() {
        Object[] objects = testExercise.getObjects();
        assertEquals(4, objects.length);
        assertEquals("testExerciseName", objects[0]);
        assertEquals(15, objects[1]);
        assertEquals("[1,11]", objects[2]);
        assertEquals("[2,22]", objects[3]);
    }

    public void testGetGameObjects() {
        aChild.addScore(testExercise,9);
        aChild.addScore(testExercise,13);
        aChild.addTime(testExercise,70);
        aChild.addTime(testExercise,90);

        Object[] gameObjects = testExercise.getGameObjects(aChild);
        assertEquals(6, gameObjects.length);
        assertEquals("testExerciseName", gameObjects[0]);
        assertEquals(15, gameObjects[1]);
        assertEquals("[1,11]", gameObjects[2]);
        assertEquals("[2,22]", gameObjects[3]);
        assertEquals(13, gameObjects[4]);
        assertEquals("01:10", gameObjects[5]);
    }

}
