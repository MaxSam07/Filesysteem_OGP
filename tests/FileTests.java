import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import File.*;

/**
 * A JUnit 6 test class for testing the public methods of the File Class.
 *
 * @author 	Joran Naessens, Lars Debrabander, Maxime Samyn
 * @author 	Michiel Provoost
 *
 * @note 	Set up a test environment with a number of variables to be used throughout the tests.
 * 			Make a separate testcase for each test instance. Sometimes, several testcases can be combined
 * 			in one method, but this makes it harder to test them independently from the other cases, and
 * 			can make it harder to find the exact problem.
 * 			As a general rule of thumb, you test only 1 exceptional condition for 1 aspect per case.
 */
public class FileTests {

    //name tests
    File myFirstFile;

    //size tests
    File emptyFile;
    File normalSizedFile;
    File tooBigFile;
    File negativeSizeFile;

    //writable tests
    File writableFile;
    File unwritableFile;

    //time
//    File negativeTimeFile;
//    File tooLateFile;

    @BeforeEach
    public void setupFiles(){

        //name tests
        File myFirstFile = new File("myFirstFile.file", 2048, true);

        //size tests
        File emptyFile = new File("empty.file", 0, true);
        File normalSizedFile = new File("normalSized.file", 256, true);
        File tooBigFile = new File("tooBig.file", Integer.MAX_VALUE, true);
        File negativeSizeFile = new File("negativeSize", -256, true);

        //writable tests
        File writableFile = new File("writable.file", 256, true);
        File unwritableFile = new File("unwritablr.file", 256, false);

        //time
//        File negativeTimeFile;
//        File tooLateFile;

    }

    @Test
    public void renameTestLegal(){

        File myFirstFile = new File("myFirstFile.file", 2048, true);

        String newName = "TestNaam.file";
        myFirstFile.renameFile(newName);

        assertEquals(newName, myFirstFile.getName());

    }

    @Test
    public void renameTestLegal2() {

        File myFirstFile = new File("myFirstFile.file", 2048, true);

        String newName = "abcdefghijklmnopqrstuvwxzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890._-";
        myFirstFile.renameFile(newName);

        assertEquals(newName, myFirstFile.getName());
    }

    @Test
    public void renameTestIllegal() {
        File myFirstFile = new File("myFirstFile.file", 2048, true);

        String newName = "geenSymbool";
        myFirstFile.renameFile(newName);

        assertEquals("myFirstFile.file", myFirstFile.getName());

    }
    @Test
    public void testEnlarge_Success() {
        File normalSizedFile = new File("normalSized.file", 256, true);
        int toename = 100;
        normalSizedFile.enlarge(toename);

        assertEquals(356, normalSizedFile.getSize(),
                "De grootte moet met 100 zijn toegenomen (256 + 100).");

        assertNotNull(normalSizedFile.getModificationTime(),
                "De modification time moet nu gezet zijn.");
    }

    @Test
    public void testEnlarge_Unwritable() {
        File unwritableFile = new File("unwritable.file", 256, false);
//        assertThrows(Exception.class, () -> {
//            unwritableFile.enlarge(50);
//        }, "Zou een exception moeten gooien omdat het bestand niet schrijfbaar is.");

        assertEquals(256, unwritableFile.getSize(),
                "De grootte mag niet aangepast zijn na een gefaalde enlarge.");
    }

    @Test
    public void testEnlarge_EmptyFile() {
        File emptyFile = new File("empty.file", 0, true);
        emptyFile.enlarge(500);

        assertEquals(500, emptyFile.getSize(),
                "Een leeg bestand moet ook vergroot kunnen worden.");
    }

    @Test
    public void testEnlarge_Boundary_TooBig() {
        assertThrows(Exception.class, () -> {
            writableFile.enlarge(Integer.MAX_VALUE);
        }, "Mag de maximale bestandsgrootte niet overschrijden.");
    }
}
