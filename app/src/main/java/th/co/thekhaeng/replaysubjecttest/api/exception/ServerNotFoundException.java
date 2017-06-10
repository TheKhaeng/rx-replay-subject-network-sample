package th.co.thekhaeng.replaysubjecttest.api.exception;

/**
 * Created by thekhaeng on 3/20/2017 AD.
 */

public class ServerNotFoundException extends NullPointerException {
    public ServerNotFoundException() {
    }

    public ServerNotFoundException(String s) {
        super(s);
    }
}

