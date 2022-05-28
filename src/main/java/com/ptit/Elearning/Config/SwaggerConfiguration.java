package com.ptit.Elearning.Config;

import com.fasterxml.classmate.TypeResolver;
import com.ptit.Elearning.Entity.Class;
import com.ptit.Elearning.Entity.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ELearning")
                .description("Đồ án 10đ phục vụ cuối kỳ môn 'Phát triển phần mềm hướng dịch vụ'")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .contact(new Contact("", "", "n18dccn171@student.ptithcm.edu.vn"))
                .build();
    }

    @Bean
    public Docket api(TypeResolver typeResolver) {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ptit.Elearning"))
                .build().apiInfo(apiInfo())
                .additionalModels(typeResolver.resolve(Account.class),
                        typeResolver.resolve(Class.class),
                        typeResolver.resolve(ClassRegistration.class),
                        typeResolver.resolve(CreditClass.class),
                        typeResolver.resolve(Degree.class),
                        typeResolver.resolve(Department.class),
                        typeResolver.resolve(Document.class),
                        typeResolver.resolve(Excercise.class),
                        typeResolver.resolve(Folder.class),
                        typeResolver.resolve(Notification.class),
                        typeResolver.resolve(Post.class),
                        typeResolver.resolve(PostComment.class),
                        typeResolver.resolve(Profession.class),
                        typeResolver.resolve(Role.class),
                        typeResolver.resolve(Room.class),
                        typeResolver.resolve(Student.class),
                        typeResolver.resolve(Subject.class),
                        typeResolver.resolve(Submit.class),
                        typeResolver.resolve(Teacher.class),
                        typeResolver.resolve(Timeline.class),
                        typeResolver.resolve(UserInfo.class)
                        );
    }
}
