import React, { useState } from "react";

export const Footer: React.FC = () => {
  const [hoveredButton, setHoveredButton] = useState<string | null>(null);

  const linkStyle = (isHovered: boolean): React.CSSProperties => ({
    color: isHovered ? '#2a8cff' : '#595959',
    margin: '0 8px',
    textDecoration: 'none',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    fontSize: 'inherit',
    transition: 'color 0.3s ease',
  });

  const policyLinkStyle = (isHovered: boolean): React.CSSProperties => ({
    color: isHovered ? '#2a8cff' : '#8c8c8c',
    margin: '0 4px',
    textDecoration: 'none',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    fontSize: 'inherit',
    transition: 'color 0.3s ease',
  });

  return (
    <footer
      style={{
        backgroundColor: '#ffffff',
        color: '#595959',
        padding: '24px 50px',
        fontSize: '13px',
        textAlign: 'center',
        borderTop: '1px solid #f0f0f0',
        fontFamily: 'system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji"'
      }}
    >
      {/* 顶部内容 */}
      <div style={{ marginBottom: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <div style={{ 
            fontSize: '22px', 
            fontWeight: '600', 
            display: 'flex',
            alignItems: 'center',
            gap: '8px'
          }}>
            <span style={{
              color: '#0060dd',
            }}>
              HAHA PLANET
            </span>
          </div>
        </div>
      </div>

      {/* 中部版权文字 */}
      <div style={{ marginBottom: '16px', textAlign: 'center' }}>
        <p style={{ margin: '0' }}>
          © 2024 哈哈星球 版权所有。让学习变得更有趣，让知识在星球间传播。
        </p>
      </div>

      {/* 中部链接 */}
      <div style={{ marginBottom: '16px', textAlign: 'center', display: 'flex', justifyContent: 'center', flexWrap: 'wrap', gap: '8px' }}>
        {[
          '关于我们', '课程中心', '学习社区', '帮助中心', '技术博客', '加入我们', '联系我们'
        ].map(text => (
          <button
            key={text}
            onMouseEnter={() => setHoveredButton(text)}
            onMouseLeave={() => setHoveredButton(null)}
            style={linkStyle(hoveredButton === text)}
          >
            {text}
          </button>
        ))}
      </div>

      {/* 底部链接 */}
      <div style={{ marginBottom: '20px', textAlign: 'center', display: 'flex', justifyContent: 'center', alignItems: 'center', flexWrap: 'wrap', gap: '4px' }}>
        {[
          '隐私政策', '服务条款', '用户协议', '退款政策', 'Cookie政策'
        ].map((text, index, arr) => (
          <React.Fragment key={text}>
            <button
              onMouseEnter={() => setHoveredButton(text)}
              onMouseLeave={() => setHoveredButton(null)}
              style={policyLinkStyle(hoveredButton === text)}
            >
              {text}
            </button>
            {index < arr.length - 1 && <span style={{ color: '#d9d9d9' }}>|</span>}
          </React.Fragment>
        ))}
      </div>

      {/* 底部额外信息 */}
      <div style={{ fontSize: '12px', color: '#8c8c8c' }}>
        <p style={{ margin: '4px 0' }}>让每个人都能在哈哈星球上找到属于自己的学习乐趣</p>
        <p style={{ margin: '4px 0' }}>ICP备案号：京ICP备2024000000号 | 网络文化经营许可证</p>
      </div>
    </footer>
  );
};
