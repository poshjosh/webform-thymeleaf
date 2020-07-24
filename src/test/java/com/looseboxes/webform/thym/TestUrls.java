package com.looseboxes.webform.thym;

import com.bc.webform.TypeTests;
import com.looseboxes.webform.Params;
import com.looseboxes.webform.thym.domain.Blog;
import com.looseboxes.webform.thym.domain.Post;
import com.looseboxes.webform.thym.domain.Tag;
import com.looseboxes.webform.thym.domain.enums.BlogType;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.http.HttpMethod;
import com.looseboxes.webform.CRUDAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.looseboxes.webform.FormStages;

/**
 * @author hp
 */
public class TestUrls{
    
    private static final Logger LOG = LoggerFactory.getLogger(TestUrls.class);
    
    private static final Map<String, Integer> counts = new HashMap<>();
    public static Integer incrementCount(String modelname) {
        Integer count = counts.getOrDefault(modelname, 0);
        ++count;
        counts.put(modelname, count);
        return count;
    }
    public static Integer decrementCount(String modelname) {
        Integer count = counts.getOrDefault(modelname, 0);
        if(count != 0) {
            --count;
            counts.put(modelname, count);
        }
        return count;
    }
    public static Integer getCount(String modelname, Integer resultIfNone) {
        Integer count = counts.getOrDefault(modelname, resultIfNone);
        return count;
    }
    
    public static final String SUFFIX_VALIDATE = "/" + FormStages.validate;
    public static final String SUFFIX_SUBMIT = "/" + FormStages.submit;
    public static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    
    private final TypeTests typeTests;

    public TestUrls(TypeTests typeTests) {
        this.typeTests = Objects.requireNonNull(typeTests);
    }

    public Map getFormParameters(String url, String modelname) {
        final Map map = this.getParameters(url, modelname);
        final Map output = new HashMap();
        output.putAll(map);
        map.forEach((k, v) -> {
            final Class type = v == null ? null : v.getClass();
            if(v != null && type != null) {
                if(this.typeTests.isDomainType(type)) {
                    try{
                        final Object id = type.getMethod("getId").invoke(v);
                        if(id != null) {
                            LOG.debug("Updated to id: " + id + " from: " + v);
                            output.put(k, id);
                        }
                    }catch(Exception e) {
                        LOG.warn("Failed to extract id from: " + v + ", reason: " + e);
                    }
                }else if(java.util.Date.class.isAssignableFrom(type)) {
                
                    final java.util.Date date = (java.util.Date)v;
                    
                    output.put(k, new SimpleDateFormat(DATETIME_PATTERN).format(date));
                }
            }
        });
        return output;
    }
    
    public Map getParameters(String url, String modelname) {
        if(url.contains(TestUrls.SUFFIX_VALIDATE)) {
            final Object model = this.getModel(modelname);
            try{
                final Map map = this.toMap(model);
                return map;
            }catch(Exception e) {
                e.printStackTrace();
                return Collections.EMPTY_MAP;
            }
        }else{
            return Collections.EMPTY_MAP;
        }
    }
    
    public Object getModel(String modelname) {
        final Object model;
        if("blog".equalsIgnoreCase(modelname)) {
            Blog blog = new Blog();
            blog.setDescription("Sample blog description");
            blog.setEnabled(true);
            blog.setHandle("Sample Blog Handle");
//            blog.setImage();
            blog.setType(BlogType.FASHION);
            model = blog;
        }else if("post".equalsIgnoreCase(modelname)){
            final Integer blogCount = TestUrls.getCount("blog", 0);
            if(blogCount == 0) {
                throw new IllegalStateException("No blog has been created. Create a blog first");
            }
            Post post = new Post();
            post.setBlog(new Blog(blogCount));
            post.setContent("Sample post content");
            post.setTimeCreated(new Date());
            post.setTimeModified(new Date());
            post.setTitle("Sample post title");
            model = post;
        }else if("tag".equalsIgnoreCase(modelname)){    
            Tag tag = new Tag();
            tag.setName("Sample tag name");
            tag.setTimeCreated(new Date());
            model = tag;
        }else{    
            throw new UnsupportedOperationException(
                    "Unexpected model name: " + modelname);
        }
        return model;
    }
    
    public Map toMap(Object obj) {
        final Map output = new HashMap();
        final ConfigurablePropertyAccessor b = 
                PropertyAccessorFactory.forDirectFieldAccess(obj);
        final Field [] fields = obj.getClass().getDeclaredFields();
        for(Field field : fields) {
            final String name = field.getName();
            output.put(name, b.getPropertyValue(name));
        }
        LOG.trace("Extracted map: " + output + ", from object: " + obj);
        return output;
    }
    
    public Class getType(String modelname) {
        if("blog".equalsIgnoreCase(modelname)) {
            return Blog.class;
        }else if("post".equalsIgnoreCase(modelname)) {
            return Post.class;
        }else if("tag".equalsIgnoreCase(modelname)) {
            return Tag.class;
        }else if("blog_type".equalsIgnoreCase(modelname) || "blogtype".equalsIgnoreCase(modelname)) {
            return BlogType.class;
        }else {
            throw new IllegalArgumentException(modelname);
        }
    }

    public HttpMethod getHttpMethod(String url) {
        return this.isPost(url) ? HttpMethod.POST : HttpMethod.GET;
    }

    public boolean isPost(String url){
        return url.contains(TestUrls.SUFFIX_VALIDATE) || url.contains(TestUrls.SUFFIX_SUBMIT);
    }
    
    public String formUrl(int port, CRUDAction action, String modelname, String suffix) {
        if(suffix == null || "".equals(suffix)) {
            return this.formUrl(port, action, modelname, "", getModelId(action, modelname));
        }else{
            return this.formUrl(port, action, modelname, suffix, -1);
        }
    }
    
    public String formUrl(int port, CRUDAction action, String modelname, String suffix, int modelid) {
    
        if(suffix == null) {
            suffix = "";
        }
        
        String url = this.baseCrud(port, action, modelname) + suffix;
        
        if(modelid > -1) {
            
            url = url + '?' + Params.MODELID + '=' + modelid;
        }
        
        return url;
    }

    public Integer getModelId(CRUDAction action, String modelname) {
        if(this.requiresModelId(action)) {
            final Integer count = TestUrls.getCount(modelname, -1);
            if(count == -1) {
                throw new IllegalStateException("No " + modelname + ", has been created. Create one first");
            }
            return count;
        }else{
            return -1;
        }
    }
    
    public boolean requiresModelId(CRUDAction action) {
        return CRUDAction.read.equals(action) || 
                CRUDAction.update.equals(action) ||
                CRUDAction.delete.equals(action);
    }
    
    public String baseCrud(int port, CRUDAction action, String modelname) {
        return "http://localhost:" + port + "/"+action+'/'+modelname;
    }
}
