//package org.vivi.framework.elasticsearch.common.util;
//
//import jakarta.annotation.Resource;
//import lombok.extern.slf4j.Slf4j;
//import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
//import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
//import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
//import org.elasticsearch.action.support.master.AcknowledgedResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.client.indices.CreateIndexRequest;
//import org.elasticsearch.client.indices.CreateIndexResponse;
//import org.elasticsearch.client.indices.GetIndexRequest;
//import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.elasticsearch.common.xcontent.XContentFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Component
//public class EsUtils {
//
//    private static final String COMMA = ",";
//
//    @Resource
//    private RestHighLevelClient restHighLevelClient;
//
//    public void assertIndex(String index){
//        Assert.hasText(index, "index must not be null");
//    }
//
//    public void assertIndexAndId(String index,String id){
//        Assert.hasText(index, "index must not be null");
//        Assert.hasText(id, "id must not be null");
//    }
//
//    public void assertDocument(String index,Object doc){
//        Assert.hasText(index, "index must not be null");
//        Assert.notNull(doc, "doc must not be null");
//    }
//
//    public void assertIndexAndList(String index, List<?> documents){
//        Assert.hasText(index, "index must not be null");
//        Assert.notEmpty(documents, "documents must not be null");
//    }
//
//    public void assertDocument(String index,String id,Object doc){
//        Assert.hasText(index, "index must not be null");
//        Assert.hasText(id, "id must not be null");
//        Assert.notNull(doc, "doc must not be null");
//    }
//
//    /**
//     * 判断索引是否存在
//     * @param index
//     * @return
//     */
//    public boolean existIndex(String index){
//        assertIndex(index);
//
//        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
//        //主节点检索信息
//        getIndexRequest.local(false);
//        //返回结果为适合人类的格式
//        getIndexRequest.humanReadable(true);
//        //返回每个索引的所有默认设置
//        getIndexRequest.includeDefaults(false);
//
//        try {
//            return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
//        } catch (Exception e) {
//            log.error("[Elasticsearch] >> existsIndex method exception,index:{},error message:{}", index, e.getMessage(), e);
//            return false;
//        }
//    }
//
//    /**
//     * 创建索引
//     * @param index
//     * @return
//     */
//    public boolean checkIndex(String index){
//        assertIndex(index);
//
//        if (existIndex(index)){
//            log.warn("Index:[{}] is exits!", index);
//            return true;
//        }
//        //创建索引请求
//        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
//        try {
//            //执行客户端请求
//            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//            return createIndexResponse.isAcknowledged();
//        } catch (IOException e) {
//            log.error("[Elasticsearch] >> createIndex method exception,index:{},error message:{}", index, e.getMessage(), e);
//            return false;
//        }
//
//    }
//
//    public boolean createIndex(String index, Map<String,Map<String,Object>> properties,Integer shards,Integer replicas){
//        assertIndex(index);
//
//        if (existIndex(index)){
//            log.warn("Index:[{}] is exits!", index);
//            return true;
//        }
//
//        try {
//            XContentBuilder  xContentBuilder = XContentFactory.jsonBuilder();
//            xContentBuilder.startObject()
//                    .startObject("mappings")
//                    .field("properties", shards)
//                    .endObject()
//                    .startObject("settings")
//                    //设置分片数
//                    .field("number_of_shards", shards)
//                    //设置副本数
//                    .field("number_of_replicas", replicas)
//                    .endObject()
//                    .endObject();
//
//            //创建索引请求
//            CreateIndexRequest request = new CreateIndexRequest(index).source(xContentBuilder);
//            //执行客户端请求
//
//            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
//            return createIndexResponse.isAcknowledged();
//        } catch (IOException e) {
//            log.error("[Elasticsearch] >> createIndex method exception,index:{},properties:{},error message:{}", index, properties, e.getMessage(), e);
//            return false;
//        }
//    }
//
//    /**
//     * 删除索引
//     * @param index
//     * @return
//     */
//    public boolean deleIndex(String index){
//        assertIndex(index);
//
//        if (!existIndex(index)){
//            log.warn("Index:[{}] is not exits!", index);
//            return true;
//        }
//        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
//        try {
//            //删除索引请求
//            AcknowledgedResponse response = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//            return response.isAcknowledged();
//        } catch (IOException e) {
//            log.error("[Elasticsearch] >> deleIndex method exception,index:{},error message:{}", index, e.getMessage(), e);
//            return false;
//        }
//    }
//
//    /**
//     * 获取索引设置
//     * @param index
//     * @param index
//     * @return
//     */
//    public GetSettingsResponse  getIndexSetting(String index){
//        assertIndex(index);
//
//        if (!existIndex(index)){
//            log.warn("Index:[{}] is not exits!", index);
//            return null;
//        }
//
//        GetSettingsRequest request = new GetSettingsRequest().indices(index);
//        try {
//            return restHighLevelClient.indices().getSettings(request,RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            log.error("[Elasticsearch] >> getSettings method exception,index:{},error message:{}", index, e.getMessage(), e);
//            return null;
//        }
//    }
//}
