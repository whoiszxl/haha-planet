import React from 'react';
import styles from './Header.module.css';
import { Input, Badge, Avatar, Dropdown, Button, Space, Tag } from 'antd';
import {
  SearchOutlined,
  MessageOutlined,
  BellOutlined,
  UserOutlined,
} from '@ant-design/icons';
import { Link, useNavigate } from 'react-router-dom';
import useMemberStore from '../../stores/memberStore';
import type { MenuProps } from 'antd';

// 临时的Logo图标，可以替换为SVG或图片
const LogoIcon = () => (
  <svg width="24" height="24" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M24 4C12.9543 4 4 12.9543 4 24C4 35.0457 12.9543 44 24 44C35.0457 44 44 35.0457 44 24C44 18.2737 41.6965 13.1628 38.1421 9.6079M24 4C29.7263 4 34.8372 6.30355 38.3921 9.85789M24 44C18.2737 44 13.1628 41.6965 9.6079 38.1421M9.85789 38.3921C6.30355 34.8372 4 29.7263 4 24" stroke="#52c41a" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/><path d="M24 31C27.866 31 31 27.866 31 24C31 20.134 27.866 17 24 17C20.134 17 17 20.134 17 24C17 27.866 20.134 31 24 31Z" stroke="#52c41a" stroke-width="4" stroke-linecap="round" stroke-linejoin="round"/></svg>
);

export const Header: React.FC = () => {
  const navigate = useNavigate();
  const { member } = useMemberStore();

  const userMenuItems: MenuProps['items'] = [
    { key: '1', label: '我的主页', onClick: () => navigate('/profile') },
    { key: '2', label: '账号设置', onClick: () => navigate('/settings') },
    { type: 'divider' },
    { key: '3', label: '退出登录', danger: true, onClick: () => console.log('logout') },
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
            <Tag className={styles.tagBlue}>社群工具</Tag>
            <Tag className={styles.tagOrange}>笔记</Tag>
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
            
            <Button className={styles.todoButton}>待办</Button>
            <Space size="middle" align="center" className={styles.actions}>
            <Badge count={1} dot>
              <MessageOutlined className={styles.actionIcon} />
            </Badge>
            <Badge count={1}>
              <BellOutlined className={styles.actionIcon} />
            </Badge>
            <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
              <Avatar
                src={member?.avatar}
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
