import React, { useEffect, useState } from 'react';
import styles from '../login/Login.module.css';
import { Footer, Header } from '../../components';
import { useNavigate, useSearchParams } from "react-router-dom";
import { isLogin, setToken } from '../../utils/auth';
import { login } from '../../apis/auth/auth';
import NotifyUtils from '../../utils/notify';
import { getImageCaptcha } from '../../apis/captcha/captcha';
import useMemberStore from '../../stores/memberStore';
import { encrypt } from '../../utils/encrypt';

interface FormData {
    clientKey: string;
    authType: string;
    username: string;
    password: string;
    captcha: string;
    uuid: string;
    bindKey: string;
}

export const BindPage: React.FC = () => {
    const [loading, setLoading] = useState(false);
    const [captchaImg, setCaptchaImg] = useState('');
    const [searchParams] = useSearchParams();
    const bindKey = searchParams.get('bindKey') || '';
    const source = searchParams.get('source') || '';
    const navigate = useNavigate();
    const fetchMemberData = useMemberStore((state) => state.fetchMemberData);

    const [formData, setFormData] = useState<FormData>({
        clientKey: 'web_client_001',
        authType: 'ACCOUNT',
        username: '',
        password: '',
        captcha: '',
        uuid: '',
        bindKey: bindKey, // 使用URL参数中的bindKey
    });

    const getCaptcha = async () => {
        try {
            const data = await getImageCaptcha();
            if (data.code === 200) {
                setCaptchaImg(data.data.img);
                setFormData(prev => ({
                    ...prev,
                    uuid: data.data.uuid
                }));
            }
        } catch (error) {
            console.error('获取验证码失败:', error);
        }
    };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            setLoading(true);
            const encryptedPassword = await encrypt(formData.password);
            const bindData = {
                ...formData,
                password: encryptedPassword
            };
            const response = await login(bindData);
            if (response.success) {
                setToken(response.data.token);
                fetchMemberData();
                NotifyUtils.success('绑定成功');
                navigate('/');
            } else {
                getCaptcha();
                NotifyUtils.error(response.msg);
            }
        } catch (error) {
            getCaptcha();
            console.error('绑定失败:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (!bindKey) {
            NotifyUtils.error('缺少必要的绑定参数');
            navigate('/login');
            return;
        }

        if (isLogin()) {
            navigate('/');
            return;
        }

        getCaptcha();
    }, [bindKey, navigate]);

    return (
        <>
            <Header />
            <div className={styles.loginContainer}>
                <div className={styles.loginBox}>
                    <h2 className={styles.loginTitle}>{source} - 绑定已有账号</h2>
                    <form onSubmit={handleSubmit}>
                        <div className={styles.inputGroup}>
                            <label>账户名称</label>
                            <input
                                type="text"
                                name="username"
                                value={formData.username}
                                onChange={handleInputChange}
                                placeholder="请输入用户名"
                                required
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>密码</label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleInputChange}
                                placeholder="请输入密码"
                                required
                            />
                        </div>

                        <div className={styles.inputGroup}>
                            <label>验证码</label>
                            <div className={styles.captchaContainer}>
                                <input
                                    type="text"
                                    name="captcha"
                                    value={formData.captcha}
                                    onChange={handleInputChange}
                                    placeholder="请输入验证码"
                                    required
                                    maxLength={4}
                                />
                                {captchaImg ? (
                                    <img
                                        src={captchaImg}
                                        alt="验证码"
                                        onClick={getCaptcha}
                                        title="点击刷新验证码"
                                        style={{ cursor: 'pointer' }}
                                    />
                                ) : (
                                    <div className={styles.captchaLoading}>
                                        加载中...
                                    </div>
                                )}
                            </div>
                        </div>

                        <button
                            type="submit"
                            className={styles.loginButton}
                            disabled={loading}
                        >
                            {loading ? '绑定中...' : '绑定'}
                        </button>
                    </form>

                    <div style={{ marginTop: '20px', textAlign: 'center' }}>
                        <a href="/register">没有账号？立即创建</a>
                    </div>
                </div>
            </div>
            <Footer />
        </>
    );
};