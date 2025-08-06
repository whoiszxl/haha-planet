import React, { useEffect, useState } from 'react';
import styles from './Login.module.css';
import { Footer, Header } from '../../components';
import { useNavigate } from "react-router-dom";
import { isLogin, setToken } from '../../utils/auth';
import NotifyUtils from '../../utils/notify';
import useMemberStore from '../../stores/memberStore';
import { sendEmailcaptcha } from '../../apis/captcha/captcha';
import { login } from '../../apis/auth/auth';
import { AUTH, ERROR_MESSAGES } from '../../constants/auth';

interface FormData {
    clientKey: string;
    authType: string;
    email: string;
    captcha: string;
}

interface FormError {
    email?: string;
    captcha?: string;
}

export const EmailLoginPage: React.FC = () => {
    const [loading, setLoading] = useState(false);
    const [countdown, setCountdown] = useState(0);
    const navigate = useNavigate();
    const fetchMemberData = useMemberStore((state) => state.fetchMemberData);

    const [formData, setFormData] = useState<FormData>({
        clientKey: AUTH.CLIENT_KEY,
        authType: AUTH.AUTH_TYPES.EMAIL,
        email: '',
        captcha: '',
    });

    const [errors, setErrors] = useState<FormError>({});

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

    const validateEmail = (email: string): string => {
        if (!email) return ERROR_MESSAGES.EMAIL.EMPTY;
        if (!AUTH.EMAIL.PATTERN.test(email)) return ERROR_MESSAGES.EMAIL.INVALID;
        return '';
    };

    const validateCaptcha = (code: string): string => {
        if (!code) return ERROR_MESSAGES.EMAIL.CODE.EMPTY;
        if (code.length < AUTH.EMAIL.CODE.MIN_LENGTH) return ERROR_MESSAGES.EMAIL.CODE.MIN_LENGTH;
        if (code.length > AUTH.EMAIL.CODE.MAX_LENGTH) return ERROR_MESSAGES.EMAIL.CODE.MAX_LENGTH;
        if (!/^\d{4,6}$/.test(code)) return ERROR_MESSAGES.EMAIL.CODE.INVALID;
        return '';
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        
        if (name === 'email') {
            setFormData(prev => ({ ...prev, [name]: value }));
            const error = value ? validateEmail(value) : ERROR_MESSAGES.EMAIL.EMPTY;
            setErrors(prev => ({ ...prev, [name]: error }));
        } else if (name === 'captcha') {
            const newValue = value.replace(/\D/g, '').slice(0, AUTH.EMAIL.CODE.MAX_LENGTH);
            setFormData(prev => ({ ...prev, [name]: newValue }));
            const error = newValue.length >= AUTH.EMAIL.CODE.MIN_LENGTH ? validateCaptcha(newValue) : 
                         newValue.length === 0 ? ERROR_MESSAGES.EMAIL.CODE.EMPTY : 
                         ERROR_MESSAGES.EMAIL.CODE.IN_PROGRESS;
            setErrors(prev => ({ ...prev, [name]: error }));
        }
    };

    const isFormValid = (): boolean => {
        const emailError = validateEmail(formData.email);
        const codeError = validateCaptcha(formData.captcha);
        return !emailError && !codeError;
    };

    const handleSendCode = async () => {
        const emailError = validateEmail(formData.email);
        if (emailError) {
            NotifyUtils.error(emailError);
            return;
        }
        try {
            setLoading(true);
            const response = await sendEmailcaptcha(formData.email);
            if (response.success) {
                NotifyUtils.success('验证码发送成功');
                setCountdown(60);
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
        if (!formData.email || !formData.captcha) {
            NotifyUtils.error('请填写完整信息');
            return;
        }
        try {
            setLoading(true);
            const response = await login(formData);
            if (response.success) {
                setToken(response.data.token);
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
                    <h2 className={styles.loginTitle}>邮箱登录</h2>
                    <form onSubmit={handleSubmit}>
                        <div className={styles.inputGroup}>
                            <div className={styles.labelWrapper}>
                                <label>邮箱地址</label>
                                {errors.email && (
                                    <span className={styles.errorTip}>
                                        {errors.email}
                                    </span>
                                )}
                            </div>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleInputChange}
                                placeholder={AUTH.EMAIL.PLACEHOLDER}
                                className={errors.email ? styles.inputError : ''}
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <div className={styles.labelWrapper}>
                                <label>验证码</label>
                                {errors.captcha && (
                                    <span className={styles.errorTip}>
                                        {errors.captcha}
                                    </span>
                                )}
                            </div>
                            <div className={styles.smsCodeContainer}>
                                <input
                                    type="text"
                                    name="captcha"
                                    value={formData.captcha}
                                    onChange={handleInputChange}
                                    placeholder={countdown > 0 ? AUTH.EMAIL.CODE.PLACEHOLDER.AFTER_SEND : AUTH.EMAIL.CODE.PLACEHOLDER.BEFORE_SEND}
                                    className={`${styles.smsCodeInput} ${errors.captcha ? styles.inputError : ''} ${countdown === 0 ? styles.inputDisabled : ''}`}
                                    disabled={countdown === 0}
                                />
                                <button
                                    type="button"
                                    onClick={handleSendCode}
                                    disabled={countdown > 0 || loading || !!errors.email}
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
                        <button 
                            className={styles.linkButton}
                            onClick={() => navigate('/login')}
                        >
                            使用账号密码登录
                        </button>
                        <button
                            className={styles.linkButton}
                            onClick={() => navigate('/register')}
                        >
                            没有账号？立即创建
                        </button>
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
}; 