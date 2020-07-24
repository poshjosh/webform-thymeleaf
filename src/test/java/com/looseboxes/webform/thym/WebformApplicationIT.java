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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import com.looseboxes.webform.CRUDAction;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 */
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes={
            com.looseboxes.webform.thym.WebformApplication.class, 
            com.looseboxes.webform.WebformBasePackageClass.class})
public class WebformApplicationIT extends RestTestBase{
    
    private static final Logger LOG = LoggerFactory.getLogger(WebformApplicationIT.class);
    
    private final TestConfig testConfig = new TestConfig();
    
    @LocalServerPort private int port;

    @Autowired private TestRestTemplate restTemplate;
    
    @BeforeEach
    public void beforeEach() {
        this.setRestTemplate(restTemplate);
    }
    
//    @Test
//    public void givenInvalidDomainType_ShouldFail() {
//        this.debug("givenInvalidDomainType_ShouldFail");
//        this.debug(heading("@TODO"));
//    }
    
//    @Test
    public void givenCreateModel_ShouldCompleteAllStagesSuccessfully() {
        try{
            givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(CRUDAction.create, Blog.class, true);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void givenValidBlogInputs_ShouldCompleteAllCRUDActionsSuccessfully() 
            throws Exception {
        givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully(Blog.class, false);
    }

    @Test
    public void givenValidPostInputsAndExpressOptions_ShouldCompleteAllCRUDActionsSuccessfully() 
            throws Exception {
        this.givenValidPostInputs_ShouldCompleteAllCRUDActionsSuccessfully(true);
    }
    
    @Test
    public void givenValidPostInputs_ShouldCompleteAllCRUDActionsSuccessfully() 
            throws Exception {
        this.givenValidPostInputs_ShouldCompleteAllCRUDActionsSuccessfully(false);
    }
    
    public void givenValidPostInputs_ShouldCompleteAllCRUDActionsSuccessfully(
            boolean express) throws Exception {
        
        // A blog is required for the post
        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(CRUDAction.create, Blog.class, express);
        
        givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully(Post.class, express);
    }

    @Test
    public void givenValidTagInputsAndExpressOption_ShouldCompleteAllCRUDActionsSuccessfully() throws Exception {
        this.givenValidTagInputs_ShouldCompleteAllCRUDActionsSuccessfully(true);
    }
    
    @Test
    public void givenValidTagInputs_ShouldCompleteAllCRUDActionsSuccessfully() throws Exception {
        this.givenValidTagInputs_ShouldCompleteAllCRUDActionsSuccessfully(false);
    }
    
    public void givenValidTagInputs_ShouldCompleteAllCRUDActionsSuccessfully(boolean express) 
            throws Exception {
        
        // A blog is required for the post
        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(CRUDAction.create, Blog.class, express);
        
        // A post is required for the tag
        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(CRUDAction.create, Post.class, express);

        givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully(Tag.class, express);

        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(CRUDAction.delete, Post.class, express);

        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(CRUDAction.delete, Blog.class, express);
    }

    public void givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully(
            Class domainType, boolean express) throws Exception {
        LOG.debug("givenValidInputs_ShouldCompleteAllCRUDActionsSuccessfully("+domainType.getName()+")");
        LOG.debug("BEGIN-CRUD");
        
        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(CRUDAction.create, domainType, express);
        
        this.formActionForModel_ShouldReturnValidDocument(CRUDAction.read, domainType);

        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(CRUDAction.update, domainType, express);

        givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(CRUDAction.delete, domainType, express);

        LOG.debug("END-CRUD");
    }

    public void givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(
            CRUDAction action, Class modeltype) throws Exception {
        
        this.givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(action, modeltype, false);
    }
    
    public void givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully(
            CRUDAction action, Class modeltype, boolean express) throws Exception {
        LOG.debug("givenValidFormActionsForModel_ShouldCompleteAllStagesSuccessfully");
        
        final List<String> cookies = new ArrayList<>();
        final Map params = new HashMap();
        final Consumer<ResponseEntity<String>> responseHandler = 
                this.getResponseHandler(cookies, params);
        
        LOG.debug("show form");
        formActionForModel_ShouldReturnValidDocument(
                action, modeltype, responseHandler);
        
        if(express) {
        
            LOG.debug("validate & submit form");
            formActionForModel_ShouldReturnValidDocument(
                    action, modeltype, TestUrls.SUFFIX_VALIDATE + TestUrls.SUFFIX_SUBMIT, 
                    cookies, params, responseHandler);
        }else{
        
            LOG.debug("validate form");
            formActionForModel_ShouldReturnValidDocument(
                    action, modeltype, TestUrls.SUFFIX_VALIDATE, 
                    cookies, params, responseHandler);

            LOG.debug("submit form");
            formActionForModel_ShouldReturnValidDocument(
                    action, modeltype, TestUrls.SUFFIX_SUBMIT, 
                    cookies, params, responseHandler);
        }
    }
    
    public ResponseEntity<String> formActionForModel_ShouldReturnValidDocument(
            CRUDAction action, Class type) throws Exception{
        
        return formActionForModel_ShouldReturnValidDocument(
                action, type, (response) -> {});
    }
    
    public ResponseEntity<String> formActionForModel_ShouldReturnValidDocument(
            CRUDAction action, Class type, 
            Consumer<ResponseEntity<String>> responseHandler) throws Exception{
        
        return formActionForModel_ShouldReturnValidDocument(
                action, type, "", Collections.EMPTY_LIST, 
                Collections.EMPTY_MAP, responseHandler);
    }

    public ResponseEntity<String> formActionForModel_ShouldReturnValidDocument(
            CRUDAction action, Class type, 
            String suffix, List<String> cookiesForRequest,
            Map params, Consumer<ResponseEntity<String>> responseHandler) 
            throws Exception{
        
        final ResponseEntity<String> response = 
                formActionForModel_ShouldReturnSuccess(action, type, suffix, 
                        cookiesForRequest, params, responseHandler);
        
        return response;
    }
    
    public ResponseEntity<String> formActionForModel_ShouldReturnSuccess(
            CRUDAction action, Class type) throws Exception{
        
        return this.formActionForModel_ShouldReturnSuccess(
                action, type, "", Collections.EMPTY_LIST,
                Collections.EMPTY_MAP, (response) -> {});
    }

    public ResponseEntity<String> formActionForModel_ShouldReturnSuccess(
            CRUDAction action, Class type, 
            String suffix, List<String> cookies, Map params,
            Consumer<ResponseEntity<String>> responseHandler) throws Exception{
        
        final TestUrls testUrls = this.getTestUrls();
        LOG.trace("TestUrls: {}", testUrls);
        
        final String modelname = this.getTableName(type);
        LOG.trace("modelname={}", modelname);       
        
        final String url = testUrls.formUrl(port, action, modelname, suffix);
        LOG.trace("url={}", url);       
        
        HttpMethod method = testUrls.getHttpMethod(url);
        LOG.trace("HttpMethod: {}", method);       

        final Map paramsFromForm = testUrls.getFormParameters(url, modelname);
        LOG.trace("Params from form: {}", paramsFromForm);       
        
        final Map outputParams = new HashMap(paramsFromForm);
        outputParams.putAll(params);
        
        final ResponseEntity<String> response = 
                givenUrl_ShouldReturnSuccess(url, method, cookies, outputParams);
        
        responseHandler.accept(response);
        
        if(suffix != null && suffix.endsWith(TestUrls.SUFFIX_SUBMIT) && 
                response.getStatusCode().is2xxSuccessful()) {
            
            if(CRUDAction.create.equals(action)) {
                TestUrls.incrementCount(modelname);
            }else if(CRUDAction.delete.equals(action)) {
                TestUrls.decrementCount(modelname);
            }
        }
        
        return response;
    }
    
    private Consumer<ResponseEntity<String>> getResponseHandler(
            List<String> cookies, Map params) {
        
        final Consumer<ResponseEntity<String>> responseHandler = (response) -> {

            params.clear();

            cookies.addAll(getCookies(response));

            final Optional<Document> docOptional = getDocument(response);

            if(docOptional.isPresent()) {
                
                final HtmlForm htmlForm = new HtmlForm();
                
                final Document doc = docOptional.get();
                
                final Element errors = doc.getElementById("errors");
                if(errors != null) {
                    errors.html();
                }
                final Element infos = doc.getElementById("infos");
                if(infos != null) {
                    infos.html();
                }
                
                final Elements forms = htmlForm.printAll(doc);
                if(forms != null) {
                    forms.forEach((form) -> {
                        params.putAll(htmlForm.getValues(form));
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
            LOG.warn("Expected: HTML content, did not find any");
        }    
        return Optional.ofNullable(doc);
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
        
        ResponseEntity<String> result = shouldReturnStatusCode(
                givenUrl_whenRestApiCalled(url, method, cookies, String.class, params), code);
        
        final String body = result.getBody();
        
        if(expectedContent == null) {
            assertThat(body, isNull());
        }else{
//            assertThat(body, contains(expectedContent));
        }
        
        return result;
    }
    
    public TestUrls getTestUrls() {
        return getTestConfig().testUrls();
    }

    public TestConfig getTestConfig() {
        return testConfig;
    }
}