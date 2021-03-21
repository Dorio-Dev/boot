package com.daradat.boot.solutionization.logicTransition.type.impl;

import com.daradat.boot.solutionization.logicTransition.model.BizModuleInfo;
import com.daradat.boot.solutionization.logicTransition.okhttp.OKHttpRestAPI;
import com.daradat.boot.solutionization.logicTransition.type.ICallTypeInvoker;

import java.util.Map;

public class RestPutCallTypeInvoker implements ICallTypeInvoker {
    public Map<String, Object> executeCallType(BizModuleInfo info, Map<String, Object> inData){
        Map<String, Object> outData = new OKHttpRestAPI().doPut(info.getCallHost() + "/" + info.getCallInfo(), inData);
        return outData;
    }
}
