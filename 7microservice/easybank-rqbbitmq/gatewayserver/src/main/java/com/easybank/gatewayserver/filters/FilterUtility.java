package com.easybank.gatewayserver.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import org.springframework.http.HttpHeaders;
import java.util.List;

@Component
public class FilterUtility {

    public static final String CORRELATION_ID = "eazybank-correlation-id";

    public String getCorrelationId(HttpHeaders requestHeaders) {
        if (requestHeaders.get(CORRELATION_ID) != null) {
            List<String> requestHeaderList = requestHeaders.get(CORRELATION_ID);
            return requestHeaderList.stream().findFirst().get();
        } else {
            return null;
        }
    }

    /*
    方法用于在响应式请求上下文 ServerWebExchange 中设置新的请求头。
    它通过 mutate() 方法创建一个新的请求副本，并添加或覆盖指定的请求头。这样可以在过滤器链中安全地传递自定义头部信息。
     */
    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        //        使用 request.mutate() 修改请求头，然后用 exchange.mutate() 返回新的 Exchange
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    // 专门用于设置相关性 ID。它内部调用了 setRequestHeader，将 CORRELATION_ID 作为头部名称，简化了调用流程。
    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

}
