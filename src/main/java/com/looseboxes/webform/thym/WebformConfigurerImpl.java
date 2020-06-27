package com.looseboxes.webform.thym;

import com.looseboxes.webform.config.WebformConfigurer;
import com.looseboxes.webform.thym.domain.Post;
import org.springframework.context.annotation.Configuration;
import com.looseboxes.webform.entity.EntityConfigurer;
import com.looseboxes.webform.entity.EntityConfigurerService;

/**
 * @author hp
 */
@Configuration
public class WebformConfigurerImpl implements WebformConfigurer{
    /**
     * This class could be used to initialize some defaults on the model object. 
     * For example we could set the current logged in user if there is a field and/
     * accessor methods for that on the model object.
     * 
     * In this case we do something mundane. We just set a default post title.
     */
    private static class PostPreconfigurer implements EntityConfigurer<Post>{
        @Override
        public Post configure(Post post) {
            post.setTitle("My Awesome Post");
            return post;
        }
    }

    @Override
    public void addModelObjectConfigurers(EntityConfigurerService service) {
        service.addConfigurer(Post.class, new PostPreconfigurer());
    }
}
