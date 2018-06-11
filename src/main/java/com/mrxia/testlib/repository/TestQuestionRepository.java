package com.mrxia.testlib.repository;

import org.springframework.data.repository.CrudRepository;

import com.mrxia.testlib.domain.TestQuestion;

/**
 * 试题对象数据库持久类
 * @author xiazijian
 */
public interface TestQuestionRepository extends CrudRepository<TestQuestion, Integer> {
}
