AuthenticatedUser（工具类）
↓
功能 1：获取用户名（username）
↓
功能 2：获取用户角色（roles）
↓
功能 3：获取令牌属性（attributes）
↓
功能 4：从 JWT 提取角色（extractRolesFromToken）


认证类型	支持状态	示例
UserDetails	✅ 支持	基本认证、表单登录
JwtAuthenticationToken	✅ 支持	JWT 令牌认证
DefaultOidcUser	✅ 支持	OAuth2/OIDC 认证
String principal	✅ 支持	简单字符串用户名

