package yyl.mvc.test.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

import yyl.mvc.plug.json.MyObjectMapper;
import yyl.mvc.plug.spring.convert.DateConverter;
import yyl.mvc.plug.spring.convert.EnumConverterFactory;
import yyl.mvc.plug.spring.convert.ListxConverter;
import yyl.mvc.plug.spring.convert.MapxConverter;
import yyl.mvc.plug.spring.servlet.SpringMvcExceptionResolver;
import yyl.mvc.test.plug.PaginationMethodArgumentResolver;

@Configuration
//@EnableWebMvc
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	// ==============================Fields===========================================
	//...
	// ==============================Methods==========================================

	//[EnableWebMvc]
	//@Bean
	//public UrlBasedViewResolver setupViewResolver() {
	//	UrlBasedViewResolver resolver = new UrlBasedViewResolver();
	//	resolver.setPrefix("/WEB-INF/jsp/");
	//	resolver.setSuffix(".jsp");
	//	resolver.setViewClass(JstlView.class);
	//	return resolver;
	//}

	/** 类型转换 */
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new DateConverter());
		registry.addConverter(new MapxConverter());
		registry.addConverter(new ListxConverter());
		registry.addConverterFactory(new EnumConverterFactory());
	}

	/** SpringMVC异常处理 */
	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
		SpringMvcExceptionResolver resolver = new SpringMvcExceptionResolver();
		//resolver.setExceptionMappings("byit.core.plug.expection.AuthorizationException",403);
		resolvers.add(resolver);
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(stringHttpMessageConverter());
		converters.add(mappingJackson2HttpMessageConverter());
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		super.addArgumentResolvers(argumentResolvers);
		argumentResolvers.add(new PaginationMethodArgumentResolver());
	}

	//// @Override
	//// public void addViewControllers(ViewControllerRegistry registry) {
	//// 	registry.addViewController("/").setViewName("forward:/default.html");
	//// 	registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	//// 	super.addViewControllers(registry);
	//// }

	// ==============================BeanMethods======================================
	@Bean
	public StringHttpMessageConverter stringHttpMessageConverter() {
		StringHttpMessageConverter converter = new StringHttpMessageConverter(Charsets.UTF_8);
		return converter;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(objectMapper());
		return converter;
	}

	@Bean("objectMapper")
	public ObjectMapper objectMapper() {
		return MyObjectMapper.INSTANCE;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		//基于 commons-fileupload 的Multipart支持
		//CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		//resolver.setDefaultEncoding("utf-8");
		//resolver.setMaxUploadSize(104857600L);

		//基于 Servlet 3.0 的Multipart支持
		//如果需要设置上传文件大小可以注入 MultipartConfigElement 
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		return resolver;
	}
}
