package com.mrxia.testlib.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.mrxia.testlib.domain.Subject;

/**
 * 科目对象数据库持久类
 * @author xiazijian
 */
public interface SubjectRepository extends CrudRepository<Subject, Integer> {

    List<Subject> findByType(Integer type);
}
