package org.vivi.framework.elasticsearch.simple.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.vivi.framework.elasticsearch.simple.model.Article;
import org.vivi.framework.elasticsearch.simple.repository.ArticleRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleSearchService {

    // ES 操作模版
    private final ElasticsearchOperations elasticsearchOperations;

    private final ArticleRepository articleRepository;

    public Article saveArticle(Article article) {
        return articleRepository.save(article);
    }

    /**
     * 批量新增
     * @return
     */
    public Iterable<Article> saveBatchArticles(Iterable<Article> articles) {
        return articleRepository.saveAll(articles);
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    /**
     * 全文搜索(标题+内容)
     */
    public List<Article> searchByKeyword(String keyword) {
        //构建查询对象
        Query query = NativeQuery.builder()
                .withQuery(QueryBuilders.multiMatch()
                        .query(keyword).fields("title", "content", "author")
                        .build()._toQuery()).build();
        //执行搜索
        SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);

        //装换为文章列表并返回
        return searchHits.getSearchHits()
                .stream()
                .map(searchHit -> searchHit.getContent())
                .toList();
    }

    /**
     * 高级搜索
     */
    public List<Article> searchByAdvanced(String keyword,String author) {
        // 构建复合条件：（标题或内容包含关键词）且作者等于指定值
        // 1、构建布尔查询
        BoolQuery boolQuery = QueryBuilders.bool()
                .must(QueryBuilders.queryString(q -> q.query(author).fields("author")))
                .must(QueryBuilders.queryString(q -> q.query(keyword).fields("title", "content")))
                .build();

        // 2、构建查询对象
        NativeQuery query = NativeQuery.builder()
                .withQuery(boolQuery._toQuery()).build();

        log.info("查询条件：{}", query.getQuery().toString());

        SearchHits<Article> searchHits = elasticsearchOperations.search(query, Article.class);
        return searchHits.getSearchHits()
                .stream()
                .map(searchHit -> searchHit.getContent())
                .toList();
    }
}
