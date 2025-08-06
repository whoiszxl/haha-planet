import { useState, useEffect, useCallback } from 'react';
import { message } from 'antd';

/**
 * 剪贴板权限状态
 */
export type ClipboardPermissionState = 'granted' | 'denied' | 'prompt' | 'unavailable';

/**
 * 剪贴板钩子返回类型
 */
interface UseClipboardPermissionReturn {
  /**
   * 当前剪贴板权限状态
   */
  permissionState: ClipboardPermissionState;
  
  /**
   * 请求剪贴板权限
   * @returns 权限请求的结果
   */
  requestPermission: () => Promise<ClipboardPermissionState>;
  
  /**
   * 复制文本到剪贴板
   * @param text 要复制的文本
   * @param successMessage 复制成功时显示的消息
   * @param errorMessage 复制失败时显示的消息
   * @returns 是否复制成功
   */
  copyText: (
    text: string,
    successMessage?: string,
    errorMessage?: string
  ) => Promise<boolean>;
  
  /**
   * 剪贴板API是否可用
   */
  isAvailable: boolean;
}

/**
 * 剪贴板权限管理钩子
 * 
 * 提供剪贴板权限状态检测、请求权限和复制文本功能
 */
export function useClipboardPermission(): UseClipboardPermissionReturn {
  // 剪贴板API是否可用
  const [isAvailable, setIsAvailable] = useState<boolean>(false);
  // 当前权限状态
  const [permissionState, setPermissionState] = useState<ClipboardPermissionState>('unavailable');

  // 初始化时检查剪贴板API和权限状态
  useEffect(() => {
    const checkClipboardAvailability = async () => {
      // 检查navigator.clipboard API是否可用
      const clipboardAvailable = !!(
        navigator.clipboard && 
        typeof navigator.clipboard.writeText === 'function'
      );
      
      setIsAvailable(clipboardAvailable);
      
      // 如果支持权限API，检查当前权限状态
      if (clipboardAvailable && navigator.permissions) {
        try {
          const permissionStatus = await navigator.permissions.query({ name: 'clipboard-write' as PermissionName });
          setPermissionState(permissionStatus.state as ClipboardPermissionState);
          
          // 监听权限变化
          permissionStatus.addEventListener('change', () => {
            setPermissionState(permissionStatus.state as ClipboardPermissionState);
          });
        } catch (e) {
          // 如果权限API不支持clipboard-write，但clipboard API可用，
          // 我们假设可以使用（将在实际使用时处理错误）
          setPermissionState('prompt');
        }
      } else if (clipboardAvailable) {
        // 如果剪贴板API可用但不支持权限API，假设权限为prompt状态
        setPermissionState('prompt');
      } else {
        setPermissionState('unavailable');
      }
    };

    checkClipboardAvailability();
  }, []);

  /**
   * 请求剪贴板写入权限
   */
  const requestPermission = useCallback(async (): Promise<ClipboardPermissionState> => {
    if (!isAvailable) {
      return 'unavailable';
    }

    if (navigator.permissions) {
      try {
        const permissionStatus = await navigator.permissions.query({ name: 'clipboard-write' as PermissionName });
        setPermissionState(permissionStatus.state as ClipboardPermissionState);
        return permissionStatus.state as ClipboardPermissionState;
      } catch (e) {
        // 如果浏览器不支持此权限查询，尝试直接使用
        try {
          // 尝试写入一个空字符串来检查权限
          await navigator.clipboard.writeText('');
          setPermissionState('granted');
          return 'granted';
        } catch (err) {
          setPermissionState('denied');
          return 'denied';
        }
      }
    } else {
      // 没有权限API，但有剪贴板API，尝试直接使用
      try {
        await navigator.clipboard.writeText('');
        setPermissionState('granted');
        return 'granted';
      } catch (err) {
        setPermissionState('denied');
        return 'denied';
      }
    }
  }, [isAvailable]);

  /**
   * 复制文本到剪贴板
   */
  const copyText = useCallback(
    async (
      text: string,
      successMessage = '已复制到剪贴板',
      errorMessage = '复制失败，请手动复制'
    ): Promise<boolean> => {
      if (!text) return false;

      // 如果剪贴板API不可用，使用传统方法
      if (!isAvailable) {
        try {
          const textarea = document.createElement('textarea');
          textarea.value = text;
          textarea.style.position = 'fixed';  // 避免滚动到视图
          document.body.appendChild(textarea);
          textarea.select();
          const successful = document.execCommand('copy');
          document.body.removeChild(textarea);
          
          if (successful) {
            message.success(successMessage);
            return true;
          } else {
            message.error(errorMessage);
            return false;
          }
        } catch (err) {
          console.error('复制失败:', err);
          message.error(errorMessage);
          return false;
        }
      }

      // 使用现代剪贴板API
      try {
        await navigator.clipboard.writeText(text);
        message.success(successMessage);
        setPermissionState('granted'); // 如果成功，更新状态为granted
        return true;
      } catch (err) {
        console.error('复制失败:', err);
        
        // 如果失败，可能是权限问题，尝试请求权限
        if (permissionState === 'prompt') {
          const newState = await requestPermission();
          if (newState === 'granted') {
            try {
              await navigator.clipboard.writeText(text);
              message.success(successMessage);
              return true;
            } catch (e) {
              message.error(errorMessage);
              return false;
            }
          } else {
            message.error('请授予剪贴板访问权限后重试');
            return false;
          }
        } else {
          message.error(errorMessage);
          return false;
        }
      }
    },
    [isAvailable, permissionState, requestPermission]
  );

  return {
    permissionState,
    requestPermission,
    copyText,
    isAvailable
  };
}

export default useClipboardPermission; 