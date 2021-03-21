package com.daradat.boot.solutionization.logicTransition.type;

import com.daradat.boot.solutionization.logicTransition.model.BizModuleInfo;

import java.util.Map;

public interface ICallTypeInvoker {
    public Map<String, Object> executeCallType(BizModuleInfo info, Map<String, Object> inData);
}
