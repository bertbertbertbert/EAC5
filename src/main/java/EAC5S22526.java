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
        
        DataFileUtils dfu = new DataFileUtils(nomCarpeta, nomFitxer);

        int opcio;
        do {
            io.showMenu(Constants.START_MENU);
            opcio = io.askForInteger(Constants.MESSAGE_ASK_OPTION_VALUE, Constants.MESSAGE_NOT_VALID_OPTION);
            switch (opcio) {
                case 1:
                    String sport = io.askForNotEmptyString("Introdueixi el nom de l'esport", Constants.MESSAGE_ERROR_EMPTY_STRING);
                    String esdeveniment = io.askForNotEmptyString("Introdueixi el nom de l'esdeveniment", Constants.MESSAGE_ERROR_EMPTY_STRING);
                    String tipus = io.askForNotEmptyString("Introdueixi el tipus d'aposta", Constants.MESSAGE_ERROR_EMPTY_STRING);
                    float quota = io.askForFloat("Introdueixi les quotes de l'aposta", Constants.MESSAGE_ERROR_NO_FLOAT);
                    float importAposta = io.askForFloat("Introdueixi l'import de l'aposta", Constants.MESSAGE_ERROR_NO_FLOAT);
                    dfu.insertBetIntoDataFile(sport, esdeveniment, tipus, quota, importAposta);
                    break;
                case 2:
                	String dataFileRaw = dfu.getInfoFromDataFileIntoString();
                	io.showBets(dataFileRaw);
                    break;
                case 3:
                	dfu.getDataDirectoryPath();
                	dfu.getDataFilePath();
                	String resposta = io.askForNotEmptyString("Desitja esborrar i tornar a crear aquest arxiu? s/n" , Constants.MESSAGE_ERROR_EMPTY_STRING);
                	if(resposta.equals("si")) {
                		dfu.deleteDataFile();
                		dfu.createDataFile();
                		io.showInfo("Arxiu esborrat i creat de nou satisfactoriament");
                	}else {
                		io.showInfo("Cancel·lat a petició del usuari");
                	}
                    break;
                default:
                    if(opcio > 4 || opcio < 0){
                    io.showError(Constants.MESSAGE_NOT_VALID_OPTION);
                    }
            }
        } while (opcio != 0);
        io.showInfo("Has sortit.");
    }

}
