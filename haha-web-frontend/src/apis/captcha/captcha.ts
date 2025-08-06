import http from '../../utils/http'
import type * as T from './type'
const BASE_URL = '/member/api/captcha'


/** @desc 三方账号登录 */
export function getImageCaptcha() {
    return http.get<T.CaptchaResponse>(`${BASE_URL}/image`)
}


// 发送短信验证码
export const sendSmsCode = (phone: string) => {
    return http.get(`${BASE_URL}/sms`, { phone });
};

// 发送邮箱验证码
export const sendEmailcaptcha = (email: string) => {
    return http.get(`${BASE_URL}/mail`, { email });
};