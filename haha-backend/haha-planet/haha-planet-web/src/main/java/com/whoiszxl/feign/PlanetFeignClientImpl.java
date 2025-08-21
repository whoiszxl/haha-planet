package com.whoiszxl.feign;

import com.whoiszxl.common.utils.UserLoginHelper;
import com.whoiszxl.planet.mapper.PlanetMemberMapper;
import com.whoiszxl.planet.model.entity.PlanetMemberDO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlanetFeignClientImpl implements PlanetFeignClient {

    private final PlanetMemberMapper planetMemberMapper;

    @Override
    @GetMapping("/getMyPlanetIds")
    public Set<Long> getMyPlanetIds() {
        Long userId = UserLoginHelper.getUserId();
        List<PlanetMemberDO> list = planetMemberMapper.lambdaQuery()
                .eq(PlanetMemberDO::getUserId, userId)
                .eq(PlanetMemberDO::getStatus, 1) // 正常状态
                .eq(PlanetMemberDO::getIsDeleted, 0) // 未删除
                .list();
        return list.stream()
                .map(PlanetMemberDO::getPlanetId)
                .collect(Collectors.toSet());
    }
}
