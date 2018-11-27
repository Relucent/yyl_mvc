package yyl.mvc.util.http;

import java.net.URL;

/**
 * Http Tool.
 */
public class Http {

    public static Connection connect(String url) {
        return Connection.connect(url);
    }

    public static Connection connect(URL url) {
        return Connection.connect(url);
    }
}
