package TestCases;

import Entities.*;
import FileOperations.AdminRegistration;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


public class TestDataBase extends TestCase {
    private DataBase database;
    private boolean isRegisteredBefore;


    protected void setUp() throws URISyntaxException, IOException {
        isRegisteredBefore = true;
        if(!AdminRegistration.isRegistered()) { // Program daha önce çalıştırıldıysa yeni admin dosyası oluşturulmaz.
            // Çalıştırılmadıysa oluşturulur ve test sonlandığında tearDown() fonksiyonu ile siler.
            isRegisteredBefore = false;
            AdminRegistration.writeAdminData("tester".toCharArray());
        }
        database = DataBase.getInstance();
    }

    protected void tearDown() throws URISyntaxException {
        if(!isRegisteredBefore) {
            String jarPath = AdminRegistration.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            String jarDirectory = new File(jarPath).getParent();
            String filePath = jarDirectory + File.separator + "administrator.dat";
            File testFile = new File(filePath);
            testFile.delete();
        }
    }

    public void testGetInstance() {
        assertNotNull(database);
    }

    public void testAdminInitialization() {
        assertNotNull(database.getAdmin());
    }

    public void testChildsInitialization() {
        assertNotNull(database.getChilds());
    }

    public void testExercisesInitialization() {
        assertNotNull(database.getExercises());
    }


    public void testFindExercise() {
        Exercise exercise = new Exercise("exercise 1", 6, 16, 3, 9, 12);
        database.getExercises().add(exercise);

        Exercise foundExercise = database.findExercise("exercise 1");
        assertNotNull(foundExercise);
        assertEquals(exercise, foundExercise);

        Exercise notFoundExercise = database.findExercise("Nonexistent exercise");
        assertNull(notFoundExercise);
    }

    public void testFindChild() {
        Child child = new Child("testerberkan", "test123");
        database.getChilds().add(child);

        Child foundChild = database.findChild("testerberkan");
        assertNotNull(foundChild);
        assertEquals(child, foundChild);

        Child notFoundChild = database.findChild("Nonexistent child");
        assertNull(notFoundChild);
    }

}
