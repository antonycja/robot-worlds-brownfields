package robot_worlds_13.client;

import java.awt.List;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import robot_worlds_13.server.ServerProtocol;

public class GUIProtocol {

    static private Gson gson = new Gson();
    
    
    
    static public Map<String, Object> jsonResponseUnpacker(String jsonResponse) {
        
        // data.clear();
        //     data.put("message", "MOVE");
        //     state.clear();
        //     state = target.getGUIRobotState();
        //     target.setResponseToRobot(ServerProtocol.buildResponse("GUI", data, state));
        
        Map<String, Object> commandMap =  new HashMap<>();
        if (jsonResponse.equals("{}")) {
            
            return commandMap;
        }


        try {
            // Map<String, Object> responseMap = gson.fromJson(jsonResponse, new TypeToken<Map<String, Object>>(){}.getType());
            Map<String, Object> responseMap = gson.fromJson(jsonResponse, new TypeToken<Map<String, Object>>(){}.getType());

            if (!"GUI".equals(responseMap.get("result"))) {
                return commandMap;
            } 

            if (responseMap.get("data") != null) {
                if (responseMap.get("data") instanceof Map) {
                    
                    Map<String, Object> innerMap = (Map<String, Object>) responseMap.get("data");
                    
                    if (innerMap.get("message") != null) {
                        String messageResponse = (String) innerMap.get("message");
                        commandMap.put("message", messageResponse);
                    }
                }
            }

            if (responseMap.get("state") != null) {
                if (responseMap.get("state") instanceof Map) {
                    Map<String, Object> innerMap = (Map<String, Object>) responseMap.get("state");
                    if (innerMap.get("obstacles") != null) {
                        Object obstacles = (Object) innerMap.get("obstacles");
                        commandMap.put("obstacles", obstacles);
                        return commandMap;
                    }
                    
                    Object name = (Object) innerMap.get("name");
                    commandMap.put("name", name);
                    Object previousPosition = (Object) innerMap.get("previousPosition");
                    commandMap.put("previousPosition", previousPosition);
                    Object position = (Object) innerMap.get("position");
                    commandMap.put("position", position);
                    Object direction = (Object) innerMap.get("direction");
                    commandMap.put("direction", direction);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("No json found");
            return commandMap;
        }
        return commandMap;
    }
}
