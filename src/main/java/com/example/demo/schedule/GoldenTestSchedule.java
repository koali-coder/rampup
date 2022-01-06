package com.example.demo.schedule;

import com.example.demo.entity.GoldenTest;
import com.example.demo.entity.GoldenTestLog;
import com.example.demo.service.IGoldenTestLogService;
import com.example.demo.service.IGoldenTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.UUID;

/**
 * @author zhouyw
 * @date 2021-06-30
 * @describe golden test
 */
@Slf4j
@Component
public class GoldenTestSchedule {

    @Autowired
    private IGoldenTestService testService;

    @Autowired
    private IGoldenTestLogService testLogService;

    @PostConstruct
    public void init() {
        // 获取自检列表
        List<GoldenTest> goldenTestList = testService.list();
        log.info("golden test list size {}", goldenTestList.size());
        for (GoldenTest goldenTest : goldenTestList) {
            log.info("golden test url {}", goldenTest.getUrl());
            // 请求接口，获取接口返回数据
            String realityData = getResult(goldenTest);

            GoldenTestLog testLog = new GoldenTestLog();
            testLog.setCode(UUID.randomUUID().toString());
            testLog.setGoldenTestCode(goldenTest.getCode());
            testLog.setUrl(goldenTest.getUrl());
            testLog.setParam(goldenTest.getParam());
            testLog.setExpectedData(goldenTest.getExpectedData());
            testLog.setRealityData(realityData);

            // 比较接口返回结果与预期返回结果是否一致
            if (realityData.equals(goldenTest.getExpectedData())) {
                log.info("url {} : success", goldenTest.getUrl());
                testLog.setStatus(0);
            } else {
                log.info("url {} : fail", goldenTest.getUrl());
                testLog.setStatus(1);
            }

            testLogService.save(testLog);
        }
    }

    private String getResult(GoldenTest goldenTest) {
        // do url
        return null;
    }

}
