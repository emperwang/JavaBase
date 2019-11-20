package com.wk;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Test;

@Slf4j
public class SolrJService {
    private String URL = "http://localhost:8080/solr";


    @Test
    public void searchAll() throws SolrServerException {
        HttpSolrServer solrServer = new HttpSolrServer(URL);
        SolrQuery query = new SolrQuery();
        // 输入查询条件
        query.setQuery("*:*");
        // exceute query
        QueryResponse response = solrServer.query(query);
        SolrDocumentList solrDocuments = response.getResults();
        long totalDoc = solrDocuments.getNumFound();
        log.info("find num:{}",totalDoc);
        for (SolrDocument document : solrDocuments) {
            log.info("id = {}",document.get("id"));
            log.info("name = {}",document.get("pname"));
            log.info("age = {}",document.get("page"));
            log.info("address = {}",document.get("paddress"));
        }
    }
}
