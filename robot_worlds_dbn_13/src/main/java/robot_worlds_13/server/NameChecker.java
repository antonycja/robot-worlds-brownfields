package robot_worlds_13.server;

import java.util.ArrayList;

public class NameChecker {
    
    static boolean isValidName (ArrayList nameList) {
        if (nameList.isEmpty()){
            return false;
        }

        String potentialName = (String) nameList.get(0);
        if (potentialName == null || potentialName.trim() == "") {
            return false;
        }

        return true;
    }
}
