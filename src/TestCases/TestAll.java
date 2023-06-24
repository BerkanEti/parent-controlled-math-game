package TestCases;
import junit.framework.*;

public class TestAll {
    public static Test suite() {
        TestSuite suite = new TestSuite("Bütün Test Case'ler");
        suite.addTestSuite(TestChild.class);
        suite.addTestSuite(TestExercise.class);
        suite.addTestSuite(TestExerciseReport.class);
        suite.addTestSuite(TestDataBase.class);
        return suite;
    }
}
