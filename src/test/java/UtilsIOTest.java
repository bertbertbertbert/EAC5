import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 *
 * @author ioc
 */
@DisplayName("UtilsIO")
public class UtilsIOTest {

    private final static String MESSAGE_ASK_ANYTHING = "Introdueixi alguna cosa";
    private final static String MESSAGE_ERROR = "Error en la entrada";
    private final static String MESSAGE_TEST_INPUT = "This message tests the input";
    private final static float FLOAT_TEST_INPUT = 6.3f;
    private final static int INT_TEST_INPUT = 6;   

    @BeforeAll
    public static void setUpClass() {
        Locale.setDefault(Locale.US);
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    @BeforeEach
    public void setUp() {
        provideInput("");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
        System.setIn(System.in);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private UtilsIO ioWithInput(String data) {
        provideInput(data);
        return new UtilsIO();
    }

    @Nested
    @DisplayName("showAnyMessage")
    class ShowAnyMessageTests {

        @Test
        @DisplayName("prints header and content")
        void printsHeaderAndContent() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            UtilsIO io = new UtilsIO();
            io.showAnyMessage("Test Header", "Test Content");

            String text = output.toString();
            assertTrue(text.contains("Test Header"));
            assertTrue(text.contains("Test Content"));
        }

        @Test
        @DisplayName("exception when header/content null or empty")
        void throwsOnNullOrEmpty() {
            UtilsIO io = new UtilsIO();
            assertThrows(IllegalArgumentException.class, () -> io.showAnyMessage(null, "content"));
            assertThrows(IllegalArgumentException.class, () -> io.showAnyMessage("", "content"));
            assertThrows(IllegalArgumentException.class, () -> io.showAnyMessage("header", null));
            assertThrows(IllegalArgumentException.class, () -> io.showAnyMessage("header", ""));
        }
    }

    @Nested
    @DisplayName("showMenu")
    class ShowMenuTests {

        @Test
        @DisplayName("prints menu content")
        void printsMenuContent() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            UtilsIO io = new UtilsIO();
            io.showMenu("Test Menu Content");

            assertTrue(output.toString().contains("Test Menu Content"));
        }

        @Test
        @DisplayName("exception on null/empty content")
        void throwsOnNullOrEmpty() {
            UtilsIO io = new UtilsIO();
            assertThrows(IllegalArgumentException.class, () -> io.showMenu(null));
            assertThrows(IllegalArgumentException.class, () -> io.showMenu(""));
        }
    }

    @Nested
    @DisplayName("showError")
    class ShowErrorTests {

        @Test
        @DisplayName("prints error content")
        void printsErrorContent() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            UtilsIO io = new UtilsIO();
            io.showError("Test Error Message");

            assertTrue(output.toString().contains("Test Error Message"));
        }

