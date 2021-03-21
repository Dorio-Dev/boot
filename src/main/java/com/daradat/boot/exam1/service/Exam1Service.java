package com.daradat.boot.exam1.service;

import com.daradat.boot.framework.dataaccess.CommonDao;
import com.daradat.boot.solutionization.bizRuleTransition.BizRuleTransition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Exam1Service {

    private final CommonDao commonDao;

    private final BizRuleTransition bizRuleTransition;

    public Map retrieveTest(){
        Map result = new HashMap();

        Map parameters = new HashMap();
        parameters.put("tenantId", "L2100");
        parameters.put("bizRuleTypeCode", "A002");

        Map<String, Object> input = new HashMap();
        input.put("REQ_TYPE", "C");
        input.put("LIMIT_CD", "1500");

        parameters.put("input", input);
        result.put("Result", bizRuleTransition.execute(parameters));

        //result = commonDao.selectList("exam1.exam1.testsql");
        return result;
    }
}
