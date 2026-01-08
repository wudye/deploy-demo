可以用两种方式登录并访问你项目的 API：
1) 使用 Keycloak 下发的 access token（推荐用于 API 调试或机器调用）
   在 Keycloak 的 client 设置里启用 Direct Access Grants Enabled（如果用密码模式）。
   通过 token endpoint 用用户名/密码 获取 token，然后把 token 放到 Authorization: Bearer ... 头里调用 API。
2) 使用交互式浏览器登录（Authorization Code / PKCE）
   在 Keycloak 为你的应用配置 client（设置回调 URI、access type、是否 confidential 等），在 Spring Boot 中添加 spring-boot-starter-oauth2-client 并配置 spring.security.oauth2.client.registration / provider。
   或在现有 SecurityConfig 中使用 Spring 的 oauth2Login 替代自定义跳转，确保 ClientRegistrationRepository 可用（否则会报找不到 bean 的错误）。
   下面给出实用示例（先是用密码模式拿 token 并调用 API；然后给出 application.properties 的最小配置示例，用于启用 Spring OAuth2 客户端与资源服务器验证）：
   说明：将 {KEYCLOAK_HOST}, {REALM}, {CLIENT_ID}, {CLIENT_SECRET} 换成你自己的值；test1 / 123456 为你创建的用户

# 获取 access token（Resource Owner Password Credentials / Direct Access Grants）
# Keycloak token endpoint:
# http://{KEYCLOAK_HOST}/realms/{REALM}/protocol/openid-connect/token
curl -X POST "http://localhost:8080/realms/myrealm/protocol/openid-connect/token" \
-H "Content-Type: application/x-www-form-urlencoded" \
-d "grant_type=password&client_id=demo-client&client_secret=secret&username=test1&password=123456"


powershell version


curl --% -X POST "http://192.168.80.129:7080/realms/demo-realm/protocol/openid-connect/token" -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=password&client_id=demo-client&client_secret=7h4aIOYL5PGQGmuxRqoQ9F0Tttp8yqzP&username=test3&password=123456789"

curl.exe --% -X POST "http://192.168.80.129:7080/realms/demo-realm/protocol/openid-connect/token" -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=password&client_id=demo-client&client_secret=7h4aIOYL5PGQGmuxRqoQ9F0Tttp8yqzP&username=test2&password=123456"

# 返回示例 JSON 包含 access_token 字段，取出后调用 API：
curl -H "Authorization: Bearer {ACCESS_TOKEN}" http://localhost:8081/api/hello

# ------------------------------
# 如果想用 Spring OAuth2 Authorization Code 登录（浏览器重定向），在
# `src/main/resources/application.properties` 添加示例配置：
# (替换 host/realm/client/secret)
spring.security.oauth2.client.registration.keycloak.client-id=demo-client
spring.security.oauth2.client.registration.keycloak.client-secret=secret
spring.security.oauth2.client.registration.keycloak.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.keycloak.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
spring.security.oauth2.client.provider.keycloak.issuer-uri=http://localhost:8080/realms/myrealm

# 资源服务器 JWT 验证（你的 SecurityConfig 已实现 JwtDecoder）：
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/myrealm


注意事项摘要：
若用密码模式，Keycloak client 必须允许 Direct Access Grants。该方式不建议用于浏览器端生产登录（安全性问题）。
若要浏览器登录并让 Spring 管理回调，需添加 OAuth2 client 依赖与 client registration 配置，否则会出现找不到 ClientRegistrationRepository 的错误。
你的后端作为资源服务器时，不需要“登录”到后端；只需客户端携带 Keycloak 下发的有效 access token 即可访问受保护的 API。