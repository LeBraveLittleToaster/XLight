package de.pschiessle.xlight.xlightserver.endpoints.reponse;

import de.pschiessle.xlight.xlightserver.util.Response;

public class PutAddDeviceResp extends Response {
    public static final String RESPONSE_TYPE = "ADD_DEVICE";



    public PutAddDeviceResp(Object body) {
        super(STATUS_OK, RESPONSE_TYPE, body);
    }




}
