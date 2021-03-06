package com.hk.pms.core.servcie.impl;

import com.hk.commons.util.AssertUtils;
import com.hk.commons.util.ByteConstants;
import com.hk.core.cache.service.EnableCacheServiceImpl;
import com.hk.core.data.commons.BaseDao;
import com.hk.pms.core.domain.SysApp;
import com.hk.pms.core.repository.SysAppRepository;
import com.hk.pms.core.servcie.SysAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

/**
 * @author: kevin
 * @date 2018-04-12 11:32
 */
@Service
@CacheConfig(cacheNames = {"app_Cache"})
public class SysAppServiceImpl extends EnableCacheServiceImpl<SysApp, String> implements SysAppService {

    @Autowired
    private SysAppRepository sysAppRepository;

    @Override
    protected BaseDao<SysApp, String> getBaseDao() {
        return sysAppRepository;
    }

    /**
     * 根据appCode 查询唯一
     *
     * @param appCode appCode
     * @return
     */
    @Override
    public SysApp findByAppCode(String appCode) {
        AssertUtils.notBlank(appCode);
        return sysAppRepository.findByAppCode(appCode);
    }

    @Override
    public void enable(String id) {
        updateStatus(id, ByteConstants.ONE);
    }

    @Override
    public void disable(String id) {
        updateStatus(id, ByteConstants.ZERO);
    }

    private void updateStatus(String appId, Byte status) {
        SysApp app = findOne(appId);
        app.setAppStatus(status);
        getCurrentProxy().insertOrUpdate(app);
    }
}
