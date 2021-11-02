package com.example.rapidboard.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConfig {
    @Bean
    HttpClient httpClient() {
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//        connectionManager.setMaxTotal(100);
//        connectionManager.setDefaultMaxPerRoute(50);

        return HttpClientBuilder.create()
//                .setConnectionManager(connectionManager)
                .setMaxConnTotal(100)    //최대 오픈되는 커넥션 수, 연결을 유지할 최대 숫자
                .setMaxConnPerRoute(30)   //IP, 포트 1쌍에 대해 수행할 커넥션 수, 특정 경로당 최대 숫자
                .build();
    }

    @Bean
    HttpComponentsClientHttpRequestFactory factory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);        //읽기시간초과, ms
        factory.setConnectTimeout(3000);     //연결시간초과, ms
        factory.setHttpClient(httpClient);

        return factory;
    }

    @Bean
    RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }


//    Connection 수에 관한 설정을 담은 HttpClient 객체를 생성하고,
//    이 HttpClient를 이용하여 Connection 시간 설정과 함께 HttpComponetsClientHttpRequestFactory 객체를 생성한다.
//    그 후 RestTemplate의 생성자 인자인 ClientHttpRequestFactory의 구현체로써 HttpComponetsClientHttpRequestFactory 객체를 사용하여 주입을 시켜주었다.
}
