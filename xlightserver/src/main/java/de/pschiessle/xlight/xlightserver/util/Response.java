package de.pschiessle.xlight.xlightserver.util;

import java.time.Instant;

public abstract class Response {
    public static final int STATUS_OK = 1;
    public static final int STATUS_ERROR_UNKNOWN = 0;
    public final int status;
    public final long date;
    public final String responseType;
    public final Object body;

    protected Response(int status, String responseType, Object body) {
        this.status = status;
        this.date = Instant.now().getEpochSecond();
        this.responseType = responseType;
        this.body = body;
    }
}