        @Test
        @DisplayName("exception on null/empty content")
        void throwsOnNullOrEmpty() {
            UtilsIO io = new UtilsIO();
            assertThrows(IllegalArgumentException.class, () -> io.showError(null));
            assertThrows(IllegalArgumentException.class, () -> io.showError(""));
        }
    }

    @Nested
    @DisplayName("showInfo")
    class ShowInfoTests {

        @Test
        @DisplayName("prints info content")
        void printsInfoContent() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            UtilsIO io = new UtilsIO();
            io.showInfo("Test Info Message");

            assertTrue(output.toString().contains("Test Info Message"));
        }

        @Test
        @DisplayName("exception on null/empty content")
        void throwsOnNullOrEmpty() {
            UtilsIO io = new UtilsIO();
            assertThrows(IllegalArgumentException.class, () -> io.showInfo(null));
            assertThrows(IllegalArgumentException.class, () -> io.showInfo(""));
        }
    }

    @Nested
    @DisplayName("askForAnyString / askForNotEmptyString")
    class AskStringTests {

        @Test
        @DisplayName("reads any string")
        void askForAnyString() {
            UtilsIO io = ioWithInput(MESSAGE_TEST_INPUT);
            assertEquals(MESSAGE_TEST_INPUT, io.askForAnyString(MESSAGE_ASK_ANYTHING));
        }

        @Test
        @DisplayName("reads non-empty string after empty retry")
        void askForNotEmptyStringRetry() {
            UtilsIO io = ioWithInput("\n" + MESSAGE_TEST_INPUT);
            assertEquals(MESSAGE_TEST_INPUT, io.askForNotEmptyString(MESSAGE_ASK_ANYTHING, MESSAGE_ERROR));
        }

        @Test
        @DisplayName("uses defaults when messages null or empty")
        void askForStringNullEmptyMessages() {
            assertEquals(MESSAGE_TEST_INPUT, ioWithInput(MESSAGE_TEST_INPUT).askForAnyString(null));
            assertEquals(MESSAGE_TEST_INPUT, ioWithInput(MESSAGE_TEST_INPUT).askForAnyString(""));

            UtilsIO io = ioWithInput("\n" + MESSAGE_TEST_INPUT);
            assertEquals(MESSAGE_TEST_INPUT, io.askForNotEmptyString(null, MESSAGE_ERROR));
            assertEquals(MESSAGE_TEST_INPUT, ioWithInput("\n" + MESSAGE_TEST_INPUT).askForNotEmptyString("", MESSAGE_ERROR));
            assertEquals(MESSAGE_TEST_INPUT, ioWithInput("\n" + MESSAGE_TEST_INPUT).askForNotEmptyString(MESSAGE_ASK_ANYTHING, null));
            assertEquals(MESSAGE_TEST_INPUT, ioWithInput("\n" + MESSAGE_TEST_INPUT).askForNotEmptyString(MESSAGE_ASK_ANYTHING, ""));
        }
    }

    @Nested
    @DisplayName("askForInteger")
    class AskIntegerTests {

        @Test
        @DisplayName("reads integer")
        void readsInteger() {
            UtilsIO io = ioWithInput(INT_TEST_INPUT + "\n");
            assertEquals(INT_TEST_INPUT, io.askForInteger(MESSAGE_ASK_ANYTHING, MESSAGE_ERROR));
        }

        @Test
        @DisplayName("retries on non-integer input")
        void retriesOnNonInteger() {
            UtilsIO io = ioWithInput(MESSAGE_TEST_INPUT + "\n" + INT_TEST_INPUT + "\n");
            assertEquals(INT_TEST_INPUT, io.askForInteger(MESSAGE_ASK_ANYTHING, MESSAGE_ERROR));

            UtilsIO ioFloat = ioWithInput(FLOAT_TEST_INPUT + "\n" + INT_TEST_INPUT + "\n");
            assertEquals(INT_TEST_INPUT, ioFloat.askForInteger(MESSAGE_ASK_ANYTHING, MESSAGE_ERROR));
        }

        @Test
        @DisplayName("uses defaults when messages null or empty")
        void integerMessagesNullOrEmpty() {
            assertEquals(INT_TEST_INPUT, ioWithInput(INT_TEST_INPUT + "\n").askForInteger(null, MESSAGE_ERROR));
            assertEquals(INT_TEST_INPUT, ioWithInput(INT_TEST_INPUT + "\n").askForInteger("", MESSAGE_ERROR));
            assertEquals(INT_TEST_INPUT, ioWithInput(INT_TEST_INPUT + "\n").askForInteger(MESSAGE_ASK_ANYTHING, null));
            assertEquals(INT_TEST_INPUT, ioWithInput(INT_TEST_INPUT + "\n").askForInteger(MESSAGE_ASK_ANYTHING, ""));
        }
    }

    @Nested
    @DisplayName("askForFloat")
    class AskFloatTests {

        @Test
        @DisplayName("reads float")
        void readsFloat() {
            UtilsIO io = ioWithInput(FLOAT_TEST_INPUT + "\n");
            assertEquals(FLOAT_TEST_INPUT, io.askForFloat(MESSAGE_ASK_ANYTHING, MESSAGE_ERROR));
        }

        @Test
        @DisplayName("retries on non-float input")
        void retriesOnNonFloat() {
            UtilsIO io = ioWithInput(MESSAGE_TEST_INPUT + "\n" + FLOAT_TEST_INPUT + "\n");
            assertEquals(FLOAT_TEST_INPUT, io.askForFloat(MESSAGE_ASK_ANYTHING, MESSAGE_ERROR));
        }

        @Test
        @DisplayName("accepts integer as float")
        void acceptsIntegerAsFloat() {
            UtilsIO io = ioWithInput(INT_TEST_INPUT + "\n");
            assertEquals((float) INT_TEST_INPUT, io.askForFloat(MESSAGE_ASK_ANYTHING, MESSAGE_ERROR));
        }

        @Test
        @DisplayName("uses defaults when messages null or empty")
        void floatMessagesNullOrEmpty() {
            assertEquals(FLOAT_TEST_INPUT, ioWithInput(FLOAT_TEST_INPUT + "\n").askForFloat(null, MESSAGE_ERROR));
            assertEquals(FLOAT_TEST_INPUT, ioWithInput(FLOAT_TEST_INPUT + "\n").askForFloat("", MESSAGE_ERROR));

            assertEquals(FLOAT_TEST_INPUT, ioWithInput(MESSAGE_TEST_INPUT + "\n" + FLOAT_TEST_INPUT + "\n").askForFloat(MESSAGE_ASK_ANYTHING, null));
            assertEquals(FLOAT_TEST_INPUT, ioWithInput(MESSAGE_TEST_INPUT + "\n" + FLOAT_TEST_INPUT + "\n").askForFloat(MESSAGE_ASK_ANYTHING, ""));
        }
    }

    @Nested
    @DisplayName("showBets")
    class ShowBetsTests {

        @Test
        @DisplayName("throws on null or empty input")
        void throwsOnNullOrEmpty() {
            UtilsIO io = new UtilsIO();
            assertThrows(IllegalArgumentException.class, () -> io.showBets(null));
            assertThrows(IllegalArgumentException.class, () -> io.showBets(""));
        }

        @Test
        @DisplayName("ignores malformed rows")
        void ignoresMalformedRows() {
            UtilsIO io = new UtilsIO();
            assertDoesNotThrow(() -> io.showBets("This is a random String without data"));
            assertDoesNotThrow(() -> io.showBets("DateTime1,Supermarket1,City1,1,1\nDateTime2,Supermarket2,City2,1,1\nDateTime3,Supermarket3,City3,1,1\nDateTime4,Supermarket4,1\n"));
            assertDoesNotThrow(() -> io.showBets("DateTime1,Supermarket1,City1,text_instead_of_float,1\nDateTime2,Supermarket2,City2,1,1\nDateTime3,Supermarket3,City3,1,1\nDateTime4,Supermarket4,1,1\n"));
        }

        @Test
        @DisplayName("prints valid rows")
        void printsValidRows() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            UtilsIO io = new UtilsIO();
            String validData = "20231201,John,Doe,12345678A,1.8,75.5\n20231202,Jane,Smith,87654321B,2.1,65.2";
            io.showBets(validData);

            String text = output.toString();
            assertTrue(text.contains("John"));
            assertTrue(text.contains("Jane"));
            assertTrue(text.contains("1.8"));
            assertTrue(text.contains("75.50"));
        }

        @Test
        @DisplayName("skips rows with bad odds")
        void skipsBadOdds() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            UtilsIO io = new UtilsIO();
            String mixedData = "20231201,John,Doe,12345678A,invalid_odds,75.5\n20231202,Jane,Smith,87654321B,2.0,65.2";
            io.showBets(mixedData);

            String text = output.toString();
            assertTrue(text.contains("Jane"));
            assertFalse(text.contains("John"));
        }

        @Test
        @DisplayName("skips rows with bad amount")
        void skipsBadAmount() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            UtilsIO io = new UtilsIO();
            String mixedData = "20231201,John,Doe,12345678A,1.8,invalid_amount\n20231202,Jane,Smith,87654321B,2.0,65.2";
            io.showBets(mixedData);

            String text = output.toString();
            assertTrue(text.contains("Jane"));
            assertFalse(text.contains("John"));
        }

        @Test
        @DisplayName("shows only valid rows when mixed")
        void showsOnlyValidMixed() {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            System.setOut(new PrintStream(output));

            UtilsIO io = new UtilsIO();
            String mixedData = "20231201,John,Doe,12345678A,1.8,75.5\n" +
                    "InvalidDataWithoutCommas\n" +
                    "20231202,Jane,Smith,87654321B,2.0,65.2\n" +
                    "20231203,Bob,Johnson,11111111C,invalid_odds,80.0\n" +
                    "20231204,Alice,Brown,22222222D,2.5,60.5";
            io.showBets(mixedData);

            String text = output.toString();
            assertTrue(text.contains("John"));
            assertTrue(text.contains("Jane"));
            assertTrue(text.contains("Alice"));
            assertFalse(text.contains("Bob"));
        }
    }
}