//// java
//package com.easybank.gatewayserver.filters;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.Ordered;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.WebFilter;
//
//@Component("filterOrderInspectorFilters")
//public class FilterOrderInspector implements ApplicationRunner {
//
//    private static final Logger logger = LoggerFactory.getLogger(FilterOrderInspector.class);
//    private final ApplicationContext ctx;
//
//    public FilterOrderInspector(ApplicationContext ctx) {
//        this.ctx = ctx;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//        logger.info("=== SecurityWebFiltersOrder values ===");
//        for (SecurityWebFiltersOrder o : SecurityWebFiltersOrder.values()) {
//            logger.info("{} -> {}", o.name(), o.getOrder());
//            System.out.println(o.name() + " -> " + o.getOrder());
//        }
//
//        logger.info("=== Registered WebFilter beans and effective order ===");
//        String[] beanNames = ctx.getBeanNamesForType(WebFilter.class);
//        for (String name : beanNames) {
//            Object bean = ctx.getBean(name);
//            String className = bean.getClass().getName();
//            String orderStr = (bean instanceof Ordered) ? String.valueOf(((Ordered) bean).getOrder()) : "none";
//            logger.info("beanName={} class={} order={}", name, className, orderStr);
//            System.out.println(name + " -> " + className + " -> " + orderStr);
//        }
//    }
//}
//// java
//package com.easybank.gatewayserver.filters;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.Ordered;
//import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.WebFilter;
//
//@Component("filterOrderInspectorFilters")
//public class FilterOrderInspector implements ApplicationRunner {
//
//    private static final Logger logger = LoggerFactory.getLogger(FilterOrderInspector.class);
//    private final ApplicationContext ctx;
//
//    public FilterOrderInspector(ApplicationContext ctx) {
//        this.ctx = ctx;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) {
//        logger.info("=== SecurityWebFiltersOrder values ===");
//        for (SecurityWebFiltersOrder o : SecurityWebFiltersOrder.values()) {
//            logger.info("{} -> {}", o.name(), o.getOrder());
//            System.out.println(o.name() + " -> " + o.getOrder());
//        }
//
//        logger.info("=== Registered WebFilter beans and effective order ===");
//        String[] beanNames = ctx.getBeanNamesForType(WebFilter.class);
//        for (String name : beanNames) {
//            Object bean = ctx.getBean(name);
//            String className = bean.getClass().getName();
//            String orderStr = (bean instanceof Ordered) ? String.valueOf(((Ordered) bean).getOrder()) : "none";
//            logger.info("beanName={} class={} order={}", name, className, orderStr);
//            System.out.println(name + " -> " + className + " -> " + orderStr);
//        }
//    }
//}
