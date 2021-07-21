package az.ibrahimshirinov.filestorageminio;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "File Microservice",
        description = "Api endpoints for store files in minio server",
        contact = @Contact(name = "Ibrahim Shirinov", email = "Ibrahim.Shirinov@embawood.com"),
        version = "WIP"
)
)
@SpringBootApplication
public class FileStorageMinioApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileStorageMinioApplication.class, args);
    }

}
