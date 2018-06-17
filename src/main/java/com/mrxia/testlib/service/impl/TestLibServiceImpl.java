package com.mrxia.testlib.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.mrxia.common.bean.BeanMapperFactory;
import com.mrxia.common.http.UrlParser;
import com.mrxia.testlib.domain.Subject;
import com.mrxia.testlib.domain.TestPaper;
import com.mrxia.testlib.domain.TestQuestion;
import com.mrxia.testlib.domain.User;
import com.mrxia.testlib.repository.SubjectRepository;
import com.mrxia.testlib.repository.TestPaperRepository;
import com.mrxia.testlib.repository.UserRepository;
import com.mrxia.testlib.service.RemoteService;
import com.mrxia.testlib.service.TestLibService;

/**
 * @author xiazijian
 */
@Service
public class TestLibServiceImpl implements TestLibService {

    /**
     * 科目类型参数key
     */
    private static final String SUBJECT_TYPE_KEY = "kemuid";

    /**
     * 正大题库远程接口相关服务
     */
    private final RemoteService remoteService;

    private final UserRepository userRepository;

    private final SubjectRepository subjectRepository;

    private final TestPaperRepository testPaperRepository;

    public TestLibServiceImpl(RemoteService remoteService,
                              UserRepository userRepository,
                              SubjectRepository subjectRepository,
                              TestPaperRepository testPaperRepository) {
        this.remoteService = remoteService;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.testPaperRepository = testPaperRepository;
    }

    @Override
    public User login(String username, String password) {

        User user = userRepository.findUserByUsername(username);

        String sessionId;
        if (user != null) {
            sessionId = remoteService.login(user.getUsername(), user.getPassword());
            if (sessionId == null) {
                return null;
            }
            user.setZhengdaSessionId(sessionId);
            return user;
        }

        // 如果数据库中用户不存在，则进行用户登录
        sessionId = remoteService.login(username, password);
        if (sessionId != null) {

            String address = remoteService.getSubjectSelectAddress(sessionId);
            String subjectType = UrlParser.of(address).queryMap().get(SUBJECT_TYPE_KEY);

            user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setSubjectType(Integer.valueOf(subjectType));
            user.setZhengdaSessionId(sessionId);
            userRepository.save(user);
        }
        return user;
    }

    @Override
    @Transactional
    public List<Subject> listSubject(final User user) {

        List<Subject> subjectList = subjectRepository.findByType(user.getSubjectType());
        if (!subjectList.isEmpty()) {
            return subjectList;
        }

        subjectList = remoteService.getSubjectList(user.getZhengdaSessionId());
        if (!subjectList.isEmpty()) {
            subjectList.forEach(subject -> {
                subject.setType(user.getSubjectType());
                subject.getTestPapers().parallelStream().forEach(tp -> tp.setSubject(subject));
                subjectRepository.save(subject);
            });
        }

        return subjectList;
    }

    @Override
    @Transactional
    public TestPaper getTestPaper(User user, Integer subjectId, Integer paperId) {

        Optional<Subject> subject = subjectRepository.findById(subjectId);
        Assert.isTrue(subject.isPresent(), "试卷科目不存在");

        Optional<TestPaper> optional = testPaperRepository.findBySubjectAndId(subject.get(), paperId);

        Assert.isTrue(optional.isPresent(), "试卷不存在");
        TestPaper testPaper = optional.get();

        // 如果试题不存在，则请求远程，获取数据并保存数据库
        if (testPaper.getQuestions().isEmpty()) {
            BeanMapperFactory.getMapper()
                    .map(remoteService.selectTestPaper(user.getZhengdaSessionId(), subject.get().getType(), paperId), testPaper);
            for (TestQuestion question : testPaper.getQuestions()) {
                question.setTestPaper(testPaper);
            }
            testPaperRepository.save(testPaper);
        }
        return testPaper;
    }
}
