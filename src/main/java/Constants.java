/**
 * Constants class containing all application messages, configuration values, and error messages.
 * <p>
 * This class provides centralized access to all string literals and configuration constants
 * used throughout the betting application.
 * </p>
 * 
 * @IOC
 */
public class Constants {
    public static final String DEFAULT_FILE_NAME = "bets.txt";
    public static final String DEFAULT_DATA_DIRECTORY = "data";
    public static final String MESSAGE_ASK_FOLDER = "Nom de la carpeta on es vol guardar el fitxer de sortida (enter per ruta per defecte):";
    public static final String MESSAGE_ASK_FILE = "Nom del fitxer de sortida (enter per nom per defecte):";
    public static final String MESSAGE_ASK_OPTION_VALUE = "Introdueixi un valor enter per l'opció";
    public static final String MESSAGE_NOT_VALID_OPTION = "No s'ha introduït una opció vàlida";
    public static final String MESSAGE_ERROR_NO_INTEGER = "No s'ha introduït un número enter";
    public static final String MESSAGE_ERROR_NO_FLOAT = "No s'ha introduït un número amb decimals";
    public static final String MESSAGE_ERROR_EMPTY_STRING = "S'ha introduït un text buit";
    public static final String START_MENU = """
                                    1) Afegir una aposta.
                                    2) Veure el llistat d'apostes.
                                    3) Reiniciar l'arxiu de sortida.
                                    0) Sortir.
                                    """;
    public static final String DATE_FORMAT = "yyyyMMddHHmm";
}
