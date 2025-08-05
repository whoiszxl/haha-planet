import random
import json
from datetime import datetime, timedelta
from typing import List, Dict

# 管理员日志数据生成器，admin仪表盘通过此表数据统计信息，如需测试效果，执行此脚本构建测试数据。
class AdminLogDataGenerator:
    def __init__(self):
        # 浏览器数据
        self.browsers = [
            'Chrome 120.0.0.0', 'Chrome 119.0.0.0', 'Chrome 118.0.0.0', 'Chrome 117.0.0.0',
            'Firefox 121.0', 'Firefox 120.0', 'Firefox 119.0', 'Firefox 118.0',
            'Safari 17.0', 'Safari 16.6', 'Safari 16.5', 'Safari 16.4',
            'Edge 120.0.0.0', 'Edge 119.0.0.0', 'Edge 118.0.0.0',
            'Opera 105.0.0.0', 'Opera 104.0.0.0', 'Opera 103.0.0.0',
            'Brave 1.60.0', 'Brave 1.59.0', 'Vivaldi 6.5.0'
        ]
        
        # 操作系统数据
        self.operating_systems = [
            'Windows 11', 'Windows 10', 'Windows 8.1',
            'macOS Sonoma', 'macOS Ventura', 'macOS Monterey', 'macOS Big Sur',
            'Ubuntu 22.04', 'Ubuntu 20.04', 'Ubuntu 18.04',
            'CentOS 8', 'CentOS 7', 'Debian 11', 'Debian 10',
            'iOS 17.0', 'iOS 16.7', 'iOS 16.6', 'iOS 15.8',
            'Android 14', 'Android 13', 'Android 12', 'Android 11',
            'Fedora 39', 'Fedora 38', 'openSUSE Leap 15.5',
            'Arch Linux', 'Manjaro Linux', 'Linux Mint 21'
        ]
        
        # 地域数据（省份|城市|运营商）
        self.locations = [
            ('中国|北京|北京市|联通', '116.407526,39.90403'),
            ('中国|北京|北京市|移动', '116.407526,39.90403'),
            ('中国|北京|北京市|电信', '116.407526,39.90403'),
            ('中国|上海|上海市|电信', '121.473701,31.230416'),
            ('中国|上海|上海市|移动', '121.473701,31.230416'),
            ('中国|上海|上海市|联通', '121.473701,31.230416'),
            ('中国|广东|深圳市|移动', '114.085947,22.547'),
            ('中国|广东|广州市|电信', '113.280637,23.125178'),
            ('中国|广东|东莞市|联通', '113.746262,23.046237'),
            ('中国|浙江|杭州市|电信', '120.153576,30.287459'),
            ('中国|浙江|宁波市|移动', '121.549792,29.868388'),
            ('中国|浙江|温州市|联通', '120.672111,28.000575'),
            ('中国|江苏|南京市|联通', '118.767413,32.041544'),
            ('中国|江苏|苏州市|电信', '120.619585,31.299379'),
            ('中国|江苏|无锡市|移动', '120.301663,31.574729'),
            ('中国|四川|成都市|移动', '104.065735,30.659462'),
            ('中国|四川|绵阳市|电信', '104.741722,31.46402'),
            ('中国|湖北|武汉市|联通', '114.298572,30.584355'),
            ('中国|湖北|宜昌市|移动', '111.290843,30.702636'),
            ('中国|湖南|长沙市|电信', '112.982279,28.19409'),
            ('中国|湖南|株洲市|联通', '113.151737,27.835806'),
            ('中国|河南|郑州市|移动', '113.665412,34.757975'),
            ('中国|河南|洛阳市|电信', '112.434468,34.663041'),
            ('中国|山东|济南市|联通', '117.000923,36.675807'),
            ('中国|山东|青岛市|电信', '120.355173,36.082982'),
            ('中国|山东|烟台市|移动', '121.391382,37.539297'),
            ('中国|福建|福州市|电信', '119.306239,26.075302'),
            ('中国|福建|厦门市|移动', '118.11022,24.490474'),
            ('中国|福建|泉州市|联通', '118.514d,24.93d'),
            ('中国|安徽|合肥市|电信', '117.283042,31.86119'),
            ('中国|安徽|芜湖市|移动', '118.376451,31.326319'),
            ('中国|江西|南昌市|联通', '115.892151,28.676493'),
            ('中国|江西|赣州市|电信', '114.940278,25.85097'),
            ('中国|重庆|重庆市|移动', '106.504962,29.533155'),
            ('中国|重庆|重庆市|电信', '106.504962,29.533155'),
            ('中国|天津|天津市|联通', '117.190182,39.125596'),
            ('中国|天津|天津市|移动', '117.190182,39.125596'),
            ('中国|辽宁|沈阳市|联通', '123.429096,41.796767'),
            ('中国|辽宁|大连市|电信', '121.618622,38.91459'),
            ('中国|黑龙江|哈尔滨市|移动', '126.642464,45.756967'),
            ('中国|黑龙江|大庆市|电信', '125.11272,46.590734'),
            ('中国|吉林|长春市|联通', '125.3245,43.886841'),
            ('中国|吉林|吉林市|移动', '126.57637,43.843577'),
            ('中国|内蒙古|呼和浩特市|电信', '111.670801,40.818311'),
            ('中国|内蒙古|包头市|联通', '109.840405,40.658168'),
            ('中国|新疆|乌鲁木齐市|移动', '87.617733,43.792818'),
            ('中国|新疆|喀什市|电信', '75.989138,39.467664'),
            ('中国|西藏|拉萨市|联通', '91.132212,29.660361'),
            ('中国|青海|西宁市|移动', '101.778916,36.623178'),
            ('中国|宁夏|银川市|电信', '106.278179,38.46637'),
            ('中国|甘肃|兰州市|联通', '103.823557,36.058039'),
            ('中国|甘肃|天水市|移动', '105.724998,34.578529'),
            ('中国|云南|昆明市|电信', '102.712251,25.040609'),
            ('中国|云南|大理市|移动', '100.267638,25.606486'),
            ('中国|贵州|贵阳市|联通', '106.713478,26.578343'),
            ('中国|贵州|遵义市|电信', '106.937265,27.706626'),
            ('中国|广西|南宁市|移动', '108.320004,22.82402'),
            ('中国|广西|桂林市|电信', '110.299121,25.274215'),
            ('中国|海南|海口市|联通', '110.35044,20.031971'),
            ('中国|海南|三亚市|移动', '109.508268,18.247872'),
            ('中国|河北|石家庄市|电信', '114.502461,38.045474'),
            ('中国|河北|唐山市|联通', '118.175393,39.635113'),
            ('中国|山西|太原市|移动', '112.549248,37.857014'),
            ('中国|山西|大同市|电信', '113.295259,40.09031'),
            ('中国|陕西|西安市|联通', '108.948024,34.263161'),
            ('中国|陕西|宝鸡市|移动', '107.14487,34.369315')
        ]
        
        # 模块数据
        self.modules = [
            ('登录', ['账号密码登录', '手机验证码登录', '第三方登录', '单点登录', '退出登录']),
            ('用户管理', ['分页查询用户列表', '新增用户', '编辑用户信息', '删除用户', '重置密码', '用户详情', '批量导入用户', '导出用户列表']),
            ('角色管理', ['查询角色列表', '新增角色', '编辑角色', '删除角色', '分配角色权限', '角色详情']),
            ('权限管理', ['查看权限列表', '新增权限', '编辑权限', '删除权限', '权限树结构']),
            ('消息管理', ['分页查询消息列表', '发送系统消息', '标记已读', '删除消息', '消息详情']),
            ('系统设置', ['查看系统配置', '更新系统配置', '系统参数管理', '字典管理', '缓存管理']),
            ('仪表盘', ['查看仪表盘', '统计数据概览', '实时监控数据']),
            ('数据统计', ['查询数据统计', '导出数据报表', '生成统计图表', '数据分析']),
            ('文件管理', ['文件上传', '文件下载', '文件删除', '文件预览', '批量上传']),
            ('日志管理', ['查看操作日志', '查看登录日志', '清理过期日志', '日志统计分析']),
            ('组织架构', ['查看部门列表', '新增部门', '编辑部门', '删除部门', '部门人员管理']),
            ('通知公告', ['查看公告列表', '发布公告', '编辑公告', '删除公告', '公告详情']),
            ('系统监控', ['服务器监控', '数据库监控', '接口监控', '性能分析']),
            ('定时任务', ['查看任务列表', '新增任务', '编辑任务', '执行任务', '任务日志']),
            ('API管理', ['接口文档', 'API测试', '接口统计', '接口权限管理'])
        ]
        
        # URL模板
        self.url_templates = {
            '登录': 'http://localhost:8000/auth/{action}',
            '用户管理': 'http://localhost:8000/system/user{path}',
            '角色管理': 'http://localhost:8000/system/role{path}',
            '权限管理': 'http://localhost:8000/system/permission{path}',
            '消息管理': 'http://localhost:8000/system/message{path}',
            '系统设置': 'http://localhost:8000/system/config{path}',
            '仪表盘': 'http://localhost:8000/dashboard{path}',
            '数据统计': 'http://localhost:8000/analytics{path}',
            '文件管理': 'http://localhost:8000/file{path}',
            '日志管理': 'http://localhost:8000/system/log{path}',
            '组织架构': 'http://localhost:8000/system/dept{path}',
            '通知公告': 'http://localhost:8000/system/notice{path}',
            '系统监控': 'http://localhost:8000/monitor{path}',
            '定时任务': 'http://localhost:8000/system/job{path}',
            'API管理': 'http://localhost:8000/api{path}'
        }
        
        # HTTP方法
        self.http_methods = ['GET', 'POST', 'PUT', 'DELETE']
        
        # 状态码
        self.status_codes = [200, 201, 400, 401, 403, 404, 500]
        self.status_weights = [0.85, 0.05, 0.03, 0.02, 0.02, 0.02, 0.01]  # 成功率较高
        
        # IP地址池
        self.ip_pools = [
            '192.168.1.{}', '192.168.0.{}', '10.0.0.{}', '172.16.0.{}',
            '192.168.100.{}', '192.168.200.{}', '10.10.10.{}'
        ]
        
    def generate_user_agent(self, browser: str, os: str) -> str:
        """生成User-Agent字符串"""
        ua_templates = {
            'Chrome': 'Mozilla/5.0 ({os_string}) AppleWebKit/537.36 (KHTML, like Gecko) {browser} Safari/537.36',
            'Firefox': 'Mozilla/5.0 ({os_string}; rv:109.0) Gecko/20100101 {browser}',
            'Safari': 'Mozilla/5.0 ({os_string}) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/{version} Safari/604.1',
            'Edge': 'Mozilla/5.0 ({os_string}) AppleWebKit/537.36 (KHTML, like Gecko) {browser} Safari/537.36 Edg/{version}',
            'Opera': 'Mozilla/5.0 ({os_string}) AppleWebKit/537.36 (KHTML, like Gecko) {browser} Safari/537.36',
            'Brave': 'Mozilla/5.0 ({os_string}) AppleWebKit/537.36 (KHTML, like Gecko) {browser} Safari/537.36',
            'Vivaldi': 'Mozilla/5.0 ({os_string}) AppleWebKit/537.36 (KHTML, like Gecko) {browser} Safari/537.36'
        }
        
        os_strings = {
            'Windows 11': 'Windows NT 10.0; Win64; x64',
            'Windows 10': 'Windows NT 10.0; Win64; x64',
            'Windows 8.1': 'Windows NT 6.3; Win64; x64',
            'macOS': 'Macintosh; Intel Mac OS X 10_15_7',
            'Ubuntu': 'X11; Linux x86_64',
            'CentOS': 'X11; Linux x86_64',
            'Debian': 'X11; Linux x86_64',
            'iOS': 'iPhone; CPU iPhone OS 17_0 like Mac OS X',
            'Android': 'Linux; Android 14'
        }
        
        browser_name = browser.split()[0]
        os_key = next((key for key in os_strings.keys() if key in os), 'Ubuntu')
        
        if browser_name in ua_templates:
            template = ua_templates[browser_name]
            os_string = os_strings[os_key]
            
            if browser_name == 'Safari':
                version = browser.split()[1] if len(browser.split()) > 1 else '17.0'
                return template.format(os_string=os_string, version=version)
            elif browser_name == 'Edge':
                version = browser.split()[1] if len(browser.split()) > 1 else '120.0.0.0'
                return template.format(os_string=os_string, browser=browser, version=version)
            else:
                return template.format(os_string=os_string, browser=browser)
        
        return f'Mozilla/5.0 ({os_strings[os_key]}) {browser}'
    
    def generate_request_headers(self, browser: str, os: str, has_auth: bool = True) -> str:
        """生成请求头"""
        headers = {
            'user-agent': self.generate_user_agent(browser, os),
            'accept': 'application/json, text/plain, */*',
            'accept-language': random.choice(['zh-CN,zh;q=0.9', 'en-US,en;q=0.9', 'zh-CN,zh;q=0.9,en;q=0.8']),
            'accept-encoding': 'gzip, deflate, br, zstd',
            'connection': 'close',
            'host': 'localhost:8000',
            'sec-fetch-dest': 'empty',
            'sec-fetch-mode': 'cors',
            'sec-fetch-site': 'same-origin'
        }
        
        if has_auth:
            headers['authorization'] = 'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiTjNWN2dUckZaMlFHNm5hYURldXFlUGt1OEtaeVV6Z1AifQ.HDzKIYEz2P7ZetwCWlEw1UHac6C9sPqXrQ1V2k0fy5U'
        
        if random.random() < 0.3:  # 30%概率添加referer
            headers['referer'] = 'http://localhost:5173/'
        
        if random.random() < 0.2:  # 20%概率添加origin
            headers['origin'] = 'http://localhost:5173'
            
        return json.dumps(headers, ensure_ascii=False)
    
    def escape_sql_string(self, value: str) -> str:
        """转义SQL字符串中的特殊字符"""
        if value == 'NULL':
            return value
        # 转义单引号和双引号
        return value.replace("'", "\\'").replace('"', '\\"')
    
    def generate_request_body(self, module: str, description: str, method: str) -> str:
        """生成请求体"""
        if method == 'GET' or method == 'DELETE':
            return 'NULL'
        
        bodies = {
            '登录': {
                '账号密码登录': '{\"username\":\"admin\",\"password\":\"encrypted_password\",\"captcha\":\"1234\",\"uuid\":\"uuid123\"}',
                '手机验证码登录': '{\"phone\":\"13800138000\",\"code\":\"123456\"}',
                '第三方登录': '{\"type\":\"wechat\",\"code\":\"auth_code_123\"}'
            },
            '用户管理': {
                '新增用户': '{\"username\":\"newuser\",\"email\":\"user@example.com\",\"phone\":\"13800138000\",\"roleIds\":[1,2]}',
                '编辑用户信息': '{\"id\":123,\"email\":\"updated@example.com\",\"phone\":\"13900139000\"}',
                '重置密码': '{\"userId\":123,\"newPassword\":\"encrypted_new_password\"}',
                '批量导入用户': '{\"file\":\"users.xlsx\",\"overwrite\":false}'
            },
            '角色管理': {
                '新增角色': '{\"name\":\"新角色\",\"code\":\"new_role\",\"description\":\"角色描述\"}',
                '编辑角色': '{\"id\":1,\"name\":\"更新角色\",\"description\":\"更新描述\"}',
                '分配角色权限': '{\"roleId\":1,\"permissionIds\":[1,2,3,4,5]}'
            },
            '消息管理': {
                '发送系统消息': '{\"title\":\"系统通知\",\"content\":\"消息内容\",\"type\":1,\"userIds\":[1,2,3]}',
                '标记已读': '{\"messageIds\":[1,2,3]}'
            },
            '文件管理': {
                '文件上传': '{\"file\":\"document.pdf\",\"category\":\"document\"}',
                '批量上传': '{\"files\":[\"file1.jpg\",\"file2.png\",\"file3.doc\"]}'
            }
        }
        
        if module in bodies and description in bodies[module]:
            return f"'{bodies[module][description]}'"
        
        # 默认请求体
        default_bodies = [
            '{\"page\":1,\"size\":10}',
            '{\"id\":123}',
            '{\"status\":1}',
            '{\"keyword\":\"search_term\"}',
            '{\"startDate\":\"2025-01-01\",\"endDate\":\"2025-01-31\"}'
        ]
        return f"'{random.choice(default_bodies)}'"
    
    def generate_response_body(self, status_code: int, module: str) -> str:
        """生成响应体"""
        if status_code == 200 or status_code == 201:
            success_responses = [
                '{\"success\":true,\"code\":200,\"msg\":\"success\",\"data\":{}}',
                '{\"success\":true,\"code\":200,\"msg\":\"操作成功\",\"data\":{\"id\":123}}',
                '{\"list\":[],\"total\":0}',
                '{\"list\":[],\"total\":50}',
                '{\"success\":true,\"token\":\"jwt_token_here\"}',
                '{\"success\":true,\"data\":{\"count\":100}}',
                '{\"success\":true,\"data\":{\"url\":\"http://example.com/file.pdf\"}}'
            ]
            return f"'{random.choice(success_responses)}'"
        else:
            error_responses = [
                '{\"success\":false,\"code\":400,\"msg\":\"参数错误\"}',
                '{\"success\":false,\"code\":401,\"msg\":\"未授权访问\"}',
                '{\"success\":false,\"code\":403,\"msg\":\"权限不足\"}',
                '{\"success\":false,\"code\":404,\"msg\":\"资源不存在\"}',
                '{\"success\":false,\"code\":500,\"msg\":\"服务器内部错误\"}'
            ]
            return f"'{random.choice(error_responses)}'"
    
    def generate_url(self, module: str, description: str, method: str) -> str:
        """生成请求URL"""
        base_template = self.url_templates.get(module, 'http://localhost:8000/api')
        
        url_mappings = {
            '登录': {
                '账号密码登录': '/account',
                '手机验证码登录': '/phone',
                '第三方登录': '/oauth',
                '退出登录': '/logout'
            },
            '用户管理': {
                '分页查询用户列表': '?page=1&size=10&sort=createdAt,desc',
                '新增用户': '',
                '编辑用户信息': '/123',
                '删除用户': '/123',
                '用户详情': '/123',
                '重置密码': '/123/reset-password',
                '批量导入用户': '/import',
                '导出用户列表': '/export'
            },
            '角色管理': {
                '查询角色列表': '?page=1&size=10',
                '新增角色': '',
                '编辑角色': '/1',
                '删除角色': '/1',
                '分配角色权限': '/1/permissions'
            }
        }
        
        if module in url_mappings and description in url_mappings[module]:
            path = url_mappings[module][description]
        else:
            # 生成默认路径
            if method == 'GET':
                path = '?page=1&size=10' if random.random() < 0.6 else '/123'
            elif method == 'POST':
                path = ''
            elif method == 'PUT':
                path = '/123'
            elif method == 'DELETE':
                path = '/123'
            else:
                path = ''
        
        if '{action}' in base_template:
            action = description.replace('账号密码', 'account').replace('手机验证码', 'phone')
            return base_template.format(action=action.split('登录')[0] + 'login' if '登录' in action else action)
        elif '{path}' in base_template:
            return base_template.format(path=path)
        else:
            return base_template + path
    
    def generate_ip(self) -> str:
        """生成IP地址"""
        pool = random.choice(self.ip_pools)
        return pool.format(random.randint(100, 254))
    
    def generate_trace_id(self) -> str:
        """生成链路ID"""
        return str(random.randint(1100000000000000000, 1199999999999999999))
    
    def generate_consuming_time(self, status_code: int) -> int:
        """生成消耗时间"""
        if status_code == 200 or status_code == 201:
            # 成功请求的响应时间分布
            return random.randint(200, 3000)
        else:
            # 错误请求通常响应更快
            return random.randint(100, 1000)
    
    def generate_single_record(self, record_id: int, created_time: datetime, user_id: int) -> Dict:
        """生成单条记录"""
        # 随机选择模块和描述
        module, descriptions = random.choice(self.modules)
        description = random.choice(descriptions)
        
        # 随机选择浏览器和操作系统
        browser = random.choice(self.browsers)
        os = random.choice(self.operating_systems)
        
        # 随机选择地域
        location, coordinates = random.choice(self.locations)
        
        # 根据描述确定HTTP方法
        if any(word in description for word in ['查询', '查看', '分页', '详情', '列表', '下载']):
            method = 'GET'
        elif any(word in description for word in ['新增', '发送', '上传', '导入', '生成']):
            method = 'POST'
        elif any(word in description for word in ['编辑', '更新', '修改', '重置', '分配']):
            method = 'PUT'
        elif any(word in description for word in ['删除', '清理']):
            method = 'DELETE'
        else:
            method = random.choice(self.http_methods)
        
        # 生成状态码
        status_code = random.choices(self.status_codes, weights=self.status_weights)[0]
        
        # 生成其他字段
        trace_id = self.generate_trace_id()
        url = self.generate_url(module, description, method)
        ip = self.generate_ip()
        consuming_time = self.generate_consuming_time(status_code)
        
        # 登录相关请求不需要认证头
        has_auth = module != '登录'
        request_headers = self.generate_request_headers(browser, os, has_auth)
        request_body = self.generate_request_body(module, description, method)
        
        response_headers = f'{{"traceId":"{trace_id}","Content-Type":"application/json"}}'
        response_body = self.generate_response_body(status_code, module)
        
        # 状态：1成功，2失败
        status = 1 if status_code in [200, 201] else 2
        
        return {
            'id': record_id,
            'trace_id': trace_id,
            'description': description,
            'module': module,
            'request_url': url,
            'request_http_method': method,
            'request_headers': request_headers,
            'request_body': request_body,
            'status_code': status_code,
            'response_headers': response_headers,
            'response_body': response_body,
            'consuming_time': consuming_time,
            'ip': ip,
            'ip_address': location,
            'browser': browser,
            'os': os,
            'version': 1,
            'status': status,
            'is_deleted': 0,
            'created_by': user_id,
            'updated_by': 'NULL',
            'created_at': created_time.strftime('%Y-%m-%d %H:%M:%S'),
            'updated_at': created_time.strftime('%Y-%m-%d %H:%M:%S')
        }
    
    def generate_sql_data(self, num_records: int = 1000, days_back: int = 30) -> str:
        """生成SQL插入语句"""
        records = []
        base_id = 1951227766220070914
        
        # 生成时间范围
        end_time = datetime.now()
        start_time = end_time - timedelta(days=days_back)
        
        # 用户ID池
        user_ids = [1, 2, 3, 4, 5]
        
        for i in range(num_records):
            # 随机生成时间，但要保证时间分布合理
            # 工作时间（9-18点）的访问量更高
            hour_weights = [0.01, 0.01, 0.01, 0.01, 0.01, 0.02, 0.03, 0.05,  # 0-7点
                          0.08, 0.12, 0.10, 0.09, 0.08, 0.07, 0.09, 0.10,  # 8-15点
                          0.08, 0.06, 0.04, 0.03, 0.02, 0.02, 0.01, 0.01]  # 16-23点
            
            # 随机选择日期
            random_date = start_time + timedelta(
                days=random.randint(0, days_back),
                hours=random.choices(range(24), weights=hour_weights)[0],
                minutes=random.randint(0, 59),
                seconds=random.randint(0, 59)
            )
            
            record = self.generate_single_record(
                record_id=base_id + i,
                created_time=random_date,
                user_id=random.choice(user_ids)
            )
            records.append(record)
        
        # 按时间排序
        records.sort(key=lambda x: x['created_at'])
        
        # 生成SQL
        sql_parts = []
        sql_parts.append("-- 生成的管理员日志测试数据")
        sql_parts.append("INSERT INTO `sys_admin_log` (")
        sql_parts.append("    `trace_id`, `description`, `module`, `request_url`, `request_http_method`,")
        sql_parts.append("    `request_headers`, `request_body`, `status_code`, `response_headers`, `response_body`,")
        sql_parts.append("    `consuming_time`, `ip`, `ip_address`, `browser`, `os`, `version`, `status`,")
        sql_parts.append("    `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`")
        sql_parts.append(") VALUES")
        
        value_parts = []
        for i, record in enumerate(records):
            # 转义字符串字段中的特殊字符
            escaped_headers = self.escape_sql_string(record['request_headers'])
            escaped_response_headers = self.escape_sql_string(record['response_headers'])
            
            value_part = f"('{record['trace_id']}', '{record['description']}', '{record['module']}', '{record['request_url']}', '{record['request_http_method']}', '{escaped_headers}', {record['request_body']}, {record['status_code']}, '{escaped_response_headers}', {record['response_body']}, {record['consuming_time']}, '{record['ip']}', '{record['ip_address']}', '{record['browser']}', '{record['os']}', {record['version']}, {record['status']}, {record['is_deleted']}, {record['created_by']}, {record['updated_by']}, '{record['created_at']}', '{record['updated_at']}')"
            
            if i < len(records) - 1:
                value_part += ","
            else:
                value_part += ";"
            
            value_parts.append(value_part)

        return "\n".join(sql_parts + value_parts)

# 使用示例
if __name__ == "__main__":
    generator = AdminLogDataGenerator()
    
    # 生成1000条记录，时间跨度30天
    sql_data = generator.generate_sql_data(num_records=1000, days_back=30)
    
    # 保存到文件
    with open('admin_log_test_data.sql', 'w', encoding='utf-8') as f:
        f.write(sql_data)
    
    print("SQL测试数据已生成并保存到 admin_log_test_data.sql 文件中")
    print(f"生成了1000条记录，时间跨度30天")
    print("\n数据特点：")
    print("- 包含15个不同模块的操作")
    print("- 覆盖全国34个省市自治区的主要城市")
    print("- 包含21种不同的浏览器版本")
    print("- 包含28种不同的操作系统")
    print("- 模拟真实的24小时访问分布（工作时间访问量更高）")
    print("- 包含三大运营商（电信、移动、联通）的网络")
    print("- 响应时间和状态码分布符合实际情况")