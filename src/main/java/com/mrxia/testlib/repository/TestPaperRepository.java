package com.mrxia.testlib.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrxia.testlib.domain.TestPaper;

/**
 * 试卷对象数据库持久类
 * @author xiazijian
 */
public interface TestPaperRepository extends JpaRepository<TestPaper, Integer> {
}
