import java.util.Locale;

/**
 * Main class that controls the application's execution flow.
 * <p>
 * This program interacts with users through a menu system to manage
 * bet data stored in files. It supports inserting, displaying,
 * and resetting bet data files.
 * </p>
 * 
 * <p>
 * The data input/output operations are handled by utility classes.
 * </p>
 * 
 * @IOC
 */
public class EAC5S22526 {

    /**
     * Entry point of the program.
     * <p>
     * It creates an instance of the class and starts the interaction loop
     * after setting the default locale to US.
     * </p>
     * 
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        EAC5S22526 program = new EAC5S22526();
        Locale.setDefault(Locale.US);
        program.start();
    }

    /**
     * Starts the main program loop, handling menu options and user interaction.
     * <p>
     * Prompts the user for the data directory and file name, then continuously
     * displays a menu and executes selected operations until the user exits.
     * </p>
     */
    public void start() {
        UtilsIO io = new UtilsIO();
        String nomCarpeta = io.askForAnyString(Constants.MESSAGE_ASK_FOLDER);
        if (nomCarpeta == null || nomCarpeta.isEmpty()) {
            nomCarpeta = Constants.DEFAULT_DATA_DIRECTORY;
        }

        String nomFitxer = io.askForAnyString(Constants.MESSAGE_ASK_FILE);
        if (nomFitxer == null || nomFitxer.isEmpty()) {
            nomFitxer = Constants.DEFAULT_FILE_NAME;
        }
        
        new DataFileUtils(nomCarpeta, nomFitxer);

        int opcio;
        do {
            io.showMenu(Constants.START_MENU);
            opcio = io.askForInteger(Constants.MESSAGE_ASK_OPTION_VALUE, Constants.MESSAGE_NOT_VALID_OPTION);
            switch (opcio) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    if(opcio !=0){
                    io.showError(Constants.MESSAGE_NOT_VALID_OPTION);
                    }
            }
        } while (opcio != 0);
        io.showInfo("Has sortit.");
    }

}
