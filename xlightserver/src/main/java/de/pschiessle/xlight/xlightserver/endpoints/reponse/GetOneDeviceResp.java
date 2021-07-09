package de.pschiessle.xlight.xlightserver.endpoints.reponse;

import de.pschiessle.xlight.xlightserver.model.XDeviceEntity;
import de.pschiessle.xlight.xlightserver.util.Response;

public class GetOneDeviceResp extends Response {
    public static final String RESPONSE_TYPE = "GET_ONE_DEVICE";

    public GetOneDeviceResp(XDeviceEntity device) {
        super(STATUS_OK, RESPONSE_TYPE, device);
    }

    public GetOneDeviceResp(int status) {
        super(status, RESPONSE_TYPE, null);
    }

    public GetOneDeviceResp() {
        super(STATUS_ERROR_UNKNOWN, RESPONSE_TYPE, null);
    }
}
