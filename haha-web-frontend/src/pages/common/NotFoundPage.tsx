import React from 'react';
import { Link } from 'react-router-dom';
import styles from './NotFound.module.css';

export const NotFoundPage: React.FC = () => {
    return (
        <div className={styles.notFoundContainer}>
          <div className={styles.glitch} data-text="404">404</div>
          <div className={styles.content}>
            <h2 className={styles.errorTitle}>页面未找到</h2>
            <p className={styles.errorMessage}>
              看起来您要找的页面已经消失在了数字世界中...
            </p>
            <div className={styles.actionButtons}>
              <Link to="/" className={styles.homeButton}>
                返回首页
              </Link>
              <button 
                onClick={() => window.history.back()} 
                className={styles.backButton}
              >
                返回上一页
              </button>
            </div>
          </div>
        </div>
      );
};
