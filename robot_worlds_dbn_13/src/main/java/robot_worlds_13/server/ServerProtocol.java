package robot_worlds_13.server;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ServerProtocol {
    
    // must take command responses from each command executed by the commands and represent a fail or not
    // each of the commands must send an 
    // must return a json representation of the data
    // public String jsonResponseBuilder () {

    //     return ;
    // }


    // public jsonRequestUnpacker ()

    private static Gson gson = new Gson();
    
    // forward, left, right, 
    public static String buildResponse(String result, Map<String, Object> data, Map<String, Object> state) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", result);
        responseMap.put("data", data);
        if (state != null) {
            responseMap.put("state", state);
        }
        return gson.toJson(responseMap);
    }

    //
    public static String buildResponse(String result, Map<String, Object> data) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", result);
        responseMap.put("data", data);
        return gson.toJson(responseMap);
    }

    // for commands like state
    public static String buildResponse(Map<String, Object> state) {
        Map<String, Object> responseMap = new HashMap<>();
        if (state != null) {
            responseMap.put("state", state);
        }
        return gson.toJson(responseMap);
    }
}
