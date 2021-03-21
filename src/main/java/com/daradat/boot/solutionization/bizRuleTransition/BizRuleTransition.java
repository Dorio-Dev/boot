package com.daradat.boot.solutionization.bizRuleTransition;

import com.daradat.boot.framework.dataaccess.CommonDao;
import com.daradat.boot.solutionization.bizRuleTransition.type.*;

import com.daradat.boot.solutionization.logicTransition.LogicTransition;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BizRuleTransition {

    private final CommonDao commonDao;

    private final LogicTransition logicTransition;

    public Object execute(Map parameters){


        //PROCESS_RESULT_TYPE
        Map bizType = commonDao.select("solutionization.bizRuleExecute.retrieveBizType", parameters);
        ProcessResultTypeEnum processResultType = ProcessResultTypeEnum.valueOf((String) bizType.get("PROCESS_RESULT_TYPE"));
        List<Map> bizRules = commonDao.selectList("solutionization.bizRuleExecute.retrieveBizRule", parameters);
        Object result = null;
        for(Map bizRule : bizRules){
            ProcessTypeEnum processType = ProcessTypeEnum.valueOf((String) bizRule.get("PROCESS_TYPE"));
            parameters.put("ruleId", bizRule.get("RULE_ID"));
            switch (processType){
                case VD:
                    result = executeExpression(bizRule, parameters);
                    if((Boolean)result && processResultType == ProcessResultTypeEnum.RI){
                        result = bizRule.get("RULE_ID");
                        return result;
                    }
                    break;
                case CL:
                    result = executeExpression(bizRule, parameters);
                    break;
                case MD:
                    result = executeModule(bizRule, parameters);
                    break;
                case RS:
                    result = replaceSentence(bizRule, parameters);
                    break;
            }
        }

        return result;
    }

    private Object replaceSentence(Map bizRule, Map parameters){
        /*
            문구 리턴
         */
        Map bizRuleConditions = commonDao.select("solutionization.bizRuleExecute.retrieveBizRuleCondition", parameters);

        return bizRuleConditions.get("COND_VALUE");
    }

    private Object executeModule(Map bizRule, Map parameters){
        /*
            문구 치환
         */
        Map bizRuleConditions = commonDao.select("solutionization.bizRuleExecute.retrieveBizRuleCondition", parameters);
        String[] moduleInfo = ((String) bizRuleConditions.get("COND_VALUE")).split(";");


        return logicTransition.execute((String) parameters.get("tenantId"), moduleInfo[0], moduleInfo[1], parameters);
    }

    private Object executeExpression(Map bizRule, Map parameters){
        List<Map> bizRuleConditions = commonDao.selectList("solutionization.bizRuleExecute.retrieveBizRuleCondition", parameters);
        /*
            산술식 생성
         */
        StringBuilder expression = new StringBuilder();

        Map input = (Map)parameters.get("input");

        for(Map bizRuleCondition : bizRuleConditions){
            if(bizRuleCondition.get("COND_TYPE_CODE") != null) {
                ConditionTypeEnum conditionTypeEnum = ConditionTypeEnum.valueOf((String) bizRuleCondition.get("COND_TYPE_CODE"));
                switch (conditionTypeEnum) {
                    case AN: //AND
                        expression.append(" &&");
                        break;
                    case OR: //OR
                        expression.append(" ||");
                        break;
                    case BS: // (
                        expression.append(" (");
                        break;
                    case BE: // )
                        expression.append(" )");
                        break;
                    case EQ: // =
                        expression.append(" ==");
                        break;
                    case NE:
                        expression.append(" !=");
                        break;
                    case GT:
                        expression.append(" >");
                        break;
                    case GE:
                        expression.append(" >=");
                        break;
                    case LT:
                        expression.append(" <");
                        break;
                    case LE:
                        expression.append(" <=");
                        break;
                    case PS: // +
                        expression.append(" +");
                        break;
                    case MN: // -
                        expression.append(" -");
                        break;
                    case MP: // *
                        expression.append(" *");
                        break;
                    case DV: // /
                        expression.append(" /");
                        break;
                    case NN: // not null
                        break;
                    case NU: // is null
                        break;
                }
            }else if(bizRuleCondition.get("COMP_ITEM_ID") != null && !"".equals(bizRuleCondition.get("COMP_ITEM_ID"))){
                if("String".equalsIgnoreCase((String) bizRuleCondition.get("ITEM_DATA_TYPE"))){
                    expression.append(" '").append(input.get(bizRuleCondition.get("COMP_ITEM_ID"))).append("'");;
                }else{
                    expression.append(" ").append(input.get(bizRuleCondition.get("COMP_ITEM_ID")));;
                }
            }else if(bizRuleCondition.get("COND_VALUE") != null && !"".equals(bizRuleCondition.get("COND_VALUE"))){
                boolean isNumeric = false;
                if(StringUtils.isNumeric((String) bizRuleCondition.get("COND_VALUE"))){
                    isNumeric = true;
                }
                if(isNumeric){
                    expression.append(" ").append((String)bizRuleCondition.get("COND_VALUE"));
                }else{
                    expression.append(" '").append((String)bizRuleCondition.get("COND_VALUE")).append("'");
                }
            }
        }

        /*
            js엔진 사용하여 expression 실행
         */
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");
        System.out.println("expression : " +expression.toString());
        Object result = null;
        try {
            result = scriptEngine.eval(expression.toString());
            System.out.println("result : " +result);
            return result;
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return result;
    }
    public static void temp() throws ScriptException {
        /*
            test input param 세팅
         */
        Map<String, Object> inp = new HashMap<>();
        inp.put("REQ_TYPE", "B");
        inp.put("LIMIT_CD", "2500");
        //TODO RuleId 필요.

        /*
            Test Record 생성 -> Rule ID를 가지고 DB에서 조회해오는 Rule 조건 정보
         */
        List<Map> dataset = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        row.put("seq", 1);
        row.put("logicalOperator", null);
        row.put("startBracket", true);
        row.put("condType", "String");
        row.put("condCd", "REQ_TYPE");
        row.put("condOperator", "==");
        row.put("condValue", "B");
        row.put("endBracket", false);
        dataset.add(row);
        row = new HashMap<>();
        row.put("seq", 2);
        row.put("logicalOperator", "AND");
        row.put("startBracket", false);
        row.put("condType", "int");
        row.put("condCd", "LIMIT_CD");
        row.put("condOperator", ">");
        row.put("condValue", "2000");
        row.put("endBracket", true);
        dataset.add(row);


        /*
            Rule 생성
         */
        StringBuilder rule = new StringBuilder();

        for(Map r : dataset){
            if(r.get("logicalOperator") != null){
                switch ((String)r.get("logicalOperator")){
                    case "AND":
                        rule.append(" &&");
                        break;
                    case "OR":
                        rule.append(" ||");
                        break;
                }
            }
            if((boolean)r.get("startBracket")){
                rule.append(" (");
            }

            if("String".equalsIgnoreCase((String) r.get("condType"))){
                rule.append(" '").append(inp.get(r.get("condCd"))).append("'");
            }else if("int".equalsIgnoreCase((String) r.get("condType"))){
                rule.append(" ").append(inp.get(r.get("condCd")));
            }

            rule.append(" ").append(r.get("condOperator"));
            if("String".equalsIgnoreCase((String) r.get("condType"))){
                rule.append(" '").append(r.get("condValue")).append("'");
            }else if("int".equalsIgnoreCase((String) r.get("condType"))){
                rule.append(" ").append(r.get("condValue"));
            }

            if((boolean)r.get("endBracket")){
                rule.append(" )");
            }
        }


        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");

        System.out.println("rule : " +rule.toString());
        Object result =  scriptEngine.eval(rule.toString());
        System.out.println("result : " +result);

        //TODO return result
    }
}
