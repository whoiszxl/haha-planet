import React, { useState, useEffect, useRef, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { isLogin, setToken } from '../../utils/auth';
import { bindSocialAccount, login } from '../../apis/auth/auth';
import useMemberStore from '../../stores/memberStore';

export const OAuthRedirectPage: React.FC = () => {
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('登录中...');
  const navigate = useNavigate();
  const source = new URLSearchParams(window.location.search).get('source') as string;

  const fetchMemberData = useMemberStore((state) => state.fetchMemberData);

  const hasCalledLogin = useRef(false);  // 用来防止重复调用

  // 判断是否已登录
  const handleLogin = useCallback(() => {
    if (hasCalledLogin.current) return;  // 如果已经调用过 login 就不再调用
    hasCalledLogin.current = true;  // 标记已经调用过

    setMessage('登录中...');
    const { redirect, ...othersQuery } = Object.fromEntries(new URLSearchParams(window.location.search));
    console.log("othersQuery", othersQuery);
    console.log("source", source);
    login({ source, ...othersQuery, authType: "SOCIAL", clientKey: "666" })
      .then((result) => {
        console.log("获取到社交登录用户信息: ", result);
        const response = result.data;
        if (response) {
          
          if(response.type === 'bind') {
            // 如果是绑定类型，则需要跳转到 BindPage，并带上 bindKey 参数
            navigate({
              pathname: '/social/bind',
              search: new URLSearchParams({
                bindKey: response.token,
                source: source,
                ...othersQuery
              }).toString()
            });

            return;
          }


          console.log("登录成功了", response.token);
          // 设置token到本地
          setToken(response.token);

          // 通过token获取用户个人信息
          fetchMemberData();
        }

        navigate({
          pathname: redirect || '/',
          search: new URLSearchParams(othersQuery).toString(),
        });
      })
      .catch((e) => {
        console.log("捕获到异常：" + e);
        navigate('/login');
      })
      .finally(() => {
        setLoading(false);
      });
  }, [source, navigate, fetchMemberData]);

  // 绑定社交账号
  const handleBind = useCallback(() => {
    setMessage('绑定中...');
    const othersQuery = Object.fromEntries(new URLSearchParams(window.location.search));

    bindSocialAccount(source, othersQuery)
      .then(() => {

        // 通过token获取用户个人信息
        fetchMemberData();

        navigate({
          pathname: '/',
          search: new URLSearchParams(othersQuery).toString(),
        });
      })
      .catch(() => {
        navigate({
          pathname: '/',
          search: new URLSearchParams(othersQuery).toString(),
        });
      })
      .finally(() => {
        setLoading(false);
      });
  }, [source, navigate, fetchMemberData]);

  // 页面加载时判断用户登录状态
  useEffect(() => {
    if (isLogin()) {
      handleBind();
    } else {
      handleLogin();
    }
  }, [handleBind, handleLogin]); // 添加依赖项

  return (
    <div style={styles.container}>
      {loading ? (
        <div style={styles.spinnerContainer}>
          <div style={styles.spinner}></div>
          <p>{message}</p>
        </div>
      ) : (
        <div style={styles.content}>
          <h2 style={styles.heading}>处理完成</h2>
          <p style={styles.subheading}>您已成功登录或绑定，正在跳转...</p>
        </div>
      )}
    </div>
  );
};

const styles = {
  container: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    height: '100vh',
    backgroundColor: '#f5f5f5',
    flexDirection: 'column' as 'column',
    textAlign: 'center',
  } as React.CSSProperties,
  spinnerContainer: {
    display: 'flex',
    flexDirection: 'column' as 'column',
    justifyContent: 'center',
    alignItems: 'center',
  } as React.CSSProperties,
  spinner: {
    border: '8px solid #f3f3f3', /* Light gray */
    borderTop: '8px solid #3498db', /* Blue */
    borderRadius: '50%',
    width: '50px',
    height: '50px',
    animation: 'spin 2s linear infinite',
  } as React.CSSProperties,
  content: {
    marginTop: '20px',
    padding: '20px',
    backgroundColor: '#fff',
    borderRadius: '8px',
    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
    width: '100%',
    maxWidth: '400px',
  } as React.CSSProperties,
  heading: {
    fontSize: '24px',
    fontWeight: 'bold',
    color: '#333',
  } as React.CSSProperties,
  subheading: {
    fontSize: '16px',
    color: '#666',
    marginTop: '10px',
  } as React.CSSProperties,
  buttonWrapper: {
    marginTop: '20px',
  } as React.CSSProperties,
  button: {
    width: '100%',
    backgroundColor: '#1890ff',
    borderColor: '#1890ff',
    padding: '10px',
    color: 'white',
    fontSize: '16px',
    borderRadius: '5px',
    cursor: 'pointer',
  } as React.CSSProperties,
};
