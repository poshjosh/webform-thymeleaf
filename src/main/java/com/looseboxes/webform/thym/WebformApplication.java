package com.looseboxes.webform.thym;

import com.looseboxes.webform.FormEndpoints;
import com.looseboxes.webform.util.PrintAppInfo;
import com.looseboxes.webform.web.FormConfigDTO;
import com.looseboxes.webform.web.HtmResponseHandler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import com.looseboxes.webform.web.ResponseHandler;

@SpringBootApplication(scanBasePackageClasses = {
        com.looseboxes.webform.thym.WebformApplication.class, 
        com.looseboxes.webform.WebformBasePackageClass.class
})
public class WebformApplication {
    
    public static void main(String[] args) {
            SpringApplication.run(WebformApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
            return new PrintAppInfo(ctx);
    }

    @Bean public ResponseHandler<FormConfigDTO, String> responseHandler() {
        return new HtmResponseHandler(formEndpoints());
    }

    @Bean FormEndpoints formEndpoints() {
        return new WebformEndpoints();
    }
}
