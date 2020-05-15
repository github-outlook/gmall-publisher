package com.github_zhu.gmall.publisher.service.impl;

import com.github_zhu.gmall.publisher.service.EsService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: github_zhu
 * @Describtion:
 * @Date:Created in 2020/5/14 9:23
 * @ModifiedBy:
 */
@Service
public class EsServiceImpl implements EsService {

    @Autowired
    JestClient jestClient;

    @Override
    public Long getDauTotal(String date) {
        //创建query
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(new MatchAllQueryBuilder());
        String query = searchSourceBuilder.toString();
        //
        date = date.replace("-", "");
        String indexName = "gmall_dau_info_" + date + "-query";

        Search search = new Search.Builder(query).addIndex(indexName).addType("_doc").build();
        Long total = 0L;
        try {
            SearchResult searchResult = jestClient.execute(search);
            total = searchResult.getTotal();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ES 查询异常");
        }
        return total;
    }

   /* @Override
    public Map getDauHour(String date) {

        //创建query
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder aggregationBuilders = AggregationBuilders.terms("groupby_hr").field("hr.keyword").size(24);
        searchSourceBuilder.aggregation(aggregationBuilders);
        String query = searchSourceBuilder.toString();
        System.out.println(query);
        //
        date = date.replace("-", "");
        String indexName = "gmall_dau_info_" + date ;
        System.out.println(indexName);

        Search search = new Search.Builder(query).addIndex(indexName).addType("_doc").build();
        HashMap<String, Long> aggsMap = new HashMap<>();
        try {
            SearchResult searchResult = jestClient.execute(search);
            List<TermsAggregation.Entry> buckets = searchResult.getAggregations().getTermsAggregation("groupby_hr").getBuckets();
            for (TermsAggregation.Entry bucket : buckets) {
                aggsMap.put(bucket.getKey(), bucket.getCount());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ES 查询异常");
        }

        return aggsMap;
    }*/

    public static void main(String[] args) {
        new EsServiceImpl().getDauHour("2020-05-14");
    }

    @Override
    public Map getDauHour(String date) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("groupby_hr").field("hr").size(24);
        searchSourceBuilder.aggregation(aggregationBuilder);

        String query = searchSourceBuilder.toString();

        date=date.replace("-","");
        String indexName="gmall_dau_info_"+date+"-query";

        Search search = new Search.Builder(query).addIndex(indexName).addType("_doc").build();
        Map aggsMap=new HashMap();
        try {
            SearchResult searchResult = jestClient.execute(search);
            List<TermsAggregation.Entry> buckets = searchResult.getAggregations().getTermsAggregation("groupby_hr").getBuckets();
            for (TermsAggregation.Entry bucket : buckets) {
                aggsMap.put(  bucket.getKey(),bucket.getCount());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException("查询ES异常");
        }

        return aggsMap;
    }
}














