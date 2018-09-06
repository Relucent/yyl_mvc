package yyl.mvc.util.http.v1;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import yyl.mvc.util.http.v1.HttpClient;
import yyl.mvc.util.http.v1.Interceptor;
import yyl.mvc.util.http.v1.Request;
import yyl.mvc.util.http.v1.Response;

public class HttpV1Test {
    public static void main(String[] args) {
        System.out.println(new HttpClient.Builder()//
                .connectTimeout(5, TimeUnit.SECONDS)//
                .readTimeout(5, TimeUnit.SECONDS)//
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        System.out.println("Request Headers:");
                        System.out.println(request.headers());
                        Response response = chain.proceed(request);
                        System.out.println("Response Headers:");
                        System.out.println(response.headers());
                        return response;
                    }
                })//
                .build()//
                .execute(//
                        new Request.Builder()//
                                .url("http://www.360.com/")//
                                .header("Host", "www.360.com")//
                                .header("User-Agent", "text/html; charset=utf-8")//
                                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")//
                                .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")//
                                .header("Connection", "keep-alive")//
                                .get()//
                                .build()//
                ).body().string()//
        );
    }
}
