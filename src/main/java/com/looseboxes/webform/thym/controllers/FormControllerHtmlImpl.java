package com.looseboxes.webform.thym.controllers;

import com.looseboxes.webform.controllers.FormControllerHtml;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hp
 */
@Controller
public class FormControllerHtmlImpl extends FormControllerHtml{

    @RequestMapping("/") 
    public String home(){
        // @TODO stream README.md
        return "home";
    }
}
/**
 * 
    @Autowired
    public FormControllerImpl(Environment environment, FormService genericFormSvc, FormValidatorFactory formValidatorFactory, AttributeService genericAttributeSvc, MessageAttributesService messageAttributesSvc, FileUploadService fileUploadSvc, OnFormSubmitted onFormSubmitted) {
        super(environment, genericFormSvc, formValidatorFactory, genericAttributeSvc, messageAttributesSvc, fileUploadSvc, onFormSubmitted);
    }
 * 
 */