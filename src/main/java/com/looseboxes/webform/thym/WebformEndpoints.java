package com.looseboxes.webform.thym;

import com.looseboxes.webform.FormEndpoints;
import org.springframework.stereotype.Component;

/**
 * @author hp
 */
@Component
public class WebformEndpoints implements FormEndpoints{

    @Override
    public String getError() {
        return "error";
    }

    @Override
    public String getSuccess() {
        return "success";
    }

    @Override
    public String getForm() {
        return "form";
    }

    @Override
    public String getFormConfirmation() {
        return "formConfirmation";
    }

    @Override
    public String getFormData() {
        return "formData";
    }
}
