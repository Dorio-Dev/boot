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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BizRuleTransition {

    private final CommonDao commonDao;

    private final LogicTransition logicTransition;

    public Object execute(Map parameters) throws ParseException {
        //PROCESS_RESULT_TYPE
        Map bizType = commonDao.select("solutionization.bizRuleExecute.retrieveBizType", parameters);
        ProcessResultTypeEnum processResultType = ProcessResultTypeEnum.valueOf((String) bizType.get("PROCESS_RESULT_TYPE"));
        List<Map> bizRules = commonDao.selectList("solutionization.bizRuleExecute.retrieveBizRule", parameters);
        Object result = null;
        for(Map bizRule : bizRules){
            ProcessTypeEnum processType = ProcessTypeEnum.valueOf((String) bizRule.get("PROCESS_TYPE"));
            parameters.put("processResultType", processResultType);
            parameters.put("ruleId", bizRule.get("RULE_ID"));
            switch (processType){
                case VD:
                    if(processResultType == ProcessResultTypeEnum.BL){
                        result = executeExpression(bizRule, parameters);
                        if(!(Boolean)result){
                            return false;
                        }
                    }else if(processResultType == ProcessResultTypeEnum.RI){
                        result = executeExpression(bizRule, parameters);
                        if((Boolean)result){
                            result = bizRule.get("RULE_ID");
                            return result;
                        }
                    }
                    break;
                case CL:
                case AS:
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
            ?????? ??????
         */
        Map bizRuleConditions = commonDao.select("solutionization.bizRuleExecute.retrieveBizRuleCondition", parameters);

        return bizRuleConditions.get("COND_VALUE");
    }

    private Object executeModule(Map bizRule, Map parameters){
        /*
            ?????? ?????? ?????? ????????? ???????????? ?????? ???????????? ?????? ??????
         */
        Map bizRuleConditions = commonDao.select("solutionization.bizRuleExecute.retrieveBizRuleCondition", parameters);
        String[] moduleInfo = ((String) bizRuleConditions.get("COND_VALUE")).split(";");


        return logicTransition.execute((String) parameters.get("tenantId"), moduleInfo[0], moduleInfo[1], parameters);
    }

    private Object executeExpression(Map bizRule, Map parameters) throws ParseException {
        List<Map> bizRuleConditions = commonDao.selectList("solutionization.bizRuleExecute.retrieveBizRuleCondition", parameters);
        /*
            ????????? ??????
         */
        StringBuilder expression = new StringBuilder();

        /*
        DATE TYPE ?????? ?????? ?????? ??????
         */
        Map input = (Map)parameters.get("input");
        Map<String, DateFormat> dateFormatMap = new HashMap<>();
        dateFormatMap.put("DY", new SimpleDateFormat("yyyy"));
        dateFormatMap.put("DYM", new SimpleDateFormat("yyyyMM"));
        dateFormatMap.put("DYMD", new SimpleDateFormat("yyyyMMdd"));
        Boolean dateAsYn = false;
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = null;
        DateFormat currentDateFormat = null;
        byte addOperation = 1;


        for(Map bizRuleCondition : bizRuleConditions){
            if(bizRuleCondition.get("COND_TYPE_CODE") != null && !"".equals(bizRuleCondition.get("COND_TYPE_CODE"))) {
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
                        if(dateAsYn){
                            addOperation = -1;
                        }
                        expression.append(" -");
                        break;
                    case MP: // *
                        expression.append(" *");
                        break;
                    case DV: // /
                        expression.append(" /");
                        break;
                    case NN: // not null
                        expression.append(" != 'null'");
                        break;
                    case NU: // is null
                        expression.append(" == 'null'");
                        break;
                    case AS: //Assign
                        expression.append(" <-");
                        break;
                }
            }else if(bizRuleCondition.get("COMP_ITEM_ID") != null && !"".equals(bizRuleCondition.get("COMP_ITEM_ID"))){
                //Assign ??? ????????? Date type(DY, DYM, DYMD) ?????? ?????? ?????? ????????? ?????? date ?????? ??????
                if(dateAsYn){
                    currentDate = currentDateFormat.parse((String) input.get(bizRuleCondition.get("COMP_ITEM_ID")));
                    currentCalendar.setTime(currentDate);
                }
                if("DY".startsWith((String) bizRuleCondition.get("ITEM_DATA_TYPE")) && "Y".equals(bizRuleCondition.get("ASSIGN_YN"))){
                    //Assign?????? ??? date ???????????? ?????? ????????? ?????? DateFormat ??????
                    dateAsYn = true;
                    currentDateFormat =dateFormatMap.get(bizRuleCondition.get("ITEM_DATA_TYPE"));
                }
                if("Y".equals(bizRuleCondition.get("ASSIGN_YN"))) {
                    //type.put(bizRuleCondition.get("COMP_ITEM_ID"), bizRuleCondition.get("ITEM_DATA_TYPE"));
                    expression.append(bizRuleCondition.get("COMP_ITEM_ID"));
                }else if("String".equalsIgnoreCase((String) bizRuleCondition.get("ITEM_DATA_TYPE"))){
                    expression.append(" '").append(input.get(bizRuleCondition.get("COMP_ITEM_ID"))).append("'");;
                }else if(dateAsYn && "DY".startsWith((String) bizRuleCondition.get("ITEM_DATA_TYPE"))) {
                    if("yyyy".equals(((SimpleDateFormat)currentDateFormat).toPattern())){
                        currentCalendar.add(Calendar.YEAR, (int)input.get(bizRuleCondition.get("COMP_ITEM_ID")) * addOperation);
                    }else if("yyyyMM".equals(((SimpleDateFormat)currentDateFormat).toPattern())){
                        currentCalendar.add(Calendar.MONTH, (int)input.get(bizRuleCondition.get("COMP_ITEM_ID")) * addOperation);
                    }else if("yyyyMMdd".equals(((SimpleDateFormat)currentDateFormat).toPattern())){
                        currentCalendar.add(Calendar.DATE, (int)input.get(bizRuleCondition.get("COMP_ITEM_ID")) * addOperation);
                    }
                }else {
                    expression.append(" ").append(input.get(bizRuleCondition.get("COMP_ITEM_ID")));;
                }
            }else if(bizRuleCondition.get("COND_VALUE") != null && !"".equals(bizRuleCondition.get("COND_VALUE"))){
                boolean isNumeric = false;
                if(StringUtils.isNumeric((String) bizRuleCondition.get("COND_VALUE"))){
                    isNumeric = true;
                }
                if(isNumeric){
                    if(dateAsYn) {
                        if("yyyy".equals(((SimpleDateFormat)currentDateFormat).toPattern())){
                            currentCalendar.add(Calendar.YEAR, (int)bizRuleCondition.get("COND_VALUE") * addOperation);
                        }else if("yyyyMM".equals(((SimpleDateFormat)currentDateFormat).toPattern())){
                            currentCalendar.add(Calendar.MONTH, (int)bizRuleCondition.get("COND_VALUE") * addOperation);
                        }else if("yyyyMMdd".equals(((SimpleDateFormat)currentDateFormat).toPattern())){
                            currentCalendar.add(Calendar.DATE, (int)bizRuleCondition.get("COND_VALUE") * addOperation);
                        }
                    }else {
                        expression.append(" ").append((String)bizRuleCondition.get("COND_VALUE"));
                    }
                }else{
                    expression.append(" '").append((String)bizRuleCondition.get("COND_VALUE")).append("'");
                }
            }
        }

        /*
            js?????? ???????????? expression ??????
         */
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");
        System.out.println("expression : " +expression.toString());
        Object result = null;
        try {
            if(parameters.get("processResultType") == ProcessResultTypeEnum.AS){
                result = new HashMap<>();
                String[] expressions = expression.toString().split("<-|&&");

                for(int i=0; i< expressions.length; i+=2){
                    //TODO: ?????? ?????? ?????? ?????? ??????
                    ((Map)result).put(StringUtils.trim(expressions[i]), scriptEngine.eval(StringUtils.trim(expressions[i+1])));
                }
            }else {
                result = scriptEngine.eval(expression.toString());
            }
            System.out.println("result : " + result);
            return result;
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        return result;
    }
}
