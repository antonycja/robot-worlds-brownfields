package robot_worlds_13.server;

import java.util.ArrayList;

public class NameChecker {
    
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
