package com.daradat.boot.cm.bizRuleProcess;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class bizRuleProcessor {
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

    public static void main(String[] args) {
        try {
            temp();
        } catch (ScriptException e) {
            e.printStackTrace();
        }

    }
}
