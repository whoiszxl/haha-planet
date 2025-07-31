package com.whoiszxl.common.context;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import com.whoiszxl.starter.core.utils.ExceptionUtils;

public class MemberContextHolder {

    private static final ThreadLocal<MemberContext> CONTEXT_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<MemberExtraContext> EXTRA_CONTEXT_HOLDER = new ThreadLocal<>();

    private MemberContextHolder() {
    }


    /**
     * 设置上下文
     *
     * @param context 上下文
     */
    public static void setContext(MemberContext context) {
        setContext(context, true);
    }

    /**
     * 设置上下文
     *
     * @param context  上下文
     * @param isUpdate 是否更新
     */
    public static void setContext(MemberContext context, boolean isUpdate) {
        CONTEXT_HOLDER.set(context);
        if (isUpdate) {
            StpUtil.getSessionByLoginId(context.getId()).set(SaSession.USER, context);
        }
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public static MemberContext getContext() {
        MemberContext context = CONTEXT_HOLDER.get();
        if (null == context) {
            context = StpUtil.getSession().getModel(SaSession.USER, MemberContext.class);
            CONTEXT_HOLDER.set(context);
        }
        return context;
    }

    /**
     * 获取指定用户的上下文
     *
     * @param userId 用户 ID
     * @return 上下文
     */
    public static MemberContext getContext(Long userId) {
        SaSession session = StpUtil.getSessionByLoginId(userId, false);
        if (null == session) {
            return null;
        }
        return session.getModel(SaSession.USER, MemberContext.class);
    }

    /**
     * 设置额外上下文
     *
     * @param context 额外上下文
     */
    public static void setExtraContext(MemberExtraContext context) {
        EXTRA_CONTEXT_HOLDER.set(context);
    }

    /**
     * 获取额外上下文
     *
     * @return 额外上下文
     */
    public static MemberExtraContext getExtraContext() {
        MemberExtraContext context = EXTRA_CONTEXT_HOLDER.get();
        if (null == context) {
            context = getExtraContext(StpUtil.getTokenValue());
            EXTRA_CONTEXT_HOLDER.set(context);
        }
        return context;
    }

    /**
     * 获取额外上下文
     *
     * @param token 令牌
     * @return 额外上下文
     */
    public static MemberExtraContext getExtraContext(String token) {
        MemberExtraContext context = new MemberExtraContext();
        context.setIp(Convert.toStr(StpUtil.getExtra(token, "ip")));
        context.setAddress(Convert.toStr(StpUtil.getExtra(token, "address")));
        context.setBrowser(Convert.toStr(StpUtil.getExtra(token, "browser")));
        context.setOs(Convert.toStr(StpUtil.getExtra(token, "os")));
        context.setLoginTime(Convert.toLocalDateTime(StpUtil.getExtra(token, "loginTime")));
        return context;
    }

    /**
     * 清除上下文
     */
    public static void clearContext() {
        CONTEXT_HOLDER.remove();
        EXTRA_CONTEXT_HOLDER.remove();
    }

    /**
     * 获取用户 ID
     *
     * @return 用户 ID
     */
    public static Long getMemberId() {
        return ExceptionUtils.exToNull(() -> getContext().getId());
    }

    /**
     * 获取手机号
     *
     * @return 手机号
     */
    public static String getPhone() {
        return ExceptionUtils.exToNull(() -> getContext().getPhone());
    }


    /**
     * 获取邮箱
     *
     * @return 邮箱
     */
    public static String getEmail() {
        return ExceptionUtils.exToNull(() -> getContext().getEmail());
    }


}
