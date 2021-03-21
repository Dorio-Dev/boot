package com.daradat.boot.sample;

import com.daradat.boot.solutionization.logicTransition.IDoBizRule;
import com.daradat.boot.solutionization.logicTransition.model.BizModuleInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EchoExample implements IDoBizRule {

    public Map<String, Object> executeBizRule(BizModuleInfo info, Map<String, Object> inData) {
        Map<String, Object> result = new HashMap<>();
        result.put("Echo", inData.get("input"));

        return result;
    }
}
