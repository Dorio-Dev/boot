package com.daradat.boot.solutionization.logicTransition;

import com.daradat.boot.solutionization.logicTransition.model.BizModuleInfo;
import com.daradat.boot.solutionization.logicTransition.service.LogicTransitionService;
import com.daradat.boot.solutionization.logicTransition.type.CallTypeInvokerFactory;
import com.daradat.boot.solutionization.logicTransition.type.ICallTypeInvoker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogicTransition {
    private final LogicTransitionService logicTransitionService;
    /**
     * tenantId+bizRuleId+altFlag 로 구성된 업무규칙을 호출한다.
     *
     * @param tenantId
     * @param bizRuleId
     * @param altFlag
     * @param inData
     * @return
     */
    public Map<String, Object> execute(String tenantId, String bizRuleId, String altFlag, Map<String, Object> inData){
        BizModuleInfo info = getBizRuleInfo(tenantId, bizRuleId, altFlag);
        return execute(info, inData);
    }

    /**
     * BizRuleInfo의 tenantId+bizRuleId+altFlag 로 구성된 업무규칙을 호출한다.
     *
     * @param info
     * @param inData
     * @return
     */
    public Map<String, Object> execute(BizModuleInfo info, Map<String, Object> inData){
        ICallTypeInvoker handler = CallTypeInvokerFactory.getInstance().createCallTypeHandler(info);
        return handler.executeCallType(info, inData);
    }

    /**
     * tenantId+bizRuleId+altFlag 값으로 BizRuleInfo를 가져온다.
     * 필요시 Override하여 사용할 수도 있다.
     *
     * @param tenantId
     * @param bizRuleId
     * @param altFlag
     * @return
     */
    public BizModuleInfo getBizRuleInfo(String tenantId, String bizRuleId, String altFlag){
        return logicTransitionService.retrieveBizRuleInfo(tenantId, bizRuleId, altFlag);
    }

}
