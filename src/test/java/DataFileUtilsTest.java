import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
class DataFileUtilsTest {

    private String directoryName;
    private String fileName;

    @BeforeEach
    void setup() {
        directoryName = "datafileutils-test-" + UUID.randomUUID();
        fileName = "bets-test.txt";
    }

    @AfterEach
    void cleanup() throws IOException {
        deleteRecursively(dirPath());
    }

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        @DisplayName("throws when directory name is null")
        void constructorRejectsNullDirectory() {
            assertThrows(IllegalArgumentException.class, () -> new DataFileUtils(null, fileName));
        }

        @Test
        @DisplayName("throws when file name is null")
        void constructorRejectsNullFile() {
            assertThrows(IllegalArgumentException.class, () -> new DataFileUtils(directoryName, null));
        }

        @Test
        @DisplayName("throws when names are empty")
        void constructorRejectsEmptyValues() {
            assertThrows(IllegalArgumentException.class, () -> new DataFileUtils("", ""));
        }

        @Test
        @DisplayName("creates directory and file when valid names provided")
        void constructorCreatesArtifacts() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertTrue(Files.exists(dirPath()));
            assertTrue(Files.exists(filePath()));
            assertTrue(utils.dataDirectoryExists());
            assertTrue(utils.dataFileExists());
        }
    }

    @Nested
    @DisplayName("Directory management")
    class DirectoryTests {

        @Test
        @DisplayName("createDataDirectory is idempotent")
        void createDataDirectoryIdempotent() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertTrue(Files.exists(dirPath()));
            assertDoesNotThrow(utils::createDataDirectory);
            assertTrue(Files.exists(dirPath()));
        }

        @Test
        @DisplayName("createDataDirectory recreates removed directory")
        void createDataDirectoryAfterRemoval() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            deleteRecursively(dirPath());

            assertFalse(Files.exists(dirPath()));
            assertDoesNotThrow(utils::createDataDirectory);
            assertTrue(Files.exists(dirPath()));
            assertFalse(Files.exists(filePath()));
        }

        @Test
        @DisplayName("deleteDataFolder removes empty directory")
        void deleteDataFolderSuccess() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertDoesNotThrow(utils::deleteDataFile);
            assertDoesNotThrow(utils::deleteDataFolderIfEmpty);
            assertFalse(Files.exists(dirPath()));
        }

        @Test
        @DisplayName("deleteDataFolder no-ops when directory missing")
        void deleteDataFolderWhenMissing() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            deleteRecursively(dirPath());

            assertDoesNotThrow(utils::deleteDataFolderIfEmpty);
            assertFalse(Files.exists(dirPath()));
        }


    }

    @Nested
    @DisplayName("File management")
    class FileTests {

        @Test
        @DisplayName("createDataFile recreates missing file")
        void createDataFileCreatesWhenMissing() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            assertTrue(Files.exists(filePath()));
            Files.deleteIfExists(filePath());
            assertFalse(Files.exists(filePath()));
            assertDoesNotThrow(utils::createDataFile);
            assertTrue(Files.exists(filePath()));
        }

        @Test
        @DisplayName("createDataFile is safe when file exists")
        void createDataFileWhenAlreadyExists() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertTrue(Files.exists(filePath()));
            assertDoesNotThrow(utils::createDataFile);
            assertTrue(Files.exists(filePath()));
        }

        @Test
        @DisplayName("deleteDataFile does nothing when file absent")
        void deleteDataFileWhenMissing() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            Files.deleteIfExists(filePath());

            assertDoesNotThrow(utils::deleteDataFile);
            assertFalse(Files.exists(filePath()));
        }
    }

        @Test
        @DisplayName("deleteDataFile removes existing file")
        void deleteDataFileSuccess() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertTrue(Files.exists(filePath()));
            assertDoesNotThrow(utils::deleteDataFile);
            assertFalse(Files.exists(filePath()));
        }
        
    @Nested
    @DisplayName("Reading")
    class ExtractBetsTests {

        @Test
        @DisplayName("returns empty string for empty file")
        void extractBetsEmptyFile() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertDoesNotThrow(() -> {
                String content = utils.getInfoFromDataFileIntoString();
                assertEquals("", content.trim());
            });
        }

        @Test
        @DisplayName("throws IllegalStateException when file missing")
        void extractBetsMissingFile() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            Files.deleteIfExists(filePath());

            assertThrows(IllegalStateException.class, utils::getInfoFromDataFileIntoString);
        }

        @Test
        @DisplayName("reads full content with trailing newline")
        void extractBetsWithContent() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            String expected = "Line 1\nLine 2\nLine 3";
            Files.writeString(filePath(), expected);

            String content = utils.getInfoFromDataFileIntoString();
            assertNotNull(content);
            assertTrue(content.endsWith("\n"));
            assertEquals(expected, content.trim());
        }
    }

    @Nested
    @DisplayName("Writing String Content")
    class InsertStringTests {

        @Test
        @DisplayName("inserts non-empty string successfully")
        void insertStringSuccess() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            String content = "Test content line";

            assertTrue(utils.insertStringIntoDataFile(content));

            String fileContent = Files.readString(filePath()).trim();
            assertEquals(content, fileContent);
        }

        @Test
        @DisplayName("appends multiple strings with newlines")
        void insertStringAppends() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertTrue(utils.insertStringIntoDataFile("First line"));
            assertTrue(utils.insertStringIntoDataFile("Second line"));
            assertTrue(utils.insertStringIntoDataFile("Third line"));

            String content = Files.readString(filePath());
            String[] lines = content.trim().split("\n");
            assertEquals(3, lines.length);
            assertEquals("First line", lines[0]);
            assertEquals("Second line", lines[1]);
            assertEquals("Third line", lines[2]);
        }

        @Test
        @DisplayName("throws IllegalArgumentException when content is null")
        void insertStringNullContent() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertThrows(IllegalArgumentException.class, () ->
                utils.insertStringIntoDataFile(null));
        }

        @Test
        @DisplayName("throws IllegalArgumentException when content is empty")
        void insertStringEmptyContent() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertThrows(IllegalArgumentException.class, () ->
                utils.insertStringIntoDataFile(""));
        }

        @Test
        @DisplayName("throws IllegalStateException when file missing")
        void insertStringMissingFile() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            Files.deleteIfExists(filePath());

            assertThrows(IllegalStateException.class, () ->
                utils.insertStringIntoDataFile("Some content"));
        }

        @Test
        @DisplayName("handles multi-line content correctly")
        void insertStringMultiLine() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            String multiLineContent = "Line 1\nLine 2\nLine 3";

            assertTrue(utils.insertStringIntoDataFile(multiLineContent));

            String fileContent = Files.readString(filePath());
            assertTrue(fileContent.contains("Line 1\nLine 2\nLine 3"));
        }

        @Test
        @DisplayName("handles special characters")
        void insertStringSpecialCharacters() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            String specialContent = "Special chars: @#$%^&*()";

            assertTrue(utils.insertStringIntoDataFile(specialContent));

            String fileContent = Files.readString(filePath()).trim();
            assertEquals(specialContent, fileContent);
        }

        @Test
        @EnabledOnOs({OS.MAC, OS.LINUX})
        @DisplayName("insertStringIntoDataFile throws RuntimeException on IOException (POSIX)")
        void insertStringThrowsOnIOException_posix() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            // Guardem permisos actuals i els restablim al final
            Set<PosixFilePermission> originalPerms = Files.getPosixFilePermissions(filePath());
            try {
                // Make file read-only (POSIX) to trigger write failure
                Files.setPosixFilePermissions(filePath(), PosixFilePermissions.fromString("r--r--r--"));

                assertThrows(RuntimeException.class, () ->
                        utils.insertStringIntoDataFile("A sample line"));
            } finally {
                Files.setPosixFilePermissions(filePath(), originalPerms);
            }
        }

        @Test
        @EnabledOnOs(OS.WINDOWS)
        @DisplayName("insertStringIntoDataFile throws RuntimeException on IOException (Windows read-only)")
        void insertStringThrowsOnIOException_windows() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            DosFileAttributeView dosView =
                    Files.getFileAttributeView(filePath(), DosFileAttributeView.class);

            // Si el FS no suporta DOS attrs, el test no té sentit
            assertNotNull(dosView, "DOS file attributes not supported on this filesystem");

            try {
                // Make file read-only (DOS) to trigger write failure
                dosView.setReadOnly(true);

                assertThrows(RuntimeException.class, () ->
                        utils.insertStringIntoDataFile("A sample line"));
            } finally {
                dosView.setReadOnly(false);
            }
        }
    }

    @Nested
    @DisplayName("Writing")
    class InsertBetTests {

        @Test
        @DisplayName("appends bet with timestamp")
        void insertBetSuccess() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertDoesNotThrow(() -> utils.insertBetIntoDataFile("Football", "TeamA vs TeamB", "WIN", 1.8f, 75.5f));

            String[] parts = firstLineFields(utils);
            assertEquals("Football", parts[1]);
            assertEquals("TeamA vs TeamB", parts[2]);
            assertEquals("WIN", parts[3]);
            assertEquals("1.8", parts[4]);
            assertEquals("75.5", parts[5]);
        }

        @Test
        @DisplayName("timestamp matches DATE_FORMAT length and digits-only")
        void insertBetTimestampFormat() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertDoesNotThrow(() -> utils.insertBetIntoDataFile("Football", "Match", "WIN", 1.5f, 10f));

            String[] parts = firstLineFields(utils);
            String ts = parts[0];
            assertEquals(Constants.DATE_FORMAT.length(), ts.length());
            assertTrue(ts.matches("\\d+"));
        }

        @Test
        @DisplayName("throws IllegalArgumentException on invalid parameters")
        void insertBetInvalidParameters() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertThrows(IllegalArgumentException.class, () ->
                utils.insertBetIntoDataFile(null, "Event", "WIN", 1.8f, 10f));
            assertThrows(IllegalArgumentException.class, () ->
                utils.insertBetIntoDataFile("Football", "", "WIN", 1.8f, 10f));
            assertThrows(IllegalArgumentException.class, () ->
                utils.insertBetIntoDataFile("Football", "Event", "", 1.8f, 10f));
            assertThrows(IllegalArgumentException.class, () ->
                utils.insertBetIntoDataFile("Football", "Event", "WIN", 0f, 10f));
            assertThrows(IllegalArgumentException.class, () ->
                utils.insertBetIntoDataFile("Football", "Event", "WIN", 1.8f, 0f));
        }

        @Test
        @DisplayName("throws IllegalStateException when file missing")
        void insertBetMissingFile() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            Files.deleteIfExists(filePath());

            assertThrows(IllegalStateException.class, () ->
                    utils.insertBetIntoDataFile("Football", "Event", "WIN", 1.8f, 10f));
        }

        @Test
        @DisplayName("multiple bets append")
        void insertBetAppends() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            assertDoesNotThrow(() -> {
                utils.insertBetIntoDataFile("Football", "Match 1", "WIN", 1.9f, 25.0f);
                utils.insertBetIntoDataFile("Basketball", "Game 2", "OVER", 2.1f, 40.0f);
            });

            String content = utils.getInfoFromDataFileIntoString();
            assertNotNull(content);
            String[] lines = content.trim().split("\n");
            assertEquals(2, lines.length);
        }
    }

    @Nested
    @DisplayName("Flags and Paths")
    class FlagsAndPathsTests {

        @Test
        @DisplayName("dataDirectoryExists true then false after deletion")
        void dataDirectoryExistsStates() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            assertTrue(utils.dataDirectoryExists());
            deleteRecursively(dirPath());
            assertFalse(utils.dataDirectoryExists());
        }

        @Test
        @DisplayName("dataFileExists true then false after deletion")
        void dataFileExistsStates() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            assertTrue(utils.dataFileExists());
            Files.deleteIfExists(filePath());
            assertFalse(utils.dataFileExists());
        }

        @Test
        @DisplayName("getDataDirectoryPath and getDataFilePath return expected paths")
        void pathGettersReturnExpected() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            String expectedDir = Paths.get(System.getProperty("user.dir"), directoryName).toString();
            String expectedFile = Paths.get(System.getProperty("user.dir"), directoryName, fileName).toString();
            assertEquals(expectedDir, utils.getDataDirectoryPath());
            assertEquals(expectedFile, utils.getDataFilePath());
        }
    }

    @Nested
    @DisplayName("Delete non-empty directory")
    class DeleteNonEmptyDirectoryTests {
        @Test
        @DisplayName("deleteDataFolder throws when directory not empty")
        void deleteDataFolderThrowsOnNonEmpty() {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);
            assertTrue(Files.exists(filePath())); // ensure non-empty
            assertThrows(RuntimeException.class, utils::deleteDataFolderIfEmpty);
        }
    }

    @Nested
    @DisplayName("Write failure handling")
    class WriteFailureTests {
        @Test
        @EnabledOnOs({OS.MAC, OS.LINUX})
        @DisplayName("insertBetIntoDataFile throws RuntimeException on IOException (POSIX)")
        void insertBetThrowsOnIOException_posix() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            // Guardem permisos actuals i els restablim al final
            Set<PosixFilePermission> originalPerms = Files.getPosixFilePermissions(filePath());
            try {
                // Make file read-only (POSIX) to trigger write failure
                Files.setPosixFilePermissions(filePath(), PosixFilePermissions.fromString("r--r--r--"));

                assertThrows(RuntimeException.class, () ->
                        utils.insertBetIntoDataFile("Football", "Match", "WIN", 1.5f, 10f));
            } finally {
                Files.setPosixFilePermissions(filePath(), originalPerms);
            }
        }

        @Test
        @EnabledOnOs(OS.WINDOWS)
        @DisplayName("insertBetIntoDataFile throws RuntimeException on IOException (Windows read-only)")
        void insertBetThrowsOnIOException_windows() throws IOException {
            DataFileUtils utils = new DataFileUtils(directoryName, fileName);

            DosFileAttributeView dosView =
                    Files.getFileAttributeView(filePath(), DosFileAttributeView.class);

            // Per seguretat: si el FS no suporta DOS attrs, el test no té sentit
            assertNotNull(dosView, "DOS file attributes not supported on this filesystem");

            try {
                // Make file read-only (DOS) to trigger write failure
                dosView.setReadOnly(true);

                assertThrows(RuntimeException.class, () ->
                        utils.insertBetIntoDataFile("Football", "Match", "WIN", 1.5f, 10f));
            } finally {
                dosView.setReadOnly(false);
            }
        }
    }

    private Path dirPath() {
        return Paths.get(System.getProperty("user.dir"), directoryName);
    }

    private Path filePath() {
        return dirPath().resolve(fileName);
    }

    private void deleteRecursively(Path root) throws IOException {
        if (root == null || !Files.exists(root)) {
            return;
        }
        Files.walk(root)
                .sorted((a, b) -> b.compareTo(a))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
    }

    private String[] firstLineFields(DataFileUtils utils) {
        String content = utils.getInfoFromDataFileIntoString();
        assertNotNull(content);
        String firstLine = content.trim().split("\n")[0];
        return firstLine.split(",");
    }
}
