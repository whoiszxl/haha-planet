package com.whoiszxl.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whoiszxl.model.req.PlanetListReq;
import com.whoiszxl.model.resp.PlanetResp;

/**
 * 星球服务接口
 * @author whoiszxl
 */
public interface PlanetService {

    /**
     * 通过分类ID查询星球列表（分页）
     * 使用多级缓存：本地缓存10分钟，远程缓存30分钟
     *
     * @param req 查询请求参数
     * @return 星球分页列表
     */
    Page<PlanetResp> listPlanetsByCategory(PlanetListReq req);

}
