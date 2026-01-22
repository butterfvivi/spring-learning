package org.vivi.framework.elasticsearch.simple.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.elasticsearch.simple.model.Article;
import org.vivi.framework.elasticsearch.simple.service.ArticleSearchService;

import java.util.List;

@RestController
@RequestMapping("/api/es")
@RequiredArgsConstructor
public class EsController {

    private final ArticleSearchService articleSearchService;

    /**
     * 新增文章
     */
    @PostMapping
    public Article saveArticle(@RequestBody Article article) {
        return articleSearchService.saveArticle(article);
    }

    /**
     * 批量新增文章
     */
    @PostMapping("/batch")
    public Iterable<Article> saveBatchArticles(@RequestBody List<Article> articles) {
        return articleSearchService.saveBatchArticles(articles);
    }

    /**
     * 全文搜索
     * @param keyword 搜索关键词
     * @return 文章列表
     */
    @GetMapping("/search")
    public List<Article> search(@RequestParam String keyword) {
        return articleSearchService.searchByKeyword(keyword);
    }

    /**
     * 高级搜索
     * @param keyword 关键词
     * @param author 作者
     * @return 文章列表
     */
    @GetMapping("/advanced-search")
    public List<Article> advancedSearch(@RequestParam String keyword, @RequestParam String author) {
        return articleSearchService.searchByAdvanced(keyword, author);
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleSearchService.delete(id);
    }

}
