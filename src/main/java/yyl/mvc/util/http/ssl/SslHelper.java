package yyl.mvc.util.http.ssl;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

    /**
     * 获得主机名验证器(不做HOSTNAME验证的验证器)
     * @return 主机名验证器
     */
    public static HostnameVerifier getInsecureHostnameVerifier() {
        return INSECURE_HOSTNAME_VERIFIER;
    }

    /**
     * 获得不安全的 SSLSocket 工厂类 (忽略SSL校验)
     * @return SSLSocket工厂类
     */
    public static SSLSocketFactory getInsecureSSLSocketFactory() {
        return INSECURE_SSL_SOCKET_FACTORY;
    }

    private static final HostnameVerifier INSECURE_HOSTNAME_VERIFIER;
    private static final SSLSocketFactory INSECURE_SSL_SOCKET_FACTORY;

    static {
        // Instantiate Hostname Verifier that does nothing
        INSECURE_HOSTNAME_VERIFIER = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public void checkClientTrusted(final X509Certificate[] chain, final String authType) {}

            public void checkServerTrusted(final X509Certificate[] chain, final String authType) {}

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS", "SunJSSE");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            INSECURE_SSL_SOCKET_FACTORY = sslContext.getSocketFactory();
        } catch (NoSuchProviderException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Can't create unsecure trust manager");
        }
    }
}
