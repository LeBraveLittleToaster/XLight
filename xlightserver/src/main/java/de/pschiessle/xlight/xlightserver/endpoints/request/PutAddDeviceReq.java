package de.pschiessle.xlight.xlightserver.endpoints.request;

import de.pschiessle.xlight.xlightserver.util.Request;

public class PutAddDeviceReq extends Request {
    public final String name;

    public PutAddDeviceReq(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PutAddDeviceReq{" +
                "name='" + name + '\'' +
                '}';
    }
}
