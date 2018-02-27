package retrofit;

import java.io.IOException;

/**
 * Created by Ewan on 2017/1/13.
 */

public class NoConnectivityException extends IOException {
    private static final long serialVersionUID = 6306271651592225301L;

    public NoConnectivityException () {
        super();
    }

    public NoConnectivityException (String arg0, Throwable throwable) {
        super(arg0, throwable);
    }

    public NoConnectivityException (String arg0) {
        super(arg0);
    }

    public NoConnectivityException (Throwable throwable) {
        super(throwable);
    }
}
