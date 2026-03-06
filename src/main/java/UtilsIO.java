
import java.util.Scanner;

/**
 * Utility class for handling user input and output in the console.
 * <p>
 * This class provides methods for displaying messages, errors, menus, and
 * asking for various types of input such as strings, integers, and floats. It
 * also supports formatted display of bet lists.
 * </p>
 *
 * <p>
 * It relies on predefined constants to standardize messages and layout.
 * </p>
 *
 * @IOC
 */
public class UtilsIO {

    /**
     * Constructs a UtilsIO object and initializes the input scanner.
     */
    Scanner scan;

    public UtilsIO() {
        scan = new Scanner(System.in);
    }

    /**
     * Displays a formatted message with a header and body content. Throws
     * IllegalArgumentException if either part is null or empty.
     *
     * @param header the title or heading of the message
     * @param mainText the main body of the message
     * @throws IllegalArgumentException if header or mainText is null or empty
     */
    public void showAnyMessage(String header, String mainText) {
    
            if (header == null || mainText == null || header.isEmpty() || mainText.isEmpty()) {
                throw new IllegalArgumentException(Constants.MESSAGE_ERROR_EMPTY_STRING);
            } else {
                System.out.println(
                        "---------------------------------------------------------------------------------------"
                        + "\n"
                        + header + "\n"
                        + "---------------------------------------------------------------------------------------"
                        + "\n"
                        + mainText);
            }
        } 

    /**
     * Displays a formatted menu using a default menu header.
     *
     * @param menuText the content of the menu to display
     * @throws IllegalArgumentException if menuText is null or empty
     */
    public void showMenu(String menuText) {
        
            if (menuText == null || menuText.isEmpty()) {
                throw new IllegalArgumentException(Constants.MESSAGE_ERROR_EMPTY_STRING);
            } else {
                System.out.println(
                        "---------------------------------------------------------------------------------------"
                        + "\n"
                        + "BET IOC!" + "\n"
                        + "---------------------------------------------------------------------------------------"
                        + "\n"
                        + menuText);
            }

    }

    /**
     * Displays a formatted error message using a default error header.
     *
     * @param errorText the content of the error message
     * @throws IllegalArgumentException if errorText is null or empty
     */
    public void showError(String errorText) {
      
            if (errorText == null || errorText.isEmpty()) {
                throw new IllegalArgumentException(Constants.MESSAGE_ERROR_EMPTY_STRING);
            } else {
                System.out.println(
                        "---------------------------------------------------------------------------------------"
                        + "\n"
                        + "ERROR" + "\n"
                        + "---------------------------------------------------------------------------------------"
                        + "\n"
                        + errorText
                        + "\n");
            }

    }

    /**
     * Displays an informational message using a default info header.
     *
     * @param infoText the content of the info message
     * @throws IllegalArgumentException if infoText is null or empty
     */
    public void showInfo(String infoText) {
      
            if (infoText == null || infoText.isEmpty()) {
                throw new IllegalArgumentException(Constants.MESSAGE_ERROR_EMPTY_STRING);
            } else {
                System.out.println(
                        "-----------------------------------------------------------------------------------------"
                        + "\n"
                        + "INFO" + "\n"
                        + "---------------------------------------------------------------------------------------"
                        + "\n"
                        + infoText
                        + "\n");
            }


    }

    /**
     * Prompts the user for any string input and returns it.
     *
     * @param message the prompt shown to the user. If not provided, a default
     * prompt is used.
     * @return the user’s input as a string
     */
    public String askForAnyString(String message) {
        if (message == null || message.isEmpty()) {
            message = "Intrueixi una paraula o frase";
        }
        System.out.println(message);
        return scan.nextLine();
    }

