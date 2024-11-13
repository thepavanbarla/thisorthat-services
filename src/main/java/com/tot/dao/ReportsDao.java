package com.tot.dao;

import com.tot.pojo.Reports;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author karthik on 02/07/22.
 * @project totservices
 */


@Repository public interface ReportsDao extends MongoRepository<Reports, String> {


}



