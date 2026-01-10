package com.mwu.whatsappclone.demo.keycloakdemo;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextLogger {



    @EventListener
    public void handleAuthenticationSuccess(
            AuthenticationSuccessEvent event) {

        Authentication auth = event.getAuthentication();
        System.out.println("=== Authentication Success ===");
        System.out.println("Principal: " + auth.getPrincipal());
        System.out.println("Authorities: " + auth.getAuthorities());
        System.out.println("SecurityContext: " +
                SecurityContextHolder.getContext().getAuthentication());

        /*
        监听的 AuthenticationSuccessEvent 不保证发生在同一个线程/同一个请求上下文里，而 SecurityContextHolder 默认使用 ThreadLocal 存储安全上下文；事件发布时当前线程里可能还没设置 SecurityContext，或者事件在异步执行/不同线程执行，所以你看到：
event.getAuthentication() 有值（事件携带了认证结果）
但 SecurityContextHolder.getContext().getAuthentication() 为 null（当前线程的 ThreadLocal 里没放）
另外，你现在打印的是 SecurityContextHolder.getContext().getAuthentication()，如果想在事件里看到一致的内容，应直接用 event.getAuthentication()，或者在请求处理链里（过滤器/控制器）读取。

         */
        Authentication eventAuth = event.getAuthentication();

        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication contextAuth = (ctx != null) ? ctx.getAuthentication() : null;

        System.out.println("=== Authentication Success ===");
        System.out.println("Event Authentication: " + eventAuth);
        System.out.println("Event Authorities: " + eventAuth.getAuthorities());
        System.out.println("SecurityContext Authentication: " + contextAuth);
    }
}
