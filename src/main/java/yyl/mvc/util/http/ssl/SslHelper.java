package yyl.mvc.util.http.ssl;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SslHelper {

	/**
	 * 获得 KeyStore
	 * @param input 数据流
	 * @param password 密码
	 * @return KeyStore
	 */
	public static KeyStore getKeyStore(InputStream input, String password) {
		try {
			KeyStore keyStore = KeyStore.getInstance("");
			keyStore.load(input, password.toCharArray());
			return keyStore;
		} catch (Exception exception) {
			return null;
		}

	}

	/**
	 * 获得X509证书
	 * @param input 证书的数据流
	 * @return X509证书
	 */
	public static X509Certificate getX509Certificate(InputStream input) {
		try {
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			return (X509Certificate) factory.generateCertificate(input);
		} catch (Exception exception) {
			return null;
		}
	}
}
