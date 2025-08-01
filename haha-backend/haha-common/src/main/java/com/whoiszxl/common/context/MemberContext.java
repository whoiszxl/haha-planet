package com.whoiszxl.common.context;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户上下文
 */
@Data
@RequiredArgsConstructor
public class MemberContext implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    private String username;

    private String avatar;

    private String avatarFrame;

    /**
     * 客户端类型
     */
    private String clientType;

    /**
     * 客户端 key
     */
    private String clientKey;
}
