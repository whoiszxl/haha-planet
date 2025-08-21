package com.whoiszxl.feign;

import com.whoiszxl.planet.mapper.PlanetMemberMapper;
import com.whoiszxl.planet.model.entity.PlanetMemberDO;
import com.whoiszxl.starter.web.model.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 星球 feign 客户端实现
 * @author whoiszxl
 */
@RestController
@RequiredArgsConstructor
public class PlanetFeignClientImpl implements PlanetFeignClient {

    private final PlanetMemberMapper planetMemberMapper;

    @Override
    public R<Set<Long>> getMyPlanetIds(Long userId) {
        List<PlanetMemberDO> list = planetMemberMapper.lambdaQuery()
                .eq(PlanetMemberDO::getUserId, userId)
                .eq(PlanetMemberDO::getStatus, 1)
                .list();
        Set<Long> result = list.stream()
                .map(PlanetMemberDO::getPlanetId)
                .collect(Collectors.toSet());
        return R.ok(result);
    }
}
