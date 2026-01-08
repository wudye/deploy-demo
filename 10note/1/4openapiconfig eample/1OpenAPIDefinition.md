import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(name = "mwu", email = "<EMAIL>"),
                description = "study" +
                        "(java 25)",
                title = "my " ,
                version = "1.0"
        )
)
public class OpenApiConfig {
}

