curl.exe --% -X POST "http://localhost:9080/realms/whatsapp-clone/protocol/openid-connect/token" -H "Content-Type: application/x-www-form-urlencoded" -d "grant_type=password&client_id=whatsapp-clone-client&client_secret=your-client-secret&username=adminuser&password=admin123"

Invoke-RestMethod -Uri 'http://localhost:8080/api/users/me' -Method GET -Headers @{ Authorization = 'Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ6V0JBUENMeTVTdFpubjRaN0JldTJyUXlTS0ZPRmRnMnpmaEtDTk10ZjdrIn0.eyJleHAiOjE3Njc5NzA1NTgsImlhdCI6MTc2Nzk2Njk1OCwianRpIjoib25ydHJvOmQ3ZDdmZGQ3LTc4MzctNzBhNy03NWE0LWFkMjM5NzJhMzg0MiIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTA4MC9yZWFsbXMvd2hhdHNhcHAtY2xvbmUiLCJzdWIiOiI4ZGI1YzNlZC1mNmMzLTQ0OGMtOGQwOS0yNDJkOGIyYzMzZDgiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ3aGF0c2FwcC1jbG9uZS1jbGllbnQiLCJzaWQiOiJGSHRVdDk0Y3djT29CMFBfS0FzYmJYNXYiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHA6Ly9sb2NhbGhvc3Q6NDIwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiQURNSU4iLCJVU0VSIl19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicm9sZXMiOlsiQURNSU4iLCJVU0VSIl0sIm5hbWUiOiJBZG1pbiBVc2VyIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiYWRtaW51c2VyIiwiZ2l2ZW5fbmFtZSI6IkFkbWluIiwiZmFtaWx5X25hbWUiOiJVc2VyIiwiZW1haWwiOiJhZG1pbnVzZXJAZXhhbXBsZS5jb20ifQ.ApqbP9Cr2iXBgEaw5Nrvq2lcyWPXEIOFnSh7ORuJGHbjN3zyI1g5SroUg_6S1gWSwk5IxGLHD_Gv3BtQS1hXC-PQRmhi7h23FYlzAXLDlrQHkqhiNSA4T6BeGI8EWqrYf3R0IQCdJTWYx60JkNxnoFKPGnc3C_XrqyjFZJxIzZ03rESOJHuhmhBK_oSIeqjj6r_0X4eKIorbMw_zpT9RjTsKOGUmCjO-srDaX26ah4rC1P3FZr3dWLRW3QE97abaAXjmd4uqdbRxJ5YEzdt7YSLkW0RjHMkvgCZ95qN-sLRUNoUW3gTnnAzOSnxNQh4MGyhWfcG1F11R-Jo3K6kFDA' } -Verbose 

{
"realm": "whatsapp-clone",     // 领域名称，应用的唯一标识
"enabled": true,                // 启用该领域
"sslRequired": "external",      // 仅外部连接需要 SSL
"registrationAllowed": false,    // 禁止用户自行注册
"loginWithEmailAllowed": true,   // 允许使用邮箱登录
"resetPasswordAllowed": true,    // 允许用户重置密码
"editUsernameAllowed": true,     // 允许用户修改用户名
"bruteForceProtected": true      // 启用防暴力破解保护
}


"roles": {
"realm": [
{
"name": "ADMIN",                    // 管理员角色
"description": "Administrator role"
},
{
"name": "USER",                     // 普通用户角色
"description": "Regular user role"
}
]
}

{
"username": "adminuser",
"email": "adminuser@example.com",
"firstName": "Admin",
"lastName": "User",
"emailVerified": true,           // 邮箱已验证
"enabled": true,                  // 账户已启用
"credentials": [
{
"type": "password",
"value": "admin123",          // 密码（明文存储）
"temporary": false            // 非临时密码
}
],
"realmRoles": ["ADMIN", "USER"]   // 同时拥有管理员和用户角色
}

{
"clientId": "whatsapp-clone-client",           // 客户端标识
"name": "WhatsApp Clone Client",
"enabled": true,
"clientAuthenticatorType": "client-secret",    // 使用客户端密钥认证
"secret": "your-client-secret",                // 客户端密钥（生产环境应更改）
"redirectUris": ["http://localhost:4200/*"],    // 允许的重定向 URI
"webOrigins": ["http://localhost:4200"],        // CORS 允许的源
"bearerOnly": false,                            // 非 bearer-only 客户端
"consentRequired": false,                       // 不需要用户同意
"standardFlowEnabled": true,                    // 启用标准授权码流程
"implicitFlowEnabled": false,                   // 禁用隐式流程（已废弃）
"directAccessGrantsEnabled": true,              // 启用资源所有者密码流程
"serviceAccountsEnabled": false,                // 禁用服务账户
"publicClient": false,                          // 机密客户端
"protocol": "openid-connect",                   // 使用 OpenID Connect 协议
"attributes": {
"access.token.lifespan": "3600"              // 访问令牌有效期 1 小时
}
}

"protocolMappers": [
{
"name": "realm_access",
"protocol": "openid-connect",
"protocolMapper": "oidc-usermodel-realm-role-mapper",  // Keycloak 内置映射器
"consentRequired": false,
"config": {
"multivalued": true,                // 支持多个值
"userinfo.token.claim": true,        // 包含在 UserInfo 响应中
"id.token.claim": true,              // 包含在 ID Token 中
"access.token.claim": true,          // 包含在 Access Token 中 ⭐
"claim.name": "roles"                 // 
}
}
]

{
"sslRequired": "all",                          // 强制所有连接使用 SSL
"registrationAllowed": false,                  // 保持禁用自行注册
"bruteForceProtected": true,                   // 保持防暴力破解
"attributes": {
"access.token.lifespan": "300"              // 减少到 5 分钟
}
}


方案	优点	缺点	推荐度
方案 1：移除 Protocol Mappers	• 使用 Keycloak 内置标准• 无冲突• 无需修改代码	• 固定格式	⭐⭐⭐⭐⭐
方案 2：自定义 Claim Name	• 避免冲突• 灵活命名	• 非标准• 需要修改代码	⭐⭐⭐
方案 3：扁平化数组	• 简化数据结构• 易于解析	• 非标准• 需要修改代码• 可能不支持复杂角色	⭐⭐