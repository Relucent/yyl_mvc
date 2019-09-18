package yyl.mvc.plugin.security.encoder;

/**
 * 密码编码(加密)器实现类(不进行加密操作)
 * @author _YaoYiLang
 * @version 2017-01-01
 */
public class PasswordEncoderNoOp implements PasswordEncoder {

	/**
	 * 编码<br/>
	 * @param password 原始密码
	 * @return 加密后的密码
	 */
	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	/**
	 * 匹配密码明文和密文
	 * @param rawPassword 原始密码(明文)
	 * @param encodedPassword 加密后的密码(密文)
	 * @return 如果明文和密文匹配则返回true,否则返回false
	 */
	@Override
	public boolean matches(CharSequence rawPassword, CharSequence encodedPassword) {
		return rawPassword.toString().equals(encodedPassword);
	}
}
