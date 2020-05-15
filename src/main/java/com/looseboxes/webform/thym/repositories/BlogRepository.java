package com.looseboxes.webform.thym.repositories;

import com.looseboxes.webform.thym.domain.Blog;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author hp
 */
public interface BlogRepository extends PagingAndSortingRepository<Blog, Integer>{
}
