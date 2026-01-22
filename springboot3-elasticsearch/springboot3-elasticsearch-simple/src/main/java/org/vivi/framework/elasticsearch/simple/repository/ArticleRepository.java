package org.vivi.framework.elasticsearch.simple.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.vivi.framework.elasticsearch.simple.model.Article;

/**
 *
 */
@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {
}
