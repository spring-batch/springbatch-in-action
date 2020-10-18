package kr.seok.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
    String version = "0.0.1";

    @Bean
    public Docket api() { // Swagger 설정의 핵심이 되는 Bean
        return new Docket(DocumentationType.SWAGGER_2)
                /* Request, Response Type 설정 */
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                /* 여러 Docker Bean을 생성하는 경우 구분하기 위한 group명 명시 (버전관리) */
                .groupName(version)
                // 제목, 설명, 버전 등 문서에 대한 정보들을 보여주기 위해 호출
                .apiInfo(apiInfo("SR", version))
                // ApiSelectorBuilder를 생성
                .select()
                // API spec이 작성되어 있는 패키지를 지정
                .apis(RequestHandlerSelectors.basePackage("kr.seok.admin.controller"))
                // apis()로 선택되어진 API중 특정 path 조건에 맞는 API들을 다시 필터링하여 문서화
                .paths(PathSelectors.ant("/**"))
                .build()
                ;
    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                .title(title)
                .description("SR Multi Project API Docs") // 설명
                .termsOfServiceUrl("https://github.com/SeokRae/sr")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE")
                .version(version)
                .build();
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }
}