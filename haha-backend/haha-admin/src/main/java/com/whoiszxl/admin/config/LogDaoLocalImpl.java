package com.whoiszxl.admin.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.whoiszxl.admin.cqrs.command.PasswordLoginCommand;
import com.whoiszxl.admin.entity.AdminLog;
import com.whoiszxl.admin.enums.LogStatusEnum;
import com.whoiszxl.admin.mapper.AdminLogMapper;
import com.whoiszxl.admin.service.IAdminService;
import com.whoiszxl.common.constants.SysConstants;
import com.whoiszxl.starter.core.constants.StringConstants;
import com.whoiszxl.starter.core.utils.ExceptionUtils;
import com.whoiszxl.starter.core.utils.MyStrUtil;
import com.whoiszxl.starter.log.core.dao.LogDao;
import com.whoiszxl.starter.log.core.model.LogRecord;
import com.whoiszxl.starter.log.core.model.LogRequest;
import com.whoiszxl.starter.log.core.model.LogResponse;
import com.whoiszxl.starter.web.config.trace.TraceProperties;
import com.whoiszxl.starter.web.model.R;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Set;

/**
 * @author whoiszxl
 */
@RequiredArgsConstructor
public class LogDaoLocalImpl implements LogDao {

    private final IAdminService adminService;

    private final AdminLogMapper logMapper;

    private final TraceProperties traceProperties;


    @Override
    public void add(LogRecord logRecord) {
        AdminLog log = new AdminLog();
        log.setDescription(logRecord.getDescription());
        String module = logRecord.getModule();
        log.setModule(MyStrUtil.blankToDefault(module, null, m -> m.replace("API", StringConstants.EMPTY).trim()));
        log.setCreatedAt(LocalDateTime.ofInstant(logRecord.getTimestamp(), ZoneId.systemDefault()));
        log.setConsumingTime(logRecord.getTimeTaken().toMillis());
        // 请求信息
        LogRequest logRequest = logRecord.getRequest();
        log.setRequestHttpMethod(logRequest.getMethod());
        URI requestUrl = logRequest.getUrl();
        String requestUri = requestUrl.getPath();
        log.setRequestUrl(requestUrl.toString());
        Map<String, String> requestHeaderMap = logRequest.getHeaders();
        log.setRequestHeaders(JSONUtil.toJsonStr(requestHeaderMap));
        String requestBody = logRequest.getBody();
        log.setRequestBody(requestBody);
        log.setIp(logRequest.getIp());
        log.setIpAddress(logRequest.getAddress());
        log.setBrowser(logRequest.getBrowser());
        log.setOs(MyStrUtil.subBefore(logRequest.getOs(), " or", false));
        // 响应信息
        LogResponse logResponse = logRecord.getResponse();
        Integer statusCode = logResponse.getStatus();
        log.setStatusCode(statusCode);
        Map<String, String> responseHeaders = logResponse.getHeaders();
        log.setResponseHeaders(JSONUtil.toJsonStr(responseHeaders));
        log.setTraceId(responseHeaders.get(traceProperties.getHeaderName()));
        String responseBody = logResponse.getBody();
        log.setResponseBody(responseBody);
        // 状态
        log.setStatus(statusCode >= HttpStatus.HTTP_BAD_REQUEST ? LogStatusEnum.FAILURE : LogStatusEnum.SUCCESS);
        if (MyStrUtil.isNotBlank(responseBody) && JSONUtil.isTypeJSON(responseBody)) {
            R result = JSONUtil.toBean(responseBody, R.class);
            if (!result.isSuccess()) {
                log.setStatus(LogStatusEnum.FAILURE);
                // logDO.setErrorMsg(result.getMsg());
            }
            // 操作人
            if (requestUri.startsWith(SysConstants.LOGOUT_URI)) {
                Long loginId = Convert.toLong(result.getData(), -1L);
                log.setCreatedBy(-1 != loginId ? loginId : null);
            } else if (result.isSuccess() && requestUri.startsWith(SysConstants.LOGIN_URI)) {
                PasswordLoginCommand loginReq = JSONUtil.toBean(requestBody, PasswordLoginCommand.class);
                log.setCreatedBy(ExceptionUtils.exToNull(() -> adminService.getByUsername(loginReq.getUsername())
                        .getId()));
            }
        }
        // 操作人
        String headerName = HttpHeaders.AUTHORIZATION;
        boolean isContains = CollUtil.containsAny(requestHeaderMap.keySet(), Set.of(headerName, headerName
                .toLowerCase()));
        if (!requestUri.startsWith(SysConstants.LOGOUT_URI) && MapUtil.isNotEmpty(requestHeaderMap) && isContains) {
            String authorization = requestHeaderMap.getOrDefault(headerName, requestHeaderMap.get(headerName
                    .toLowerCase()));
            String token = authorization.replace(SaManager.getConfig()
                                                         .getTokenPrefix() + StringConstants.S_SPACE, StringConstants.EMPTY);
            log.setCreatedBy(Convert.toLong(StpUtil.getLoginIdByToken(token)));
        }
        logMapper.insert(log);
    }
}
