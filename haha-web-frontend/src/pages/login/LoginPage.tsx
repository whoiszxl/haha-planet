import React, { useEffect, useState } from 'react';
import styles from './Login.module.css';
import { Footer, Header } from '../../components';
import { useNavigate } from "react-router-dom";
import { isLogin, setToken } from '../../utils/auth';
import { socialAuth, login } from '../../apis/auth/auth';
import NotifyUtils from '../../utils/notify';
import { getImageCaptcha } from '../../apis/captcha/captcha';
import { encrypt } from '../../utils/encrypt';
import useMemberStore from '../../stores/memberStore';
import { GithubIcon, GoogleIcon, WechatIcon, EmailIcon, MobileIcon, GiteeIcon } from '../../components/icons/SocialIcons';

interface FormData {
    clientKey: string;
    authType: string;
    username: string;
    password: string;
    captcha: string;
    uuid: string;
    bindKey: string;
}

export const LoginPage: React.FC = () => {
    const [loading, setLoading] = useState(false);

    const [captchaImg, setCaptchaImg] = useState('');  // 存储验证码图片

    const fetchMemberData = useMemberStore((state) => state.fetchMemberData);

    const [formData, setFormData] = useState<FormData>({
        clientKey: '666',
        authType: 'ACCOUNT',
        username: '',
        password: '',
        captcha: '',
        uuid: '',
        bindKey: '',
    });


    const getCaptcha = async () => {
        try {
            const data = await getImageCaptcha();
            if (data.code === 200) {  // 检查正确的成功状态码
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

    const navigate = useNavigate();

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
            const loginData = {
                ...formData,
                password: encryptedPassword
            };
            const response = await login(loginData);
            if (response.success) {
                setToken(response.data.token);
                fetchMemberData();
                navigate('/');
            } else {
                getCaptcha();
                NotifyUtils.error(response.msg);
            }
        } catch (error) {
            getCaptcha();
            console.error('登录失败:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSocialLogin = async (platform: 'github' | 'google' | 'wechat' | 'gitee') => {
        try {
            setLoading(true);
            const response = await socialAuth(platform);
            if (response.code === 200) {
                window.location.href = response.data.authorizeUrl;
            }
        } catch (error) {
            console.error(`${platform}登录失败:`, error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        if (isLogin()) {
            navigate('/');
        }
        getCaptcha();
    }, [navigate]);

    return (
        <>
            <Header />
            <div className={styles.loginContainer}>
                <div className={styles.loginBox}>
                    <h2 className={styles.loginTitle}>登录哈哈星球</h2>
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
                            {loading ? '登录中...' : '登录'}
                        </button>
                    </form>

                    <div className={styles.divider}>
                        <span className={styles.dividerText}>其他方式登录</span>
                    </div>

                    <div className={styles.socialLogin}>
                        <button onClick={() => navigate('/sms-login')} disabled={loading} className={styles.socialButton} title="手机号登录">
                            <MobileIcon className={styles.icon} />
                        </button>
                        <button onClick={() => navigate('/email-login')} disabled={loading} className={styles.socialButton} title="邮箱登录">
                            <EmailIcon className={styles.icon} />
                        </button>
                        <button onClick={() => handleSocialLogin('github')} disabled={loading} className={styles.socialButton} title="Github登录">
                            <GithubIcon className={styles.icon} />
                        </button>
                        <button onClick={() => handleSocialLogin('google')} disabled={loading} className={styles.socialButton} title="Google登录">
                            <GoogleIcon className={styles.icon} />
                        </button>
                        <button onClick={() => handleSocialLogin('wechat')} disabled={loading} className={styles.socialButton} title="微信登录">
                            <WechatIcon className={styles.icon} />
                        </button>
                        <button onClick={() => handleSocialLogin('gitee')} disabled={loading} className={styles.socialButton} title="Gitee登录">
                            <GiteeIcon className={styles.icon} />
                        </button>
                    </div>

                    <a href="/register">没有账号？立即创建</a>
                </div>
            </div>
            <Footer />
        </>
    );
};