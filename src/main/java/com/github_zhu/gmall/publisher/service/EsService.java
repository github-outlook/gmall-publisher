package com.github_zhu.gmall.publisher.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: github_zhu
 * @Describtion:
 * @Date:Created in 2020/5/14 9:22
 * @ModifiedBy:
 */
@Service
public interface EsService {

    //日活总数的查询
    public Long getDauTotal(String date);

    //日活分时数据查询
    public Map getDauHour(String date);

}
