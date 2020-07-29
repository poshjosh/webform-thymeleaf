package com.looseboxes.webform.thym;

import com.bc.fileupload.FileuploadConfigurationSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author hp
 */
@Configuration
public class WebformFileuploadConfiguration extends FileuploadConfigurationSource{

    public WebformFileuploadConfiguration(Environment environment) {
        super(environment);
    }
}
