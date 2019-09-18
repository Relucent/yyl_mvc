package yyl.mvc.util.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import yyl.mvc.common.http.Http;
import yyl.mvc.common.http.Connection.Method;

public class HttpTest {
    public static void main(String[] args) throws IOException {
        System.out.println(Http.connect("http://www.360.com/")//
                .connectTimeout(5, TimeUnit.SECONDS)//
                .readTimeout(5, TimeUnit.SECONDS)//
                .header("Host", "www.360.com")//
                .header("User-Agent", "text/html; charset=utf-8")//
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")//
                .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")//
                .header("Connection", "keep-alive")//
                .method(Method.GET)//
                .execute().body());
    }
}
