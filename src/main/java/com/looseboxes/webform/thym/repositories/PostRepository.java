package com.looseboxes.webform.thym.repositories;

import com.looseboxes.webform.thym.domain.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author hp
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Integer>{
    
}
