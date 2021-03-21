package com.daradat.boot.solutionization.logicTransition.service;

import com.daradat.boot.framework.dataaccess.CommonDao;
import com.daradat.boot.solutionization.logicTransition.model.BizModuleInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogicTransitionService {

    private final CommonDao commonDao;

    public BizModuleInfo retrieveBizRuleInfo(String tenantId, String bizRuleId, String altFlag){

        try {

            BizModuleInfo info = new BizModuleInfo();
            info.setTenantId(tenantId);
            info.setBizModuleId(bizRuleId);
            info.setAltFlag(altFlag);
            //Map temp = commonDao.select("bizModuleExecute.retrieveBizModule", info);
            info = commonDao.select("bizModuleExecute.retrieveBizModule", info);

            //solutionization.bizModuleExecute.retrieveBizModule

//            Connection conn = DataSourceConnection.getInstance().getConnection();
//
//            PreparedStatement stmt = conn.prepareStatement("SELECT TENANT_ID, BIZRULE_ID, ALT_FLG, CALL_TYPE, CALL_HOST, CALL_INFO FROM BIZRULE_INFO where TENANT_ID=? and BIZRULE_ID=? and ALT_FLG=?");
//            stmt.setString(1, info.getTenantId());
//            stmt.setString(2, info.getBizRuleId());
//            stmt.setString(3, info.getAltFlag());
//
//            ResultSet rs = stmt.executeQuery("select user_id, user_nm from internal_user");
//
//            while (rs.next()) {
//                info.setTenantId(rs.getString(1));
//                info.setBizRuleId(rs.getString(2));
//                info.setAltFlag(rs.getString(3));
//                info.setCallType(rs.getString(4));
//                info.setCallHost(rs.getString(5));
//                info.setCallInfo(rs.getString(6));
//            }
            return info;
        } catch (Exception e) {
            throw new RuntimeException("Error Occurred while retrieve BizModuleInfo from DB : " + tenantId + ", " + bizRuleId + ", " + altFlag, e);
        }

    }
}
