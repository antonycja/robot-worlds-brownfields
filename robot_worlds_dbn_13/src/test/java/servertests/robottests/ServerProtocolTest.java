package servertests.robottests;

import org.junit.jupiter.api.Test;


import robot_worlds_13.server.ServerProtocol;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.HashMap;

import java.util.Map;

public class ServerProtocolTest{
    @Test

    void testBuildResponseWithResultDataAndState() {

        Map<String, Object> data = new HashMap<>();

        data.put("key", "value");


        Map<String, Object> state = new HashMap<>();

        state.put("stateKey", "stateValue");


        String response = ServerProtocol.buildResponse("OK", data, state);


        assertEquals("{\"result\":\"OK\",\"data\":{\"key\":\"value\"},\"state\":{\"stateKey\":\"stateValue\"}}", response);

    }

    @Test

    void testBuildResponseWithResultAndData() {

        Map<String, Object> data = new HashMap<>();

        data.put("key", "value");


        String response = ServerProtocol.buildResponse("OK", data);


        assertEquals("{\"result\":\"OK\",\"data\":{\"key\":\"value\"}}", response);

    }

    @Test

    void testBuildResponseWithState() {

        Map<String, Object> state = new HashMap<>();

        state.put("stateKey", "stateValue");


        String response = ServerProtocol.buildResponse(state);


        assertEquals("{\"state\":{\"stateKey\":\"stateValue\"}}", response);

    }


}
