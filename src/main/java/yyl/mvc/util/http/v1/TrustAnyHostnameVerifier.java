package yyl.mvc.util.http.v1;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/** HTTPS域名校验(信任所有) */
public class TrustAnyHostnameVerifier implements HostnameVerifier {

	private TrustAnyHostnameVerifier() {
	}

	public boolean verify(String hostname, SSLSession session) {
		return true;
	}

	/** HTTPS域名校验(信任所有) */
	public static HostnameVerifier getInsecureVerifier() {
		return new TrustAnyHostnameVerifier();
	}
}
