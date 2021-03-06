package com.hk.pms.core.servcie;

import com.google.common.collect.Maps;
import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.StringUtils;
import com.hk.core.authentication.api.SecurityContextUtils;
import com.hk.core.service.BaseService;
import com.hk.pms.core.domain.SysPermission;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: kevin
 * @date 2018-04-12 16:53
 */
public interface SysPermissionService extends BaseService<SysPermission, String> {

    /**
     * 获取指定用户的所有权限
     *
     * @param userId userId
     * @return
     */
    List<SysPermission> getPermissionList(String userId, String appId);

    /**
     * 获取当前登陆用户的所有权限
     *
     * @return
     */
    default List<SysPermission> getCurrentUserPermissionList(String appId) {
        return getPermissionList(SecurityContextUtils.getPrincipal().getUserId(), appId);
    }

    /**
     * 用户是否有指定的权限
     *
     * @param userId         userId
     * @param appId          appId
     * @param permissionCode permissionCode
     * @return
     */
    default boolean hasPermission(String userId, String appId, String permissionCode) {
        return StringUtils.isNotBlank(permissionCode) && getPermissionListAsString(userId, appId).contains(permissionCode);
    }

    /**
     * 检查权限是否存在
     *
     * @param userId         用户id
     * @param appId          appId
     * @param permissionCode 权限编号
     */
    default void checkPermissioin(String userId, String appId, String permissionCode) {
        AssertUtils.isTrue(hasPermission(userId, appId, permissionCode), String.format("userId[%s],appId[%s], No Permission:[%s]", userId, appId, permissionCode));
    }

    /**
     * 返回当前登陆用户的所有系统权限编号列表
     *
     * @return
     */
    default Map<String, Collection<String>> getCurrentUserAllRoleListAsString() {
        List<SysPermission> permissionList = getPermissionList(SecurityContextUtils.getPrincipal().getUserId(), null);
        Map<String, List<SysPermission>> byAppIdMap = permissionList.stream().collect(Collectors.groupingBy((permission) -> permission.getApp().getId()));
        Map<String, Collection<String>> result = Maps.newHashMap();
        byAppIdMap.forEach((key, value) -> result.put(key, value.stream().map(SysPermission::getPermissionCode).collect(Collectors.toList())));
        return result;
    }

    /**
     * 获取当前登陆用户的所有权限字符串
     *
     * @return
     */
    default List<String> getCurrentUserPermissionListAsString(String appId) {
        return getPermissionListAsString(SecurityContextUtils.getPrincipal().getUserId(), appId);
    }

    /**
     * 获取指定用户的所有权限字符串
     *
     * @param userId userId
     * @param appId  appId
     * @return 权限列表
     */
    default List<String> getPermissionListAsString(String userId, String appId) {
        return getPermissionList(userId, appId)
                .stream()
                .map(SysPermission::getPermissionCode)
                .collect(Collectors.toList());
    }


}
