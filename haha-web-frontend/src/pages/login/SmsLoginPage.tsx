import React, { useEffect, useState } from 'react';
import styles from './Login.module.css';
import { Footer, Header } from '../../components';
import { useNavigate } from "react-router-dom";
import { isLogin, setToken } from '../../utils/auth';
import NotifyUtils from '../../utils/notify';
import useMemberStore from '../../stores/memberStore';
import { sendSmsCode } from '../../apis/captcha/captcha';
import { login } from '../../apis/auth/auth';
import { AUTH, ERROR_MESSAGES } from '../../constants/auth';
import { ExclamationCircleIcon } from '../../components/icons/SocialIcons';

interface FormData {
    clientKey: string;
    authType: string;
    phone: string;
    smsCode: string;
}

interface FormError {
    phone?: string;
    smsCode?: string;
}

export const SmsLoginPage: React.FC = () => {
    const [loading, setLoading] = useState(false);
    const [countdown, setCountdown] = useState(0);
    const navigate = useNavigate();
    const fetchMemberData = useMemberStore((state) => state.fetchMemberData);

    const [formData, setFormData] = useState<FormData>({
        clientKey: AUTH.CLIENT_KEY,
        authType: AUTH.AUTH_TYPES.SMS,
        phone: '',
        smsCode: '',
    });

    const [errors, setErrors] = useState<FormError>({});

    // 处理倒计时
    useEffect(() => {
        let timer: NodeJS.Timeout;
        if (countdown > 0) {
            timer = setTimeout(() => {
                setCountdown(countdown - 1);
            }, 1000);
        }
        return () => {
            if (timer) {
                clearTimeout(timer);
            }
        };
    }, [countdown]);

    const validatePhone = (phone: string): string => {
        if (!phone) return ERROR_MESSAGES.PHONE.EMPTY;
        if (phone.length !== AUTH.PHONE.LENGTH) return ERROR_MESSAGES.PHONE.LENGTH;
        if (!AUTH.PHONE.PATTERN.test(phone)) return ERROR_MESSAGES.PHONE.INVALID;
        return '';
    };

    const validateSmsCode = (code: string): string => {
        if (!code) return ERROR_MESSAGES.SMS.EMPTY;
        if (code.length < AUTH.SMS.MIN_LENGTH) return ERROR_MESSAGES.SMS.MIN_LENGTH;
        if (code.length > AUTH.SMS.MAX_LENGTH) return ERROR_MESSAGES.SMS.MAX_LENGTH;
        if (!/^\d{4,6}$/.test(code)) return ERROR_MESSAGES.SMS.INVALID;
        return '';
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        
        if (name === 'phone') {
            const newValue = value.replace(/\D/g, '').slice(0, AUTH.PHONE.LENGTH);
            setFormData(prev => ({ ...prev, [name]: newValue }));
            const error = newValue.length === AUTH.PHONE.LENGTH ? validatePhone(newValue) : 
                         newValue.length === 0 ? ERROR_MESSAGES.PHONE.EMPTY : 
                         ERROR_MESSAGES.PHONE.LENGTH;
            setErrors(prev => ({ ...prev, [name]: error }));
        } else if (name === 'smsCode') {
            const newValue = value.replace(/\D/g, '').slice(0, AUTH.SMS.MAX_LENGTH);
            setFormData(prev => ({ ...prev, [name]: newValue }));
            const error = newValue.length >= AUTH.SMS.MIN_LENGTH ? validateSmsCode(newValue) : 
                         newValue.length === 0 ? ERROR_MESSAGES.SMS.EMPTY : 
                         ERROR_MESSAGES.SMS.IN_PROGRESS;
            setErrors(prev => ({ ...prev, [name]: error }));
        }
    };

    // 验证表单是否有效
    const isFormValid = (): boolean => {
        const phoneError = validatePhone(formData.phone);
        const smsError = validateSmsCode(formData.smsCode);
        return !phoneError && !smsError;
    };

    // 发送验证码
    const handleSendCode = async () => {
        const phoneError = validatePhone(formData.phone);
        if (phoneError) {
            NotifyUtils.error(phoneError);
            return;
        }
        try {
            setLoading(true);
            const response = await sendSmsCode(formData.phone);
            if (response.success) {
                NotifyUtils.success('验证码发送成功');
                setCountdown(60); // 开始60秒倒计时
            } else {
                NotifyUtils.error(response.msg);
            }
        } catch (error) {
            console.error('发送验证码失败:', error);
            NotifyUtils.error('发送验证码失败');
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!formData.phone || !formData.smsCode) {
            NotifyUtils.error('请填写完整信息');
            return;
        }
        try {
            setLoading(true);
            const response = await login(formData);
            if (response.success) {
                setToken(response.data.token);
                // 通过token获取用户个人信息
                fetchMemberData();
                navigate('/');
            } else {
                NotifyUtils.error(response.msg);
            }
        } catch (error) {
            console.error('登录失败:', error);
            NotifyUtils.error('登录失败');
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (isLogin()) {
            navigate('/');
        }
    }, [navigate]);

    return (
        <>
            <Header />
            <div className={styles.loginContainer}>
                <div className={styles.loginBox}>
                    <h2 className={styles.loginTitle}>手机号登录</h2>
                    <form onSubmit={handleSubmit}>
                        <div className={styles.inputGroup}>
                            <div className={styles.labelWrapper}>
                                <label>手机号</label>
                                {errors.phone && (
                                    <span className={styles.errorTip}>
                                        <ExclamationCircleIcon /> {errors.phone}
                                    </span>
                                )}
                            </div>
                            <input
                                type="tel"
                                name="phone"
                                value={formData.phone}
                                onChange={handleInputChange}
                                placeholder={AUTH.PHONE.PLACEHOLDER}
                                className={errors.phone ? styles.inputError : ''}
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <div className={styles.labelWrapper}>
                                <label>验证码</label>
                                {errors.smsCode && (
                                    <span className={styles.errorTip}>
                                        <ExclamationCircleIcon /> {errors.smsCode}
                                    </span>
                                )}
                            </div>
                            <div className={styles.smsCodeContainer}>
                                <input
                                    type="text"
                                    name="smsCode"
                                    value={formData.smsCode}
                                    onChange={handleInputChange}
                                    placeholder={countdown > 0 ? AUTH.SMS.PLACEHOLDER.AFTER_SEND : AUTH.SMS.PLACEHOLDER.BEFORE_SEND}
                                    className={`${styles.smsCodeInput} ${errors.smsCode ? styles.inputError : ''} ${countdown === 0 ? styles.inputDisabled : ''}`}
                                    disabled={countdown === 0}
                                />
                                <button
                                    type="button"
                                    onClick={handleSendCode}
                                    disabled={countdown > 0 || loading || !!errors.phone}
                                    className={`${styles.sendCodeButton} ${countdown > 0 ? styles.counting : ''}`}
                                >
                                    {countdown > 0 ? `${countdown}s` : '发送验证码'}
                                </button>
                            </div>
                        </div>

                        <button
                            type="submit"
                            className={styles.loginButton}
                            disabled={loading || !isFormValid()}
                        >
                            {loading ? '登录中...' : '登录'}
                        </button>
                    </form>

                    <div className={styles.switchLogin}>
                        <a href="/login">使用账号密码登录</a>
                        <a href="/register">没有账号？立即创建</a>
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
};