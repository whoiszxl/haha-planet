import React from 'react';
import styles from './Header.module.css';
import { Input, Badge, Avatar, Dropdown, Button, Space } from 'antd';
import {
  SearchOutlined,
  MessageOutlined,
  BellOutlined,
  UserOutlined,
} from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import useMemberStore from '../../stores/memberStore';
import type { MenuProps } from 'antd';
import { logout } from '../../apis/auth/auth';
import { clearToken } from '../../utils/auth';


// 临时的Logo图标，可以替换为SVG或图片
const LogoIcon = () => (
  <svg width="24" height="24" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M24 4C12.9543 4 4 12.9543 4 24C4 35.0457 12.9543 44 24 44C35.0457 44 44 35.0457 44 24C44 18.2737 41.6965 13.1628 38.1421 9.6079M24 4C29.7263 4 34.8372 6.30355 38.3921 9.85789M24 44C18.2737 44 13.1628 41.6965 9.6079 38.1421M9.85789 38.3921C6.30355 34.8372 4 29.7263 4 24" stroke="#52c41a" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/><path d="M24 31C27.866 31 31 27.866 31 24C31 20.134 27.866 17 24 17C20.134 17 17 20.134 17 24C17 27.866 20.134 31 24 31Z" stroke="#52c41a" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/></svg>
);

export const Header: React.FC = () => {
  const navigate = useNavigate();
  // 修改这里，直接获取整个 store 状态
  const memberStore = useMemberStore();
  // 然后从 state 中获取 member，如果存在 state 包装的话
  const member = memberStore.member || (memberStore as any).state?.member;

  console.log("fffffffffffffffffff", member);

  // 获取图片基础URL - 修改为使用 process.env
  const imageBaseUrl = process.env.REACT_APP_IMAGE_BASE_URL || '';

  // 处理头像URL
  const getAvatarUrl = (avatar?: string) => {
    if (!avatar) return undefined;
    // 如果已经是完整URL，直接返回
    if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
      return avatar;
    }
    // 否则拼接基础URL
    return `${imageBaseUrl}${avatar}`;
  };

  const userMenuItems: MenuProps['items'] = [
    { key: '1', label: '我的主页', onClick: () => navigate('/profile') },
    { key: '2', label: '账号设置', onClick: () => navigate('/settings') },
    { type: 'divider' },
    { key: '3', label: '退出登录', danger: true, onClick: () => {
      logout();
      clearToken();
    } },
  ];

  return (
    <header className={styles.header}>
        {/* Left Section */}
        <div className={styles.leftSection}>
          <Space align="center" size="middle">
            <div className={styles.logoContainer}>
              <LogoIcon />
              <span className={styles.logoText}>哈哈星球</span>
            </div>
            <div className={styles.tagBlue}>私域社群</div>
            <div className={styles.tagOrange}>笔记</div>
          </Space>
        </div>

        {/* Middle Section */}
        <div className={styles.middleSection}>
          <Input
            placeholder="搜索星球、文件、主题"
            prefix={<SearchOutlined className={styles.searchIcon} />}
            className={styles.searchInput}
          />
        </div>

        {/* Right Section */}
        <div className={styles.rightSection}>
          <Space align="center" size="middle">
            
            <div className={styles.todoButton}>待办</div>
            <Space size="middle" align="center" className={styles.actions}>
            <Badge count={1} dot>
              <MessageOutlined className={styles.actionIcon} />
            </Badge>
            <Badge count={1}>
              <BellOutlined className={styles.actionIcon} />
            </Badge>
            <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
              <Avatar
                src={getAvatarUrl(member?.avatar)}
                icon={<UserOutlined />}
                className={styles.avatar}
              />
            </Dropdown>
            </Space>
          </Space>
        </div>
    </header>
  );
};
