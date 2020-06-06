package com.looseboxes.webform.thym.controllers;

import com.looseboxes.webform.controllers.FormControllerRest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hp
 */
//@RestController 
@RequestMapping(path = "/api", 
        produces = MediaType.APPLICATION_JSON_VALUE, 
        method = {RequestMethod.GET, RequestMethod.POST})
public class FormControllerRestImpl extends FormControllerRest{
    
}
