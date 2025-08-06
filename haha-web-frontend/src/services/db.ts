import { MessageProps } from '@chatui/core';

/**
 * 聊天消息数据库服务
 * 使用IndexedDB存储聊天记录
 */

// 数据库名称和版本
const DB_NAME = 'cteam_chat_db';
const DB_VERSION = 1;

// 存储对象名称
const MESSAGES_STORE = 'messages';

// 消息存储接口
export interface MessageRecord extends Omit<MessageProps, '_id'> {
  _id: string;
  friendId: string; // 用于按好友分组
  read?: boolean;
}

/**
 * 初始化数据库
 * @returns Promise<IDBDatabase> 返回数据库连接
 */
export const initDatabase = (): Promise<IDBDatabase> => {
  return new Promise((resolve, reject) => {
    // 打开数据库连接
    const request = indexedDB.open(DB_NAME, DB_VERSION);
    
    // 数据库升级事件
    request.onupgradeneeded = (event) => {
      const db = request.result;
      
      // 创建消息存储
      if (!db.objectStoreNames.contains(MESSAGES_STORE)) {
        // 创建消息存储，使用_id作为主键
        const store = db.createObjectStore(MESSAGES_STORE, { keyPath: '_id' });
        
        // 创建索引，方便按好友ID和时间查询
        store.createIndex('by_friend', 'friendId', { unique: false });
        store.createIndex('by_time', 'createdAt', { unique: false });
        store.createIndex('by_friend_time', ['friendId', 'createdAt'], { unique: false });
      }
    };
    
    // 数据库打开成功
    request.onsuccess = () => {
      console.log('聊天数据库初始化成功');
      resolve(request.result);
    };
    
    // 数据库打开失败
    request.onerror = () => {
      console.error('无法打开聊天数据库', request.error);
      reject(request.error);
    };
  });
};

/**
 * 保存消息到数据库
 * @param message 消息对象
 * @returns Promise<void>
 */
export const saveMessage = async (message: MessageRecord): Promise<void> => {
  try {
    const db = await initDatabase();
    
    return new Promise((resolve, reject) => {
      // 开始事务
      const transaction = db.transaction([MESSAGES_STORE], 'readwrite');
      const store = transaction.objectStore(MESSAGES_STORE);
      
      // 添加消息
      const request = store.put(message);
      
      request.onsuccess = () => {
        console.log('消息保存成功', message._id);
        resolve();
      };
      
      request.onerror = () => {
        console.error('保存消息失败', request.error);
        reject(request.error);
      };
      
      // 关闭数据库连接
      transaction.oncomplete = () => {
        db.close();
      };
    });
  } catch (error) {
    console.error('保存消息时出错', error);
    throw error;
  }
};

/**
 * 获取指定好友的聊天记录
 * @param friendId 好友ID
 * @param limit 限制条数，默认50条
 * @returns Promise<MessageRecord[]> 消息记录数组
 */
export const getMessagesByFriend = async (friendId: string, limit = 50): Promise<MessageRecord[]> => {
  try {
    const db = await initDatabase();
    
    return new Promise((resolve, reject) => {
      const transaction = db.transaction([MESSAGES_STORE], 'readonly');
      const store = transaction.objectStore(MESSAGES_STORE);
      
      // 使用好友ID索引查询
      const index = store.index('by_friend_time');
      const range = IDBKeyRange.bound([friendId, 0], [friendId, Date.now()]);
      
      // 按时间倒序查询最近的消息
      const request = index.getAll(range, limit);
      
      request.onsuccess = () => {
        // 按时间排序
        const messages = request.result.sort((a, b) => a.createdAt - b.createdAt);
        console.log(`获取好友${friendId}的聊天记录`, messages.length);
        resolve(messages);
      };
      
      request.onerror = () => {
        console.error('获取聊天记录失败', request.error);
        reject(request.error);
      };
      
      // 关闭数据库连接
      transaction.oncomplete = () => {
        db.close();
      };
    });
  } catch (error) {
    console.error('获取聊天记录时出错', error);
    return [];
  }
};

/**
 * 删除指定好友的所有聊天记录
 * @param friendId 好友ID
 * @returns Promise<void>
 */
export const deleteMessagesByFriend = async (friendId: string): Promise<void> => {
  try {
    const db = await initDatabase();
    
    return new Promise((resolve, reject) => {
      const transaction = db.transaction([MESSAGES_STORE], 'readwrite');
      const store = transaction.objectStore(MESSAGES_STORE);
      
      // 使用游标遍历并删除消息
      const index = store.index('by_friend');
      const request = index.openCursor(IDBKeyRange.only(friendId));
      
      request.onsuccess = (event) => {
        // @ts-ignore
        const cursor = event.target.result;
        if (cursor) {
          cursor.delete();
          cursor.continue();
        }
      };
      
      transaction.oncomplete = () => {
        console.log(`删除好友${friendId}的聊天记录成功`);
        db.close();
        resolve();
      };
      
      transaction.onerror = () => {
        console.error('删除聊天记录失败', transaction.error);
        reject(transaction.error);
      };
    });
  } catch (error) {
    console.error('删除聊天记录时出错', error);
    throw error;
  }
};

/**
 * 清空所有聊天记录
 * @returns Promise<void>
 */
export const clearAllMessages = async (): Promise<void> => {
  try {
    const db = await initDatabase();
    
    return new Promise((resolve, reject) => {
      const transaction = db.transaction([MESSAGES_STORE], 'readwrite');
      const store = transaction.objectStore(MESSAGES_STORE);
      
      // 清空存储
      const request = store.clear();
      
      request.onsuccess = () => {
        console.log('清空所有聊天记录成功');
        resolve();
      };
      
      request.onerror = () => {
        console.error('清空聊天记录失败', request.error);
        reject(request.error);
      };
      
      // 关闭数据库连接
      transaction.oncomplete = () => {
        db.close();
      };
    });
  } catch (error) {
    console.error('清空聊天记录时出错', error);
    throw error;
  }
};

export default {
  initDatabase,
  saveMessage,
  getMessagesByFriend,
  deleteMessagesByFriend,
  clearAllMessages
};
