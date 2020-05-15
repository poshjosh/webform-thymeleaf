/*
 * Copyright 2020 looseBoxes.com
 *
 * Licensed under the looseBoxes Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.looseboxes.webform.thym;

import com.looseboxes.webform.CrudActionNames;
import com.looseboxes.webform.thym.WebformApplication;
import com.looseboxes.webform.thym.domain.Blog;
import com.looseboxes.webform.thym.domain.Post;
import com.looseboxes.webform.thym.domain.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author USER
 */
@SpringBootTest(
        webEnvironment = WebEnvironment.DEFINED_PORT, 
        classes=WebformApplication.class)
public class WebformApplicationIT extends TestBase{
    
    private final TestConfig testConfig = new TestConfig();
    
    @LocalServerPort private int port;

    @Autowired private TestRestTemplate restTemplate;
    
    @Test
    public void givenInvalidDomainType_ShouldFail() {
        this.debug("givenInvalidDomainType_ShouldFail");
        this.debug(heading("@TODO"));
    }
    
    @Test
    public void givenValidBlogInputs_ShouldCompleteAllCRUDActionsSuccessfully() 
            throws Exception {
        givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully(Blog.class);
    }
    
    @Test
    public void givenValidPostInputs_ShouldCompleteAllCRUDActionsSuccessfully() 
            throws Exception {
        
        // A blog is required for the post
        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(
                CrudActionNames.CREATE, Blog.class);
        
        givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully(Post.class);
    }

    @Test
    public void givenValidTagInputs_ShouldCompleteAllCRUDActionsSuccessfully() 
            throws Exception {
        
        // A blog is required for the post
        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(
                CrudActionNames.CREATE, Blog.class);
        
        // A post is required for the tag
        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(
                CrudActionNames.CREATE, Post.class);

        givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully(Tag.class);
    }

    public void givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully(
            Class domainType) throws Exception {
        debug(heading("BEGIN-CRUD"));
        debug("givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully("+domainType.getName()+")");
        debug(heading("END-CRUD"));
        
        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(
                CrudActionNames.CREATE, domainType);
        
        this.formActionForModel_ShouldReturnValidDocument(
                CrudActionNames.READ, domainType);

        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(
                CrudActionNames.UPDATE, domainType);

        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(
                CrudActionNames.DELETE, domainType);
    }

    public void givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(
            String action, Class modeltype) throws Exception {
        debug("givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully");
        
        final List<String> cookies = new ArrayList<>();
        final Map params = new HashMap();
        final Consumer<ResponseEntity<String>> responseHandler = 
                this.getResponseHandler(cookies, params);
        
        debug(heading("show form"));
        formActionForModel_ShouldReturnValidDocument(
                action, modeltype, responseHandler);
        
        debug(heading("validate form"));
        formActionForModel_ShouldReturnValidDocument(
                action, modeltype, TestUrls.SUFFIX_VALIDATE, 
                cookies, params, responseHandler);
        
        debug(heading("submit form"));
        formActionForModel_ShouldReturnValidDocument(
                action, modeltype, TestUrls.SUFFIX_SUBMIT, 
                cookies, params, responseHandler);
    }
    
    public ResponseEntity<String> formActionForModel_ShouldReturnValidDocument(
            String action, Class type) throws Exception{
        
        return formActionForModel_ShouldReturnValidDocument(
                action, type, (response) -> {});
    }
    
    public ResponseEntity<String> formActionForModel_ShouldReturnValidDocument(
            String action, Class type, 
            Consumer<ResponseEntity<String>> responseHandler) throws Exception{
        
        return formActionForModel_ShouldReturnValidDocument(
                action, type, "", Collections.EMPTY_LIST, 
                Collections.EMPTY_MAP, responseHandler);
    }

    public ResponseEntity<String> formActionForModel_ShouldReturnValidDocument(
            String action, Class type, 
            String suffix, List<String> cookiesForRequest,
            Map params, Consumer<ResponseEntity<String>> responseHandler) 
            throws Exception{
        
        final ResponseEntity<String> response = 
                formActionForModel_ShouldReturnSuccess(action, type, suffix, 
                        cookiesForRequest, params, responseHandler);
        
        return response;
    }
    
    public ResponseEntity<String> formActionForModel_ShouldReturnSuccess(
            String action, Class type) throws Exception{
        
        return this.formActionForModel_ShouldReturnSuccess(
                action, type, "", Collections.EMPTY_LIST,
                Collections.EMPTY_MAP, (response) -> {});
    }

