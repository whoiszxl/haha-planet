package com.whoiszxl.admin.controller.sys;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Tag(name = "系统日志 API")
@RestController
@RequestMapping("/system/admin-log")
@RequiredArgsConstructor
public class AdminLogController {

}

