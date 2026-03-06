
import java.util.ArrayList;
import java.util.Locale;

/**
 * Main class that controls the application's execution flow.
 * <p>
 * This program interacts with users through a menu system to manage bet data
 * stored in files. It supports inserting, displaying, and resetting bet data
 * files.
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
     * It creates an instance of the class and starts the interaction loop after
     * setting the default locale to US.
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
                    ArrayList data = enterBetData(dfu, io);
                    try {
                        dfu.insertBetIntoDataFile((String) data.get(0), (String) data.get(1), (String) data.get(2), (float) data.get(3), (float) data.get(4));
                    } catch (IllegalStateException | IllegalArgumentException e) {
                        io.showError(e.getMessage());
                    } catch (RuntimeException e) {
                        io.showError(e.getMessage());
                    }
                    break;
                case 2:
                    try {
                        String dataFileRaw = dfu.getInfoFromDataFileIntoString();
                        showBetsData(io, dataFileRaw);
                    } catch (IllegalArgumentException | IllegalStateException e) {
                        io.showError(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println(dfu.getDataDirectoryPath());
                    System.out.println(dfu.getDataFilePath() + "\n");

                    String resposta = io.askForNotEmptyString("Desitja esborrar i tornar a crear aquest arxiu? s/n", Constants.MESSAGE_ERROR_EMPTY_STRING);
                    if (resposta.toLowerCase().equals("s")) {
                        try {
                            resetDataFileIfConfirmed(dfu, io);
                        } catch (RuntimeException e) {
                            io.showError(e.getMessage());
                        }
                    } else {
                        io.showInfo("Cancel·lat a petició del usuari");
                    }
                    break;
                default:
                    if (opcio > 4 || opcio < 0) {
                        io.showError(Constants.MESSAGE_NOT_VALID_OPTION);
                    }
            }
        } while (opcio != 0);
        io.showInfo("Has sortit.");
    }

    public ArrayList<Object> enterBetData(DataFileUtils dfu, UtilsIO io) {
        ArrayList<Object> data = new ArrayList<>();
        String sport = io.askForNotEmptyString("Introdueixi el nom de l'esport", Constants.MESSAGE_ERROR_EMPTY_STRING);
        data.add(sport);
        String esdeveniment = io.askForNotEmptyString("Introdueixi el nom de l'esdeveniment", Constants.MESSAGE_ERROR_EMPTY_STRING);
        data.add(esdeveniment);
        String tipus = io.askForNotEmptyString("Introdueixi el tipus d'aposta", Constants.MESSAGE_ERROR_EMPTY_STRING);
        data.add(tipus);
        float quota = io.askForFloat("Introdueixi les quotes de l'aposta", Constants.MESSAGE_ERROR_NO_FLOAT);
        data.add(quota);
        float importAposta = io.askForFloat("Introdueixi l'import de l'aposta", Constants.MESSAGE_ERROR_NO_FLOAT);
        data.add(importAposta);
        return data;
    }

    public void showBetsData(UtilsIO io, String dataFileRaw) {
        io.showBets(dataFileRaw);
    }

    public void resetDataFileIfConfirmed(DataFileUtils dfu, UtilsIO io) {
        dfu.deleteDataFile();
        dfu.createDataFile();
        io.showInfo("Arxiu esborrat i creat de nou satisfactoriament");
    }
}
