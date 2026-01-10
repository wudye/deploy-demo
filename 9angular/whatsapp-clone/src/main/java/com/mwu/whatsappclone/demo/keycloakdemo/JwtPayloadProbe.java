package com.mwu.whatsappclone.demo.keycloakdemo;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtPayloadProbe {
    public static void main(String[] args) {
      //  String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJCQ3l2R1k3dmhtNnBGOGF4SDNXbzdMalc4NXZ5WFV1d2x2LXc1WGc4UFNZIn0.eyJleHAiOjE3Njc5NzAwNzAsImlhdCI6MTc2Nzk2NjQ3MCwianRpIjoib25ydHJvOjY3NzA0OWIwLTM1ZTYtYTYxYi04N2RjLTVhZDkyMTYwMmY3YSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTA4MC9yZWFsbXMvd2hhdHNhcHAtY2xvbmUiLCJzdWIiOiJlMmExMGI2OC1jZjFkLTRiMzgtOGY1Ni00NTA3NzVlNmU1NWUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ3aGF0c2FwcC1jbG9uZS1jbGllbnQiLCJzaWQiOiI4ZEZoX0ZNU2pXbVEwcTlQUUtYWnJVbGEiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiIsIkFETUlOIiwiVVNFUiJdfSwic2NvcGUiOiJwcm9maWxlIGVtYWlsIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsInJlYWxtX2FjY2VzcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIiwiQURNSU4iLCJVU0VSIl0sIm5hbWUiOiJBZG1pbiBVc2VyIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW51c2VyIiwiZ2l2ZW5fbmFtZSI6IkFkbWluIiwiZmFtaWx5X25hbWUiOiJVc2VyIiwiZW1haWwiOiJhZG1pbnVzZXJAZXhhbXBsZS5jb20ifQ.moum77y_50PPbLczjmJ-_P8RofUHFpCiwYn5gL1cVVB2zEajf4Tylyi9Nlz53Y0_jm5kZh-KrV0JsL_c5HobIHWgXSMA-te_wh43u9Kcy01iArf3itqEQ5Rr24g6XPuGuT4XQyi7TmGv8gIsnqCTJl17N6MPuVb14ZGrRdgWcYd7p8A8uEXTCsCeUm9IEmhMFCs5SAncFbwvAHnGERmEQDTChwsOpvbmUO5t7xGr-W7nlQGpalSr8nV_Q1HskQXN3QYoson9UB_5btH1nbK2QYC7e3XHfS8JJfJSr1YYrgguy2evhVCSv7YwlB2Wx_arehDMRCXiJq1bk918PC5PHw";
        /*

        payload JSON 里 同名键 realm_access 出现了两次：一次是对象（带 roles），一次是数组。许多严格 JSON 解析器（Nimbus/Spring Security 使用的那套）会把这种 重复 key 视为 Malformed payload，因此资源服务器解码失败是合理的。
         payload:
        {"exp":1767970070,"iat":1767966470,"jti":"onrtro:677049b0-35e6-a61b-87dc-5ad921602f7a",
        "iss":"http://localhost:9080/realms/whatsapp-clone","sub":"e2a10b68-cf1d-4b38-8f56-450775e6e55e",
        "typ":"Bearer","azp":"whatsapp-clone-client","sid":"8dFh_FMSjWmQ0q9PQKXZrUla","acr":"1","allowed-origins"
        :["http://localhost:4200"],"realm_access":{"roles":["ROLE_USER","ROLE_ADMIN","ADMIN","USER"]},
        "scope":"profile email","email_verified":true,"realm_access":["ROLE_USER","ROLE_ADMIN","ADMIN","USER"],
        "name":"Admin User","preferred_username":"adminuser","given_name":"Admin","family_name":"User","email":"adminuser@example.com"}

         方案	优点	缺点	推荐度
方案 1：移除 Protocol Mappers	• 使用 Keycloak 内置标准• 无冲突• 无需修改代码	• 固定格式	⭐⭐⭐⭐⭐
方案 2：自定义 Claim Name	• 避免冲突• 灵活命名	• 非标准• 需要修改代码	⭐⭐⭐
方案 3：扁平化数组	• 简化数据结构• 易于解析	• 非标准• 需要修改代码• 可能不支持复杂角色	⭐⭐
         */

         String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ6V0JBUENMeTVTdFpubjRaN0JldTJyUXlTS0ZPRmRnMnpmaEtDTk10ZjdrIn0.eyJleHAiOjE3Njc5NzA1NTgsImlhdCI6MTc2Nzk2Njk1OCwianRpIjoib25ydHJvOmQ3ZDdmZGQ3LTc4MzctNzBhNy03NWE0LWFkMjM5NzJhMzg0MiIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTA4MC9yZWFsbXMvd2hhdHNhcHAtY2xvbmUiLCJzdWIiOiI4ZGI1YzNlZC1mNmMzLTQ0OGMtOGQwOS0yNDJkOGIyYzMzZDgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ3aGF0c2FwcC1jbG9uZS1jbGllbnQiLCJzaWQiOiJGSHRVdDk0Y3djT29CMFBfS0FzYmJYNXYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiQURNSU4iLCJVU0VSIl19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicm9sZXMiOlsiQURNSU4iLCJVU0VSIl0sIm5hbWUiOiJBZG1pbiBVc2VyIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW51c2VyIiwiZ2l2ZW5fbmFtZSI6IkFkbWluIiwiZmFtaWx5X25hbWUiOiJVc2VyIiwiZW1haWwiOiJhZG1pbnVzZXJAZXhhbXBsZS5jb20ifQ.ApqbP9Cr2iXBgEaw5Nrvq2lcyWPXEIOFnSh7ORuJGHbjN3zyI1g5SroUg_6S1gWSwk5IxGLHD_Gv3BtQS1hXC-PQRmhi7h23FYlzAXLDlrQHkqhiNSA4T6BeGI8EWqrYf3R0IQCdJTWYx60JkNxnoFKPGnc3C_XrqyjFZJxIzZ03rESOJHuhmhBK_oSIeqjj6r_0X4eKIorbMw_zpT9RjTsKOGUmCjO-srDaX26ah4rC1P3FZr3dWLRW3QE97abaAXjmd4uqdbRxJ5YEzdt7YSLkW0RjHMkvgCZ95qN-sLRUNoUW3gTnnAzOSnxNQh4MGyhWfcG1F11R-Jo3K6kFDA";

        String payloadPart = token.split("\\.")[1];
        byte[] payload = Base64.getUrlDecoder().decode(payloadPart); // 若此行抛 IllegalArgumentException，说明令牌本身损坏
        System.out.println(new String(payload, StandardCharsets.UTF_8));
    }
}