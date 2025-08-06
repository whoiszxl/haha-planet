import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';
import { getToken } from './auth';

/** 接口返回数据格式 */
export interface ApiRes<T> {
  code: number;
  data: T;
  msg: string;
  success: boolean;
  timestamp: string;
}

/** HTTP状态码错误消息映射 */
const StatusCodeMessage: Record<number, string> = {
  400: '请求参数错误',
  401: '未授权，请重新登录',
  403: '拒绝访问',
  404: '请求的资源不存在',
  405: '请求方法不被允许',
  408: '请求超时',
  500: '服务器内部错误',
  502: '网关错误',
  503: '服务不可用',
  504: '网关超时'
};

/** HTTP配置常量 */
const HTTP_CONFIG = {
  BASE_URL: process.env.REACT_APP_API_BASE_URL || 'http://localhost/',
  TIMEOUT: 60 * 1000,
  STREAM_TIMEOUT: 60 * 60 * 1000, // 1小时用于流式请求
  WITH_CREDENTIALS: false
} as const;

/** 创建axios实例 */
const http: AxiosInstance = axios.create({
  baseURL: HTTP_CONFIG.BASE_URL,
  timeout: HTTP_CONFIG.TIMEOUT,
  withCredentials: HTTP_CONFIG.WITH_CREDENTIALS
});

/** 改进的错误处理函数 */
const handleError = (msg: string, showNotification = true) => {
  console.error('HTTP Error:', msg);
  
  if (showNotification) {
    // 这里可以替换为更好的通知组件，如 antd 的 notification
    if (typeof window !== 'undefined') {
      alert(msg || '服务器端错误');
    }
  }
};

/** 请求拦截器 */
http.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // 添加请求时间戳用于调试
    if (process.env.NODE_ENV === 'development') {
      console.log(`[HTTP Request] ${config.method?.toUpperCase()} ${config.url}`, config);
    }
    
    return config;
  },
  (error) => {
    console.error('Request interceptor error:', error);
    return Promise.reject(error);
  }
);

/** 响应拦截器 */
http.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response;
    
    // 处理blob类型响应（文件下载等）
    if (response.request.responseType === 'blob') {
      return response;
    }
    
    // 处理成功响应
    if (data.success) {
      return response;
    }
    
    // 处理业务错误
    const { code, msg } = data;
    
    // 401未授权处理
    if (code === 401 && response.config.url !== '/auth/member/info') {
      handleError('登录已过期，请重新登录');
      // 这里可以添加跳转到登录页的逻辑
      // window.location.href = '/login';
    } else {
      handleError(msg || '请求失败');
    }
    
    return Promise.reject(new Error(msg || '服务器端错误'));
  },
  (error: AxiosError) => {
    // 网络错误处理
    if (!error.response) {
      handleError('网络连接失败，请检查您的网络');
      return Promise.reject(error);
    }
    
    const status = error.response.status;
    const errorMsg = StatusCodeMessage[status] || '服务器暂时未响应，请刷新页面并重试';
    
    handleError(errorMsg);
    return Promise.reject(error);
  }
);

/** 通用请求方法 */
const request = async <T = unknown>(config: AxiosRequestConfig): Promise<ApiRes<T>> => {
  try {
    const response = await http.request<ApiRes<T>>(config);
    return response.data;
  } catch (error: any) {
    throw error;
  }
};

/** 原生响应请求方法 */
const requestNative = async <T = unknown>(config: AxiosRequestConfig): Promise<AxiosResponse<T>> => {
  try {
    const response = await http.request<T>(config);
    return response;
  } catch (error: any) {
    throw error;
  }
};

/** 创建特定HTTP方法的请求函数 */
const createRequest = (method: string) => {
  return <T = any>(
    url: string, 
    params?: object, 
    config?: AxiosRequestConfig
  ): Promise<ApiRes<T>> => {
    const requestConfig: AxiosRequestConfig = {
      method,
      url,
      ...config
    };
    
    if (method.toLowerCase() === 'get') {
      requestConfig.params = params;
      requestConfig.paramsSerializer = (obj) => 
        new URLSearchParams(obj as Record<string, string>).toString();
    } else {
      requestConfig.data = params;
    }
    
    return request<T>(requestConfig);
  };
};

/** 文件下载方法 */
const download = (
  url: string, 
  params?: object, 
  config?: AxiosRequestConfig
): Promise<AxiosResponse> => {
  return requestNative({
    method: 'get',
    url,
    responseType: 'blob',
    params,
    paramsSerializer: (obj) => 
      new URLSearchParams(obj as Record<string, string>).toString(),
    ...config
  });
};

/** 流式请求接口定义 */
interface StreamRequestOptions<T> {
  url: string;
  data: any;
  onData: (content: T) => void;
  onError: (error: any) => void;
  onComplete: (content: T) => void;
  timeout?: number;
}

interface StreamRequestResult {
  close: () => void;
}

/** HTTP工具方法对象 */
const httpUtils = {
  get: createRequest('get'),
  post: createRequest('post'),
  put: createRequest('put'),
  patch: createRequest('patch'),
  del: createRequest('delete'),
  request,
  requestNative,
  download,
};

/** 导出HTTP工具方法 */
export default httpUtils;

/** 导出类型和常量 */
export { HTTP_CONFIG, StatusCodeMessage };
export type { StreamRequestOptions, StreamRequestResult };
