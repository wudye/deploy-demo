package com.easybank.accounts.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
//，用于将配置文件中的属性绑定到 Java 类的字段上。这里的 prefix = "accounts" 指定了配置文件中以 accounts 开头的属性会映射到该类的字段。例如，如果配置文件中有 accounts.message=Hello，它会自动绑定到 message 字段。
@ConfigurationProperties(prefix = "accounts")
@Getter
@Setter
public class AccountsContactInfoDto {

    private String message;
    private Map<String, String> contactDetails;
    private List<String> onCallSupport;

}
