package yyl.mvc.util.http;

import java.net.URL;

public class Http {

	public static Connection connect(String url) {
		return HttpConnection.connect(url);
	}

	public static Connection connect(URL url) {
		return HttpConnection.connect(url);
	}
}