    public ResponseEntity<String> formActionForModel_ShouldReturnSuccess(
            String action, Class type, 
            String suffix, List<String> cookies, Map params,
            Consumer<ResponseEntity<String>> responseHandler) throws Exception{
        
        final TestUrls testUrls = this.getTestUrls();
        trace("TestUrls: " + testUrls);
        
        final String modelname = this.getTableName(type);
        trace("modelname=" + modelname);       
        
        String url = testUrls.formUrl(port, action, modelname, suffix);
        trace("url=" + url);       
        
        HttpMethod method = testUrls.getHttpMethod(url);
        trace("HttpMethod: " + method);       
        try{
        final Map paramsFromForm = testUrls.getFormParameters(url, modelname);
        trace("Params from form: " + paramsFromForm);       
        
        final Map outputParams = new HashMap(paramsFromForm);
        outputParams.putAll(params);
        
        final ResponseEntity<String> response = 
                givenUrl_ShouldReturnSuccess(url, method, cookies, outputParams);
        
        responseHandler.accept(response);
        
        return response;
        }catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    private Consumer<ResponseEntity<String>> getResponseHandler(
            List<String> cookies, Map params) {
        
        final Consumer<ResponseEntity<String>> responseHandler = (response) -> {

            params.clear();

            cookies.addAll(getCookies(response));

            final Optional<Document> docOptional = getDocument(response);

            if(docOptional.isPresent()) {
                final Document doc = docOptional.get();
                final Elements forms = printForm(doc);
                if(forms != null) {
                    forms.forEach((form) -> {
                        params.putAll(getFormValues(form));
                    });
                }
            }else{
                fail("Expected: HTML content, did not find any");
            }
        };
        
        return responseHandler;
    }

    private Optional<Document> getDocument(ResponseEntity<String> response) {
        final String html = response.getBody();
//        debug(html);
        final Document doc = org.jsoup.Jsoup.parse(html);
        if(doc == null) {
            warn("Expected: HTML content, did not find any");
        }    
        return Optional.ofNullable(doc);
    }
    
    private Elements printForm(Document doc) {
        Elements forms = null;
        debug(heading("<response.form>"));
        if(doc != null) {
            final Element errors = doc.getElementById("errors");
            if(errors != null) {
                errors.html();
            }
            final Element infos = doc.getElementById("infos");
            if(infos != null) {
                infos.html();
            }
            forms = doc.getElementsByTag("form");
            if(forms.isEmpty()) {
                warn("FORM ELEMENT NOT FOUND IN HTML");
            }else{
                for(Element form : forms) {
                    if(form != null) {
                        debug("\n" + form.html());
                    }
                }
            }
        }
        debug(heading("</response.form>"));
        return forms;
    }
    
    private Map getFormValues(Element form) {
        final Map result = new HashMap();
        this.addFormInputs(form, result);
        this.addFormSelects(form, result);
        this.addFormTextArea(form, result);
        
        debug("Parameters extrated from form: " + result);
        
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
                trace("Found form.input: " + (input == null ? null : input.html()));
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
                trace("Found form.textarea: " + (input == null ? null : input.html()));
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
                    trace("Found form.select.option: " + (option == null ? null : option.html()));
                    if(option != null) {
                        result.put(option.attr("name"), option.attr("value"));
                    }
                }
            });
        }
    }
    
    private String getTableName(Class type) {
        return type.getSimpleName().toLowerCase();
    }
    
    public Map getParameters(String url, String action, String modelname) {
        return this.getTestUrls().getFormParameters(url, modelname);
    }
    
    private ResponseEntity<String> givenUrl_ShouldReturnSuccess(
            String url, HttpMethod method, 
            List<String> cookies, Map params) throws Exception {
        
        final ResponseEntity<String> result = this.givenUrl_ShouldReturn(
                url, method, cookies, params, 200, "success");
        
        return result;
    }

    private ResponseEntity<String> givenUrl_ShouldReturn(
            String url, HttpMethod method, 
            List<String> cookies, Map params,
            int code, String expectedContent) throws Exception {
        
        final ResponseEntity<String> result = this.givenUrl_ShouldReturn(
                url, method, cookies, code, String.class, params);
        
        final String body = result.getBody();
        
        if(expectedContent == null) {
            assertThat(body, isNull());
        }else{
//            assertThat(body, contains(expectedContent));
        }
        
        return result;
    }
    
    private <T> ResponseEntity<T> givenUrl_ShouldReturn(
            String url, HttpMethod method, List<String> cookies, int code,
            Class<T> responseType, Map params) throws Exception {
        
        debug(heading("Sending"));
        debug("->   request to: " + url);
        debug("-> with cookies: " + cookies);
        debug("->   and params: " + params);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Cookie",cookies.stream().collect(Collectors.joining(";")));
        
        final HttpEntity request;
        final ResponseEntity<T> response;
        if(HttpMethod.POST.equals(method)) {
            // If you don't use MultiValueMap you get error
            // org.springframework.web.client.RestClientException: No HttpMessageConverter for java.util.HashMap and content type "application/x-www-form-urlencoded"
            final MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
            params.forEach((k, v) -> {
                if(k != null && v != null) {
                    mvm.put(k.toString(), Collections.singletonList(v.toString()));
                }
            });
            request = new HttpEntity<>(mvm, headers);
            response = restTemplate.postForEntity(url, request, responseType);
        }else{
            request = new HttpEntity<>(headers);
            response = restTemplate.exchange(
                    url, method, request, responseType, params);
        }
        
        this.getCookies(response);
        
        assertThat(response.getStatusCodeValue(), is(code));

        return response;
    }
    
    public List<String> getCookies(ResponseEntity responseEntity) {
        final List<String> cookiesReceived = responseEntity.getHeaders().get("Set-Cookie");
        trace("Cookies received: " + cookiesReceived);
        return cookiesReceived == null ? Collections.EMPTY_LIST : cookiesReceived;
    }
    
    public TestUrls getTestUrls() {
        return getTestConfig().testUrls();
    }

    public TestConfig getTestConfig() {
        return testConfig;
    }
}