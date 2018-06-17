package com.mrxia.testlib.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mrxia.common.http.UrlParser;
import com.mrxia.testlib.domain.Subject;
import com.mrxia.testlib.domain.TestPaper;
import com.mrxia.testlib.domain.User;
import com.mrxia.testlib.repository.SubjectRepository;
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

    @Autowired
    public TestLibServiceImpl(RemoteService remoteService,
                              UserRepository userRepository,
                              SubjectRepository subjectRepository) {
        this.remoteService = remoteService;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
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
    public Page<TestPaper> pageTestPager(Integer subjectId, Pageable pageable) {
        return null;
    }
}
