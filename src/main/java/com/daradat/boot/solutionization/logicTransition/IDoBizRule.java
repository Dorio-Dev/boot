package com.daradat.boot.solutionization.logicTransition;

import com.daradat.boot.solutionization.logicTransition.model.BizModuleInfo;

import java.util.Map;

public interface IDoBizRule {
    /**
     * @param info 참고용을 전달되는 업무규칙 정의 정보
     * @param inData 처리에 필요한 input Data
     * @return 처리한 결과 output Data
     */
    public Map<String, Object> executeBizRule(BizModuleInfo info, Map<String, Object> inData);

}
