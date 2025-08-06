import React from "react";
import { Footer, Header } from "../../components";
import styles from './RegisterPage.module.css';

export const RegisterPage: React.FC = () => {
  return (
    <>
      <Header/>
      <div className={styles.loginContainer}>
                <div className={styles.loginBox}>
                    <h2 className={styles.loginTitle}>创建您的账户</h2>
                    <form>
                        <div className={styles.inputGroup}>
                            <label>电子邮件地址</label>
                            <input type="text" placeholder="用户名" />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>确认您的电子邮件地址</label>
                            <input type="text" placeholder="用户名" />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>密码</label>
                            <input type="password" placeholder="密码" />
                        </div>
                        <div className={styles.checkboxGroup}>
                            <input type="checkbox" id="remember" />
                            <label htmlFor="remember">我已年满13周岁并同意Cteam用户协议</label>
                        </div>
                        <button type="submit" className={styles.loginButton}>创建</button>
                    </form>
                    <div className={styles.qrLogin}>
                        <p>微信登录</p>
                    </div>
                    <a href="/login">已有账号，立即登录。</a>
                </div>
            </div>
      <Footer/>
    </>
  );
};
