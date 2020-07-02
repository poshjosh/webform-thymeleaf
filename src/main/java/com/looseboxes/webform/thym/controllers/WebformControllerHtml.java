package com.looseboxes.webform.thym.controllers;

import com.looseboxes.webform.controllers.FormControllerHtml;
import com.looseboxes.webform.exceptions.RouteException;
import com.looseboxes.webform.exceptions.ResourceNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author hp
 */
@Controller
public class WebformControllerHtml
        extends FormControllerHtml<Object>
        implements ErrorController{

    @RequestMapping("/") 
    public String home(){
        // @TODO stream README.md
        return "home";
    }

    @RequestMapping("/error") 
    public String error(){
        return "error";
    }

    /**
     * Returns the path of the error page.
     * 
     * Primarily used to know the error paths that will not need to be secured.
     * 
     * @return the error path
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @Override
    public ModelAndView handleMaxSizeExceeded(
        MaxUploadSizeExceededException exception, HttpServletRequest request) {
        return super.handleMaxSizeExceeded(exception, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @Override
    public ModelAndView handleResourceNotFound(
        ResourceNotFoundException exception, HttpServletRequest request) {
        return super.handleResourceNotFound(exception, request);
    }

    @ExceptionHandler(RouteException.class)
    @Override
    public ModelAndView handleRouteProblem(
            RouteException exception, HttpServletRequest request) {
        return super.handleRouteProblem(exception, request);
    }
    
    @ExceptionHandler(Exception.class)
    @Override
    public ModelAndView handleException(Exception exception, HttpServletRequest request) {
        return super.handleException(exception, request);
    }
}
