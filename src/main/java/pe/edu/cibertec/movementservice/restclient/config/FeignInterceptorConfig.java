package pe.edu.cibertec.movementservice.restclient.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignInterceptorConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
                    HttpServletRequest request = attributes.getRequest();
                    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
                    if (authorization != null) {
                        requestTemplate.header(HttpHeaders.AUTHORIZATION, authorization);
                    }
                }
            }
        };
    }
}
