package com.daradat.boot.exam1.service;

import com.daradat.boot.framework.dataaccess.CommonDao;
import com.daradat.boot.solutionization.bizRuleTransition.BizRuleTransition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Exam1Service {

    private final CommonDao commonDao;

    private final BizRuleTransition bizRuleTransition;

    public Map retrieveTest(Map param) throws ParseException {
        Map result = new HashMap();

        result.put("Result", bizRuleTransition.execute(param));

        return result;
    }
}
