package com.looseboxes.webform.thym;

import java.util.HashMap;
import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hp
 */
public class HtmlForm {
    
    private static final Logger LOG = LoggerFactory.getLogger(HtmlForm.class);
    
    public Elements printAll(Document doc) {
        Elements forms = null; 
        LOG.debug("<response.form>");
        if(doc != null) {
            forms = doc.getElementsByTag("form");
            if(forms.isEmpty()) {
                LOG.warn("FORM ELEMENT NOT FOUND IN HTML");
            }else{
                for(Element form : forms) {
                    if(form != null) {
                        LOG.debug("\n" + form.html());
                    }
                }
            }
        }
        LOG.debug("</response.form>");
        return forms == null ? new Elements() : forms; 
    }
    
    public Map getValues(Element form) {
        final Map result = new HashMap();
        this.addFormInputs(form, result);
        this.addFormSelects(form, result);
        this.addFormTextArea(form, result);
        
        LOG.debug("Parameters extrated from form: " + result);
        
        return this.removeNullOrEmpty(result);
    }
    
    private Map removeNullOrEmpty(Map result) {
        final Map output = new HashMap<>();
        result.forEach((k, v) -> {
            if(k != null && !k.toString().isEmpty() && 
                    v != null && !v.toString().isEmpty()) {
                output.put(k, v);
            }
        });
        return output;
    }
    
    private void addFormInputs(Element form, final Map result) {
        final Elements inputs = form.getElementsByTag("input");
        if(inputs != null && ! inputs.isEmpty()) {
            inputs.forEach((input) -> {
                LOG.trace("Found form.input: {}", (input == null ? null : input.html()));
                if(input != null) {
                    result.put(input.attr("name"), input.attr("value"));
                }
            });
        }
    }

    private void addFormTextArea(Element form, final Map result) {
        final Elements inputs = form.getElementsByTag("textarea");
        if(inputs != null && ! inputs.isEmpty()) {
            inputs.forEach((input) -> {
                LOG.trace("Found form.textarea: {}", (input == null ? null : input.html()));
                if(input != null) {
                    result.put(input.attr("name"), input.text());
                }
            });
        }
    }

    private void addFormSelects(Element form, final Map result) {
        final Elements inputs = form.getElementsByTag("select");
        if(inputs != null && ! inputs.isEmpty()) {
            inputs.forEach((input) -> {
                Elements e = input.getElementsByAttributeValue("selected", "true");
                if (e == null || e.isEmpty()) {
                    e = input.getElementsByAttribute("selected");
                }
                if(e != null && !e.isEmpty()) {
                    final Element option = e.get(0);
                    LOG.trace("Found form.select.option: " + (option == null ? null : option.html()));
                    if(option != null) {
                        result.put(option.attr("name"), option.attr("value"));
                    }
                }
            });
        }
    }
}
