package com.daradat.boot.solutionization.logicTransition.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class BizModuleInfo {
    private String tenantId; //업무규칙이 적용될 tenant의 ID
    private String bizModuleId; //업무규칙의 ID
    private String altFlag; //업무규칙이 경우에 따라 실행되어야 할 경우 분기할 수 있는 값
    private String callType; //이미 지정된 호출 방식
    private String callHost; //업무 규칙 모듈의 Location 정보
    private String callInfo; //업무 규칙의 호출에 필요한 값
}
