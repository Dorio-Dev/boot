package com.daradat.boot.solutionization.logicTransition.type.impl;

import com.daradat.boot.solutionization.logicTransition.IDoBizRule;
import com.daradat.boot.solutionization.logicTransition.model.BizModuleInfo;
import com.daradat.boot.solutionization.logicTransition.reflect.SimpleReflection;
import com.daradat.boot.solutionization.logicTransition.type.ICallTypeInvoker;

import java.util.Map;

/**
 * 현재 Binding 되어 있는 ClassPath로 부터 Class를 Instance화 하여 호출한다.
 * 호출 대상이 되는 Class는 {@link IDoBizRule}를 구현한 구현체여야 한다.
 *
 * {@link BizModuleInfo#getCallInfo()} 값에 package를 포함한 Class 명을 기준으로 인스턴스화 한다.
 */

public class LocalCallTypeInvoker implements ICallTypeInvoker {
    public Map<String, Object> executeCallType(BizModuleInfo info, Map<String, Object> inData){
        IDoBizRule bizRule = new SimpleReflection().createInstance(info.getCallInfo());
        return bizRule.executeBizRule(info, inData);
    }
}
