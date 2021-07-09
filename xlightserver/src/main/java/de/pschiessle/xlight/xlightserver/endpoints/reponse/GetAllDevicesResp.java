package de.pschiessle.xlight.xlightserver.endpoints.reponse;

import de.pschiessle.xlight.xlightserver.model.XDeviceEntity;
import de.pschiessle.xlight.xlightserver.util.Response;

import java.util.List;

public class GetAllDevicesResp extends Response {
    public static final String RESPONSE_TYPE = "ALL_DEVICES";

    public GetAllDevicesResp(List<XDeviceEntity> devices) {
        super(STATUS_OK, RESPONSE_TYPE, devices);
    }

    public GetAllDevicesResp(int status) {
        super(status, RESPONSE_TYPE, null);
    }

    public GetAllDevicesResp() {
        super(STATUS_ERROR_UNKNOWN, RESPONSE_TYPE, null);
    }
}
