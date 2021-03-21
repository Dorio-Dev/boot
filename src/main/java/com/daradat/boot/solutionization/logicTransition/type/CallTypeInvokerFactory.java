package com.daradat.boot.solutionization.logicTransition.type;

import com.daradat.boot.solutionization.logicTransition.model.BizModuleInfo;
import com.daradat.boot.solutionization.logicTransition.type.impl.*;

public class CallTypeInvokerFactory {
    /**
     * 생성금지
     */
    private CallTypeInvokerFactory() {}

    /**
     * {@link BizModuleInfo}로 업무규칙을 호출할 {@link ICallTypeInvoker}의 구현체를 찾아서 리턴한다.
     * {@link BizModuleInfo#getCallType()} 값은 {@link CallTypeEnum}에 미리 정의되어 있는 값중 하나여야 한다.
     */
    public ICallTypeInvoker createCallTypeHandler(BizModuleInfo info){
        return createCallTypeHandler(info.getCallType());
    }

    /**
     * callTypeString로 업무규칙을 호출할 {@link ICallTypeInvoker}의 구현체를 찾아서 리턴한다.
     * {@link BizModuleInfo#getCallType()} 값은 {@link CallTypeEnum}에 미리 정의되어 있는 값중 하나여야 한다.
     */
    public ICallTypeInvoker createCallTypeHandler(String callTypeString){
        if (callTypeString == null) {
            throw new RuntimeException("Given CALL_TYPE is null");
        }

        try {
            CallTypeEnum callType = CallTypeEnum.valueOf(callTypeString);
            switch (callType) {
                case BEAN_CALL:
                    return new BeanCallTypeInvoker();
                case INJAR_BEAN_CALL:
                    return new InjarBeanCallTypeInvoker();
                case LOCAL_CALL:
                    return new LocalCallTypeInvoker();
                case INJAR_CALL:
                    return new InjarCallTypeInvoker();
                case REST_GET:
                    return new RestGetCallTypeInvoker();
                case REST_POST:
                    return new RestPostCallTypeInvoker();
                case REST_PUT:
                    return new RestPutCallTypeInvoker();
                case REST_DELETE:
                    return new RestDeleteCallTypeInvoker();
                default:
                    throw new RuntimeException("Given CALL_TYPE defined in CallTypeEnum. but cann't create Call Type Handler : " + callTypeString);
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("No such CALL_TYPE : " + callTypeString, e);
        } catch (NullPointerException e) {
            throw new RuntimeException("Given CALL_TYPE may be null", e);
        } catch (Exception e) {
            throw new RuntimeException("Cann't create Call Type Handler :  " + callTypeString, e);
        }
    }

    /**
     * singleton으로 구성된 {@link CallTypeInvokerFactory} 인스턴스를 리턴한다.
     */
    public static CallTypeInvokerFactory getInstance(){
        return InstanceHolder.instance;
    }

    /**
     * singleton 인스턴스 구성체
     *
     */
    private static class InstanceHolder {

        private static final CallTypeInvokerFactory instance = new CallTypeInvokerFactory();

    }

}
