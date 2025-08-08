package com.whoiszxl.service.impl;

import cn.hutool.json.JSONUtil;
import com.whoiszxl.enums.SocialSourceEnum;
import com.whoiszxl.service.UserTokenService;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.user.mapper.UserTokenMapper;
import com.whoiszxl.user.model.entity.UserTokenDO;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户令牌业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

    private final UserTokenMapper userTokenMapper;

    @Override
    public void add(UserTokenDO memberToken) {
        userTokenMapper.insert(memberToken);
    }

    @Override
    public UserTokenDO getBySourceAndToken(String source, String uuid) {
        return userTokenMapper.lambdaQuery()
                .eq(UserTokenDO::getSource, source)
                .eq(UserTokenDO::getToken, uuid)
                .one();
    }

    @Override
    public void updateById(UserTokenDO userTokenDO) {
        userTokenMapper.updateById(userTokenDO);
    }

    @Override
    public void bind(AuthUser authUser, Long userId) {
        String source = authUser.getSource();
        String openId = authUser.getUuid();

        List<UserTokenDO> tokenList = userTokenMapper.lambdaQuery()
                .eq(UserTokenDO::getUserId, userId).list();

        Set<String> tokenSourceSet = tokenList.stream()
                .map(UserTokenDO::getSource)
                .collect(Collectors.toSet());

        String description = SocialSourceEnum.valueOf(source).getDescription();
        CheckUtils.throwIf(tokenSourceSet.contains(source),
                "您已经绑定过了 [{}] 平台，请先解绑", description);


        UserTokenDO userTokenDO = userTokenMapper.lambdaQuery()
                .eq(UserTokenDO::getSource, source)
                .eq(UserTokenDO::getToken, openId)
                .one();
        CheckUtils.throwIfNotNull(userTokenDO, "[{}] 平台账号 [{}] 已被其他用户绑定", description, authUser.getUsername());

        userTokenDO = new UserTokenDO();
        userTokenDO.setUserId(userId);
        userTokenDO.setSource(source);
        userTokenDO.setToken(openId);
        userTokenDO.setMetaJson(JSONUtil.toJsonStr(authUser));
        userTokenDO.setLoginTime(LocalDateTime.now());
        userTokenMapper.insert(userTokenDO);
    }
}