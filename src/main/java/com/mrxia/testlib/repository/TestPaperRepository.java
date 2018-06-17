package com.mrxia.testlib.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mrxia.testlib.domain.Subject;
import com.mrxia.testlib.domain.TestPaper;

/**
 * 试卷对象数据库持久类
 * @author xiazijian
 */
public interface TestPaperRepository extends JpaRepository<TestPaper, Integer> {

   Optional<TestPaper> findBySubjectAndId(Subject subject, Integer id);
}
