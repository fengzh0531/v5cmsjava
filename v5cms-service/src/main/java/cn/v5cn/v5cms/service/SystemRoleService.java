package cn.v5cn.v5cms.service;

import cn.v5cn.v5cms.entity.SystemRole;

import java.util.List;

/**
 * Created by ZXF-PC1 on 2015/6/26.
 */
public interface SystemRoleService {
//    Page<SystemRole> findRoleByRoleNamePageable(SystemRole role,Integer currPage);
    Long save(SystemRole role,String resIds);

    //MyBatis
    List<SystemRole> findRolesByUserName(String userName);
}
