package robot_worlds_13.server;

/**
 * Utility class to check the validity of robot names.
 */
public class NameChecker {

    /**
     * Checks if a given name is valid.
     * @param name The name to be checked.
     * @return True if the name is valid, otherwise false.
     */
    static boolean isValidName (String nameList) {
        if (nameList.isEmpty()){
            return false;
        }

        if (nameList == null || nameList.trim() == "") {
            return false;
        }

        return true;
    }
}
