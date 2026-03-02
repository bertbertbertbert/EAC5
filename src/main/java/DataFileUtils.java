import java.io.File;
import java.io.IOException;

/**
 * Utility class for handling bet data files and their containing directories.
 * <p>
 * This class manages creation, deletion, and data insertion in text files that
 * store bet information.
 * It also provides methods for retrieving file contents and checking file
 * existence.
 * </p>
 * 
 * <p>
 * The class assumes that each bet record contains timestamp, sport, event, bet
 * type, odds, and amount.
 * </p>
 * 
 * @IOC
 */
public class DataFileUtils {

    String dataDirectoryName;
    String dataFileName;

    /**
     * Constructs a DataFileUtils instance with the given directory and file name.
     * Throws IllegalArgumentException if any of the parameters are null or empty.
     *
     * @param dataDirectoryName Name of the data folder
     * @param dataFileName      Name of the file to read/write bet data
     * @throws IllegalArgumentException if dataDirectoryName or dataFileName is null
     *                                  or empty
     * @throws RuntimeException         if directory or file creation fails
     */
    public DataFileUtils(String dataDirectoryName, String dataFileName) {
        // checking if any of the parameters are empty or null
        if (dataDirectoryName == null || dataDirectoryName.isEmpty() || dataFileName == null
                || dataFileName.isEmpty()) {
            throw new IllegalArgumentException(Constants.MESSAGE_ERROR_EMPTY_STRING);
        }
        // if any both are correct their value are given to the class variables
        this.dataDirectoryName = dataDirectoryName;
        this.dataFileName = dataFileName;
        if (!dataDirectoryExists()) {
            createDataDirectory();
        }
        if (!dataFileExists()) {
            createDataFile();
        }
    }

    /**
     * Creates the data directory if it does not already exist.
     *
     * @throws RuntimeException if directory creation fails
     */
    public void createDataDirectory() {
        File dataDirectory = new File(this.dataDirectoryName);
        try {
            if (!dataDirectory.exists()) {
                if (!dataDirectory.mkdirs()) {
                    throw new RuntimeException("No se pudo crear el directorio: " + dataDirectoryName);
                }
            }
        } catch (RuntimeException re) {

            System.err.println("Error interno: " + re.getMessage());
        }
    }

    /**
     * Creates the data file if it does not already exist.
     *
     * @throws RuntimeException if file creation fails
     */
    public void createDataFile() {
        File file = new File(this.dataDirectoryName, this.dataFileName);
        try {
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new RuntimeException("No se pudo crear el archivo: " + dataFileName);
                }
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Returns the absolute path of the data directory.
     *
     * @return full path of the data directory
     */
    public String getDataDirectoryPath() {
        File path = new File(this.dataDirectoryName);
        return path.getAbsolutePath();
    }

    /**
     * Returns the absolute path of the data file.
     *
     * @return full path of the data file
     */
    public String getDataFilePath() {
        File filePath = new File(this.dataDirectoryName, this.dataFileName);
        return filePath.getAbsolutePath();
    }

    /**
     * Checks if the data directory exists.
     *
     * @return true if the directory exists; false otherwise
     */
    public boolean dataDirectoryExists() {
        File dataDirectory = new File(this.dataDirectoryName);
        return dataDirectory.exists();
    }

    /**
     * Checks if the data file exists.
     *
     * @return true if the file exists; false otherwise
     */
    public boolean dataFileExists() {
        File file = new File(this.dataDirectoryName, this.dataFileName);
        return file.exists();
    }

    /**
     * Deletes the data directory if it exists and it's empty.
     * 
     * @throws RuntimeException if directory deletion fails
     */
    public void deleteDataFolderIfEmpty() {
      File dataDirectory = new File(this.dataDirectoryName);
      
      if(dataDirectory.exists()){
        String[] directoryList = dataDirectory.list();
        if(directoryList.length == 0){
            dataDirectory.delete();
            if(dataDirectory.exists()){
                throw new RuntimeException("La carpeta está buïda però no s'ha pogut esborrar");
            }

        }
      }
    }

    /**
     * Deletes the data file if it exists.
     *
     * @throws RuntimeException if file deletion fails
     */
    public void deleteDataFile() {
       File dataFile = new File(this.dataFileName);
       if(dataFile.exists()){
        dataFile.delete();
        if(dataFile.exists()){
            throw new RuntimeException("El ftixer existeix pero no s'ha pogut esborrar");
        }
       }
    }

    /**
     * Reads and returns the full contents of data file as a String.
     *
     * @return a string containing all the content from the file
     * @throws IllegalStateException if the data file does not exist
     * @throws RuntimeException      if an I/O error occurs while reading the file
     */
    public String getInfoFromDataFileIntoString() {
        String info = "";
        return info;
    }

    /**
     * Inserts a string into the data file.
     * <p>
     * The method validates the input content and appends it to the file with a
     * newline.
     * </p>
     *
     * @param content the string content to insert (must not be null or empty)
     * @return true if the content was successfully inserted
     * @throws IllegalArgumentException if content is null or empty
     * @throws IllegalStateException    if the data file does not exist
     * @throws RuntimeException         if an I/O error occurs while writing to the
     *                                  file
     */
    public boolean insertStringIntoDataFile(String content) {
        return false;
    }

    /**
     * Inserts a new bet record into the data file, prepending a timestamp.
     * <p>
     * The method validates input parameters and writes the bet data in CSV format.
     * </p>
     *
     * @param sport   the sport name (must not be null or empty)
     * @param event   the event name (must not be null or empty)
     * @param betType the type of bet (must not be null or empty)
     * @param odds    the betting odds (must be positive)
     * @param amount  the bet amount (must be positive)
     * @return true if the bet was successfully inserted
     * @throws IllegalArgumentException if any parameter is null, empty, or invalid
     * @throws IllegalStateException    if the data file does not exist
     * @throws RuntimeException         if an I/O error occurs while writing to the
     *                                  file
     */
    public boolean insertBetIntoDataFile(String sport, String event, String betType, float odds,
            float amount) {
        return false;
    }

}