    /**
     * Prompts the user for a non-empty string input. Repeats until valid input
     * is entered.
     *
     * @param message the prompt shown to the user. If not provided, a default
     * prompt is used.
     * @param errorMessage message shown when the input is empty. If not
     * provided, a default error message is used.
     * @return a non-empty string from the user
     */
    public String askForNotEmptyString(String message, String errorMessage) {
        String text = "";
        boolean correcte = false;
        if (message == null || message.isEmpty()) {
            message = "Intrueixi una paraula o frase";
        }
        do {
            System.out.println(message);
            text = scan.nextLine();
            if (text == null || text.isEmpty()) {
                if (errorMessage == null || errorMessage.isEmpty()) {
                    errorMessage = "S'ha introduït un text buit";
                }

                showError(errorMessage);
            } else {
                correcte = true;
            }

        } while (!correcte);
        return text;
    }

    /**
     * Prompts the user to enter an integer value. Repeats until a valid integer
     * is entered.
     *
     * @param message prompt shown to the user. If not provided, a default
     * prompt is used.
     * @param errorMessage message shown when the input is invalid. If not
     * provided, a default error message is used.
     * @return a valid integer entered by the user
     */
    public int askForInteger(String message, String errorMessage) {
        String input;
        int num = 0;
        boolean correcte = false;
        if (message == null || message.isEmpty()) {
            message = "Intrueixi un número enter";
        }

        do {
            try {// en este caso a diferencia de pedir un string, sí uso el try-catch porque sí
                // puede lanzar un NumberFormatException en el parseInt
                System.out.println(message);
                input = scan.nextLine();
                num = Integer.parseInt(input);
                correcte = true;
            } catch (NumberFormatException e) {
                if (errorMessage == null || errorMessage.isEmpty()) {
                    errorMessage = "S'ha introduït un format invàlid";
                }
                showError(errorMessage);
            }

        } while (!correcte);
        return num;
    }

    /**
     * Prompts the user to enter a floating-point value. Repeats until a valid
     * float is entered.
     *
     * @param message prompt shown to the user. If not provided, a default
     * prompt is used.
     * @param errorMessage message shown when the input is invalid. If not
     * provided, a default error message is used.
     * @return a valid float entered by the user
     */
    public float askForFloat(String message, String errorMessage) {
        String input;
        float numDec = 0.0f;
        boolean correcte = false;
        if (message == null || message.isEmpty()) {
            message = "Intrueixi un número amb decimals";
        }

        do {
            System.out.println(message);
            input = scan.nextLine();
            if (input == null || input.isEmpty()) {
                System.err.println(errorMessage);
            } else {
                try {
                    numDec = Float.parseFloat(input);
                    correcte = true;
                } catch (NumberFormatException e) {
                    if (errorMessage == null || errorMessage.isEmpty()) {
                        errorMessage = "S'ha introduït un format invàlid";
                    }
                    showError(errorMessage);
                }
            }
        } while (!correcte);
        return numDec;
    }

    /**
     * Displays a list of bets formatted using a predefined template. Each bet
     * must have exactly the expected number of columns (timestamp, sport,
     * event, betType, odds, amount). Malformed bet entries are silently
     * skipped.
     *
     * @param betList the raw string containing all bets separated by newline
     * @throws IllegalArgumentException if betList is null or empty
     */
    public void showBets(String betList) {
        if (betList == null || betList.isEmpty()) {
            throw new IllegalArgumentException("El fitxer no pot ser null o estar buit");
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-20s %-20s %-20s %-30s %-10s %-5s", "DATETIME", "SPORT", "ESDEVENIMENT", "TIPUS", "QUOTES", "IMPORT\n");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------");

        String[] linies = betList.split("\n");
       
        for (String linea : linies) {
            String[] arrayLinea = linea.split(",");
             if(arrayLinea.length == 6){
            System.out.printf("%-20s %-20s %-20s %-30s %-10.2f %-5.2f%n", arrayLinea[0], arrayLinea[1], arrayLinea[2], arrayLinea[3], Float.parseFloat(arrayLinea[4]), Float.parseFloat(arrayLinea[5]));
        }   
    }
     System.out.println("");
}
}
