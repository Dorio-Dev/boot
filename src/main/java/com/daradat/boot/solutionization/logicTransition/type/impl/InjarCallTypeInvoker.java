package com.daradat.boot.solutionization.logicTransition.type.impl;

import com.daradat.boot.solutionization.logicTransition.IDoBizRule;
import com.daradat.boot.solutionization.logicTransition.model.BizModuleInfo;
import com.daradat.boot.solutionization.logicTransition.reflect.SimpleReflection;
import com.daradat.boot.solutionization.logicTransition.type.ICallTypeInvoker;

import java.util.Map;

public class InjarCallTypeInvoker implements ICallTypeInvoker {
    public Map<String, Object> executeCallType(BizModuleInfo info, Map<String, Object> inData){
        IDoBizRule bizRule = new SimpleReflection().createInstance(info.getCallHost(), info.getCallInfo());
        return bizRule.executeBizRule(info, inData);
    }
}
