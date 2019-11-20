package com.wk;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class SolrJService {
    private String URL = "http://localhost:8080/solr";
    private HttpSolrServer solrServer;

    @Before
    public void init(){
        solrServer = new HttpSolrServer(URL);
    }

    @Test
    public void searchAll() throws SolrServerException {
        SolrQuery query = new SolrQuery();
        // 输入查询条件
        query.setQuery("*:*");
        // exceute query
        QueryResponse response = solrServer.query(query);
        SolrDocumentList solrDocuments = response.getResults();
        printResult(solrDocuments);
    }

    @Test
    public void searchId1() throws SolrServerException {
        SolrQuery query = new SolrQuery();
        query.setQuery("id:1");
        QueryResponse response = solrServer.query(query);
        int status = response.getStatus();
        int qTime = response.getQTime();
        String requestUrl = response.getRequestUrl();
        long elapsedTime = response.getElapsedTime();
        log.info("status={}, qTime={},resquetUrl={},elapsedTime={}",status,qTime,requestUrl,elapsedTime);
        SolrDocumentList results = response.getResults();
        printResult(results);
    }

    @Test
    public void MultiQuery() throws SolrServerException {
        SolrQuery query = new SolrQuery();
        // 此两个表达式 是一样的， 都是设置查询条件
        //query.setQuery("pname:*");
        query.set("q","pname:*");

        // 设置过滤条件
        //query.setFilterQueries("id:[2 TO 3]");
        query.set("fq","id:[2 TO 3]");

        // 设置排序
        query.setSort("id", SolrQuery.ORDER.desc);
        // 设置分页分析
        query.setStart(0);
        query.setRows(10);
        // 设置显示的域
        query.setFields("id,pname,paddress");
        // 设置默认域
        //query.set("df","id");
        // 设置高亮信息
        query.setHighlight(true);
        query.addHighlightField("pname");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");

        // 进行查询  并 获取结果
        QueryResponse response = solrServer.query(query);
        SolrDocumentList results = response.getResults();
        //printResult(results);
        // 显示高亮的信息
        Map<String, Map<String, List<String>>> highlighting =
                response.getHighlighting();
        for (SolrDocument document : results) {
            Object id = document.get("id");
            log.info("id = {}",id);
            List<String> pname = highlighting.get(id).get("pname");
            if (pname != null){
                log.info("hightlight field: {}",pname.toString());
            }else{
                log.info("pname = {}",document.get("pname"));
            }
            log.info("paddress={}",document.get("paddress"));
        }
    }

    @Test
    public void insertDocuemnt() throws IOException, SolrServerException {
        SolrInputDocument document = new SolrInputDocument();
        // 添加字段
        document.addField("id",5);
        document.addField("pname","王虹");
        document.addField("page","25");
        document.addField("paddress","BJGD");
        // 添加到server中
        solrServer.add(document);
        //
        solrServer.commit();
    }

    @Test
    public void deleteById() throws IOException, SolrServerException {
        solrServer.deleteById("4");
        solrServer.commit();
    }

    @Test
    public void deleteByQuery() throws IOException, SolrServerException {
        solrServer.deleteByQuery("pname:wk");
        solrServer.commit();
    }

    @Test
    public void deleteAll() throws IOException, SolrServerException {
        solrServer.deleteByQuery("*:*");
        solrServer.commit();
    }

    public void printResult(SolrDocumentList solrDocuments){
        long totalDoc = solrDocuments.getNumFound();
        log.info("find num:{}",totalDoc);
        for (SolrDocument document : solrDocuments) {
            log.info("-------------------start-------------------------");
            log.info("id = {}",document.get("id"));
            log.info("name = {}",document.get("pname"));
            log.info("age = {}",document.get("page"));
            log.info("address = {}",document.get("paddress"));
            log.info("-------------end---------------------");
            System.out.println();
            System.out.println();
        }

    }
}
