package com.mrxia.testlib.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.mrxia.testlib.domain.Subject;
import com.mrxia.testlib.domain.TestPaper;
import com.mrxia.testlib.service.HtmlParseService;

/**
 * @author xiazijian
 */
@Service
public class HtmlParseServiceImpl implements HtmlParseService {

    private static final Pattern ON_CLICK_FUNCTION = Pattern.compile("enterkt\\((\\d+)\\)");

    @Override
    public Collection<Subject> parseSubjectList(String html) {

        Document root = Jsoup.parse(html);
        Elements subjectElements = root.select("span[id^=pg]");
        Elements testPaperListElements = root.select("div#shijuanlistdiv ul");

        int subjectSize = Math.min(subjectElements.size(), testPaperListElements.size());

        List<Subject> subjects = new ArrayList<>(subjectSize);
        Element sElement;
        for (int i = 0; i < subjectSize; i++) {
            Subject subject = new Subject();
            sElement = subjectElements.get(i);
            subject.setName(sElement.text());

            subject.setTestPapers(testPaperListElements.get(i).childNodes().stream()
                    .map(this::parseTestPaper)
                    .collect(Collectors.toList()));

            subjects.add(subject);
        }

        return subjects;
    }

    private TestPaper parseTestPaper(Node node) {
        TestPaper tp = new TestPaper();
        tp.setName(node.childNode(0).toString());
        String onclick = node.childNode(1).attr("onclick");
        Matcher matcher = ON_CLICK_FUNCTION.matcher(onclick);
        if (matcher.find()) {
            tp.setId(Integer.parseInt(matcher.group(1)));
        }
        return tp;
    }
}
