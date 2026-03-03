
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility class for handling bet data files and their containing directories.
 * <p>
 * This class manages creation, deletion, and data insertion in text files that
 * store bet information. It also provides methods for retrieving file contents
 * and checking file existence.
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

    private String dataDirectoryName;
    private String dataFileName;
    private File carpeta;
    private File fitxer;

    /**
     * Constructs a DataFileUtils instance with the given directory and file
     * name. Throws IllegalArgumentException if any of the parameters are null
     * or empty.
     *
     * @param dataDirectoryName Name of the data folder
     * @param dataFileName Name of the file to read/write bet data
     * @throws IllegalArgumentException if dataDirectoryName or dataFileName is
     * null or empty
     * @throws RuntimeException if directory or file creation fails
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
        carpeta = new File(this.dataDirectoryName);
        fitxer = new File(this.dataDirectoryName, this.dataFileName);
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
        try {
            if (!carpeta.exists()) {
                if (!carpeta.mkdirs()) {
                    throw new RuntimeException("No se pudo crear el directorio: " + carpeta);
                }
            }
        } catch (RuntimeException re) {
            System.err.println("Error interno: " + re);
        }
    }

    /**
     * Creates the data file if it does not already exist.
     *
     * @throws RuntimeException if file creation fails
     */
    public void createDataFile() {
        try {
            if (!fitxer.exists()) {
                if (!fitxer.createNewFile()) {
                    throw new RuntimeException("No se pudo crear el archivo: " + fitxer);
                }
            }
        } catch (IOException | RuntimeException e) {
            System.err.println("Error: " + e);
        }
    }

    /**
     * Returns the absolute path of the data directory.
     *
     * @return full path of the data directory
     */
    public String getDataDirectoryPath() {
        return carpeta.getAbsolutePath();
    }

    /**
     * Returns the absolute path of the data file.
     *
     * @return full path of the data file
     */
    public String getDataFilePath() {
        return fitxer.getAbsolutePath();
    }

    /**
     * Checks if the data directory exists.
     *
     * @return true if the directory exists; false otherwise
     */
    public boolean dataDirectoryExists() {
        return carpeta.exists();
    }

    /**
     * Checks if the data file exists.
     *
     * @return true if the file exists; false otherwise
     */
    public boolean dataFileExists() {
        return fitxer.exists();
    }

    /**
     * Deletes the data directory if it exists and it's empty.
     *
     * @throws RuntimeException if directory deletion fails
     */
    public void deleteDataFolderIfEmpty() {
        if (dataDirectoryExists()) {
            String[] directoryList = carpeta.list();
            if (directoryList.length == 0) {
                carpeta.delete();
                if (carpeta.exists()) {
                    throw new RuntimeException("La carpeta està buïda però no s'ha pogut esborrar");
                }

            }else{
                throw new RuntimeException("La carpeta no està buïda");
            }
        }
    }

    /**
     * Deletes the data file if it exists.
     *
     * @throws RuntimeException if file deletion fails
     */
    public void deleteDataFile() {
        if (dataFileExists()) {
            fitxer.delete();
            if (fitxer.exists()) {
                throw new RuntimeException("El fitxer existeix pero no s'ha pogut esborrar");
            }
        }
    }

    /**
     * Reads and returns the full contents of data file as a String.
     *
     * @return a string containing all the content from the file
     * @throws IllegalStateException if the data file does not exist
     * @throws RuntimeException if an I/O error occurs while reading the file
     */
    public String getInfoFromDataFileIntoString() {
        if (!dataFileExists()) {
            throw new IllegalStateException("El fitxer no existeix");
        }
        StringBuilder stringARetornar = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fitxer))) {
            String linia;
            while ((linia = br.readLine()) != null) {
                stringARetornar.append(linia).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error llegint el fitxer", e);
        }
        return stringARetornar.toString();
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
     * @throws IllegalStateException if the data file does not exist
     * @throws RuntimeException if an I/O error occurs while writing to the file
     */
    public boolean insertStringIntoDataFile(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException(Constants.MESSAGE_ERROR_EMPTY_STRING);
        }
        if (!dataFileExists()) {
            throw new IllegalStateException("El fitxer no existeix");
        }
        try (BufferedWriter wr = new BufferedWriter(new FileWriter(fitxer, true))) {
            wr.append(content);
            wr.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Error escribint al fitxer", e);
        }
        return true;
    }

    /**
     * Inserts a new bet record into the data file, prepending a timestamp.
     * <p>
     * The method validates input parameters and writes the bet data in CSV
     * format.
     * </p>
     *
     * @param sport the sport name (must not be null or empty)
     * @param event the event name (must not be null or empty)
     * @param betType the type of bet (must not be null or empty)
     * @param odds the betting odds (must be positive)
     * @param amount the bet amount (must be positive)
     * @return true if the bet was successfully inserted
     * @throws IllegalArgumentException if any parameter is null, empty, or
     * invalid
     * @throws IllegalStateException if the data file does not exist
     * @throws RuntimeException if an I/O error occurs while writing to the file
     */
    public boolean insertBetIntoDataFile(String sport, String event, String betType, float odds,
            float amount) {
        if (!dataFileExists()) {
            throw new IllegalStateException("El fitxer no existeix");
        }
        if ((sport == null || sport.isEmpty()) || (event == null || event.isEmpty()) || (betType == null || betType.isEmpty())) {
            throw new IllegalArgumentException(Constants.MESSAGE_ERROR_EMPTY_STRING);
        }
        if(odds <= 0 || amount <= ) {
        	throw new IllegalArgumentException(Constants.MESSAGE_ERROR_EMPTY_STRING);
        }
       String content = sport+","+event+","+betType+","+odds+","+","+amount;
       if(!insertStringIntoDataFile(content)) {
    	   throw new RuntimeException("Error escribint al fitxer", e);
       }else {
    	   return false;
       }
       
    }

}
