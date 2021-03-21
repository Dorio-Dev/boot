package com.daradat.boot.solutionization.logicTransition.okhttp;

import okhttp3.OkHttpClient;

/**
 * {@link OkHttpClient}를 Singleton으로 사용하기 위하여 구성하였다.
 * RestAPI Call 시에 추가하여야 하는 로직이 있다면 {@link Interceptor} 를 지정하여 사용할 수 있다. <br/>
 * 예를 들어 Rest API 사용시 추가해야 하는 Header 값이 있다면 추가할 수 있다. <br/>
 * 추가하는 방법으로
 * OKHTTPClient 에는 ApplicationInterceptor와 NetworkInterceptor가 있으며 자세한 활용에제는 OKHttpClient 홈페이지를 참고하라.
 *
 * @see https://square.github.io/okhttp/interceptors/
 *
 *
 *
 */
public class OKHttpClientInstance {
    private final OkHttpClient client;

    private OKHttpClientInstance() {
        this.client = new OkHttpClient();
    }

    /**
     * singleton으로 구성된 {@link com.daradat.boot.solutionization.logicTransition.type.CallTypeInvokerFactory} 인스턴스를 리턴한다.
     */
    public static OkHttpClient getInstance(){
        return InstanceHolder.instance.client;
    }

    /**
     * singleton 인스턴스 구성체
     *
     */
    private static class InstanceHolder {

        private static final OKHttpClientInstance instance = new OKHttpClientInstance();

    }
}
