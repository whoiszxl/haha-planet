// 登录响应类型
export interface LoginResp {
    token: string
}

// 第三方登录授权类型
export interface CaptchaResponse {
    uuid: string,
    img: string,
    expireTime: string,
}