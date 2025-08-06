import { notification } from 'antd';

export type NotificationType = 'success' | 'info' | 'warning' | 'error';

interface NotificationOptions {
  message: string;
  description?: string;
  duration?: number;
  placement?: 'topLeft' | 'topRight' | 'bottomLeft' | 'bottomRight';
}

class NotifyUtils {
  // 显示通知
  static showNotification(
    type: NotificationType,
    { message, description, duration = 4.5, placement = 'topRight' }: NotificationOptions
  ) {
    notification[type]({
      message,
      description,
      duration,
      placement,
    });
  }

  // 成功通知
  static success(message: string, description?: string) {
    this.showNotification('success', { message, description });
  }

  // 信息通知
  static info(message: string, description?: string) {
    this.showNotification('info', { message, description });
  }

  // 警告通知
  static warning(message: string, description?: string) {
    this.showNotification('warning', { message, description });
  }

  // 错误通知
  static error(message: string, description?: string) {
    this.showNotification('error', { message, description });
  }
}

export default NotifyUtils;
