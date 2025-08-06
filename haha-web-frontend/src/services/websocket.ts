import { message } from 'antd';
import { Buffer } from 'buffer';
import FingerprintJS from '@fingerprintjs/fingerprintjs';

// WebSocket connection status enum
export enum WebSocketStatus {
  CONNECTING = 0,
  OPEN = 1,
  CLOSING = 2,
  CLOSED = 3
}

// Message command types (matching the Java server)
export enum Command {
  LOGIN = 1002,
  LOGOUT = 1003,
  HEART_BEAT = 1004,
  PRIVATE_CHAT = 2001,
  GROUP_CHAT = 3001
}

// WebSocket service class
class WebSocketService {
  private ws: WebSocket | null = null;
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 5;
  private reconnectTimeout: NodeJS.Timeout | null = null;
  private heartbeatInterval: NodeJS.Timeout | null = null;
  private url: string = 'ws://127.0.0.1:40001/ws';
  private token: string = '';
  private clientType: number = 2; // Mobile client type
  private imei: string = '';
  private listeners: { eventType: string; callback: Function }[] = [];
  private fpPromise = FingerprintJS.load();

  // 生成设备指纹
  private async generateDeviceId(): Promise<string> {
    try {
      const fp = await this.fpPromise;
      const result = await fp.get();
      return `fp_${result.visitorId}`;
    } catch (error) {
      console.error('生成设备指纹失败:', error);
      return 'web_client_' + Date.now();
    }
  }

  // Initialize WebSocket connection
  public async connect(token: string, imei: string = '', url?: string): Promise<void> {
    if (this.ws && this.ws.readyState === WebSocketStatus.OPEN) {
      console.log('WebSocket连接已建立');
      return;
    }

    this.token = token;
    this.imei = imei || await this.generateDeviceId();
    
    if (url) {
      this.url = url;
    }

    try {
      this.ws = new WebSocket(this.url);
      
      this.ws.onopen = this.onOpen.bind(this);
      this.ws.onmessage = this.onMessage.bind(this);
      this.ws.onerror = this.onError.bind(this);
      this.ws.onclose = this.onClose.bind(this);
      
      console.log('正在连接WebSocket服务器...');
    } catch (error) {
      console.error('创建WebSocket连接失败:', error);
      this.attemptReconnect();
    }
  }

  // Handle WebSocket connection open
  private onOpen(): void {
    console.log('WebSocket连接已建立');
    this.reconnectAttempts = 0;
    
    // Send login message
    this.sendLoginMessage();
    
    // Start heartbeat
    this.startHeartbeat();
    
    // Notify listeners
    this.notifyListeners('open');
  }

  private onMessage(event: MessageEvent): void {
    try {
      console.log("eventdata", event.data);
      
      // 检查消息类型
      if (typeof event.data === 'string') {
        // 处理文本消息
        console.log('收到文本消息:', event.data);
        
        // 尝试解析JSON
        const jsonData = JSON.parse(event.data);
        
        // 通知监听器
        this.notifyListeners('message', jsonData);
        this.notifyListeners('chatMessage', jsonData);
        return;
      }
      
      // 处理Blob数据
      if (event.data instanceof Blob) {
        console.log('收到Blob消息');
        
        // 将Blob转换为文本
        const reader = new FileReader();
        reader.onload = () => {
          try {
            let text = reader.result as string;
            console.log('Blob原始文本:', text);
            
            // 尝试直接使用正则表达式匹配JSON对象
            const jsonMatch = text.match(/\{[\s\S]*\}/);
            if (jsonMatch) {
              const jsonText = jsonMatch[0];
              console.log('匹配到的JSON文本:', jsonText);
              
              try {
                // 尝试解析JSON
                const jsonData = JSON.parse(jsonText);
                console.log('解析后的JSON数据:', jsonData);
                
                // 通知监听器
                this.notifyListeners('message', jsonData);
                this.notifyListeners('chatMessage', jsonData);
                return;
              } catch (jsonError) {
                console.error('JSON解析失败:', jsonError);
              }
            }
            
            // 如果正则匹配失败，尝试手动处理
            console.log('尝试手动处理消息文本');
            
            // 移除所有不可见字符和BOM
            text = text.replace(/^\ufeff/, '').replace(/[^\x20-\x7E]/g, '');
            
            // 查找第一个{和最后一个}
            const startIdx = text.indexOf('{');
            const endIdx = text.lastIndexOf('}');
            
            if (startIdx !== -1 && endIdx !== -1 && endIdx > startIdx) {
              const jsonText = text.substring(startIdx, endIdx + 1);
              console.log('手动提取的JSON文本:', jsonText);
              
              try {
                const jsonData = JSON.parse(jsonText);
                console.log('手动解析的JSON数据:', jsonData);
                
                // 通知监听器
                this.notifyListeners('message', jsonData);
                this.notifyListeners('chatMessage', jsonData);
              } catch (jsonError) {
                console.error('手动JSON解析失败:', jsonError);
                
                // 最后的尝试：直接传递原始文本
                console.log('尝试直接传递原始文本');
                this.notifyListeners('message', { command: Command.PRIVATE_CHAT, rawText: text });
                this.notifyListeners('chatMessage', { command: Command.PRIVATE_CHAT, rawText: text });
              }
            } else {
              console.error('无法找到有效的JSON对象');
              // 直接传递原始文本
              this.notifyListeners('message', { command: Command.PRIVATE_CHAT, rawText: text });
              this.notifyListeners('chatMessage', { command: Command.PRIVATE_CHAT, rawText: text });
            }
          } catch (error) {
            console.error('处理Blob消息错误:', error);
            // 出错时也尝试通知监听器
            this.notifyListeners('error', error);
          }
        };
        
        reader.onerror = (error) => {
          console.error('读取Blob失败:', error);
          this.notifyListeners('error', error);
        };
        
        reader.readAsText(event.data);
        return;
      }
      
      // 处理二进制数据
      const data = event.data;
      if (!data) {
        console.error('收到空消息');
        return;
      }
      
      const arrayBuffer = data instanceof ArrayBuffer ? data : data.buffer;
      if (!arrayBuffer || arrayBuffer.byteLength < 4) {
        console.error('消息格式错误: 无效的ArrayBuffer');
        return;
      }

      const uint32Array = new Uint32Array(arrayBuffer, 0, 1);
      const messageLength = uint32Array[0];
      
      if (messageLength <= 0 || messageLength > arrayBuffer.byteLength - 4) {
        console.error('消息格式错误: 无效的消息长度', messageLength);
        return;
      }
      
      const messageBuffer = Buffer.from(arrayBuffer.slice(4, 4 + messageLength));
      const messageText = messageBuffer.toString('utf-8');
      
      console.log('收到二进制消息:', messageText);
      
      // 尝试解析JSON
      const jsonData = JSON.parse(messageText);
      
      // 通知监听器
      this.notifyListeners('message', jsonData);
      this.notifyListeners('chatMessage', jsonData);
      
    } catch (error) {
      console.error('解析消息错误:', error);
    }
  }

  // Handle WebSocket errors
  private onError(event: Event): void {
    console.error('WebSocket错误:', event);
    this.notifyListeners('error', event);
  }

  // Handle WebSocket connection close
  private onClose(event: CloseEvent): void {
    console.log('WebSocket连接关闭:', event.code, event.reason);
    
    // Clear heartbeat interval
    if (this.heartbeatInterval) {
      clearInterval(this.heartbeatInterval);
      this.heartbeatInterval = null;
    }
    
    // Notify listeners
    this.notifyListeners('close');
    
    // Attempt to reconnect
    this.attemptReconnect();
  }

  // Build binary message
  private buildMessage(command: Command, dataPack: any): Buffer {
    const jsonData = JSON.stringify(dataPack || {});
    
    const imei = Buffer.from(this.imei);
    const imeiLength = imei.length;
    const imeiLengthBytes = Buffer.alloc(4);
    imeiLengthBytes.writeInt32BE(imeiLength, 0);

    const token = Buffer.from(this.token);
    const tokenLength = token.length;
    const tokenLengthBytes = Buffer.alloc(4);
    tokenLengthBytes.writeInt32BE(tokenLength, 0);

    const commandBuffer = Buffer.alloc(4);
    commandBuffer.writeInt32BE(command, 0);

    const clientTypeBuffer = Buffer.from([this.clientType]);

    const body = Buffer.from(jsonData, 'utf-8');
    const bodyLength = body.length;
    const bodyLengthBytes = Buffer.alloc(4);
    bodyLengthBytes.writeInt32BE(bodyLength, 0);

    return Buffer.concat([
      commandBuffer,
      clientTypeBuffer,
      tokenLengthBytes,
      imeiLengthBytes,
      bodyLengthBytes,
      token,
      imei,
      body
    ]);
  }

  // Send login message
  private sendLoginMessage(): void {
    if (!this.ws || this.ws.readyState !== WebSocketStatus.OPEN) {
      console.error('无法发送登录消息: WebSocket未连接');
      return;
    }

    const loginData = {
      memberId: localStorage.getItem('memberId') || '1',
      token: this.token
    };

    const messageBuffer = this.buildMessage(Command.LOGIN, loginData);
    this.ws.send(messageBuffer);
    console.log('登录消息已发送');
  }

  // Start heartbeat interval
  private startHeartbeat(): void {
    // Clear existing interval if any
    if (this.heartbeatInterval) {
      clearInterval(this.heartbeatInterval);
    }
    
    // Send heartbeat every 30 seconds
    this.heartbeatInterval = setInterval(() => {
      this.sendHeartbeat();
    }, 30000);
  }

  // Send heartbeat message
  private sendHeartbeat(): void {
    if (!this.ws || this.ws.readyState !== WebSocketStatus.OPEN) {
      console.error('无法发送心跳: WebSocket未连接');
      return;
    }

    const messageBuffer = this.buildMessage(Command.HEART_BEAT, {});
    this.ws.send(messageBuffer);
    console.log('心跳已发送');
  }

  // Attempt to reconnect to WebSocket server
  private attemptReconnect(): void {
    if (this.reconnectTimeout) {
      clearTimeout(this.reconnectTimeout);
    }

    if (this.reconnectAttempts >= this.maxReconnectAttempts) {
      console.error('达到最大重连尝试次数');
      message.error('WebSocket连接失败，请刷新页面重试');
      return;
    }

    const delay = Math.min(1000 * Math.pow(2, this.reconnectAttempts), 30000);
    console.log(`尝试在${delay}毫秒后重新连接...`);
    
    this.reconnectTimeout = setTimeout(() => {
      this.reconnectAttempts++;
      this.connect(this.token, this.imei);
    }, delay);
  }

  // Disconnect WebSocket
  public disconnect(): void {
    if (!this.ws) {
      return;
    }

    // Clear intervals and timeouts
    if (this.heartbeatInterval) {
      clearInterval(this.heartbeatInterval);
      this.heartbeatInterval = null;
    }

    if (this.reconnectTimeout) {
      clearTimeout(this.reconnectTimeout);
      this.reconnectTimeout = null;
    }

    // Send logout message if connected
    if (this.ws.readyState === WebSocketStatus.OPEN) {
      const messageBuffer = this.buildMessage(Command.LOGOUT, {});
      this.ws.send(messageBuffer);
    }

    // Close connection
    this.ws.close();
    this.ws = null;
  }

  // Check if WebSocket is connected
  public isConnected(): boolean {
    return this.ws !== null && this.ws.readyState === WebSocketStatus.OPEN;
  }

  // Send a message to a user
  public sendPrivateMessage(toMemberId: string, content: string): void {
    if (!this.ws || this.ws.readyState !== WebSocketStatus.OPEN) {
      console.error('无法发送消息: WebSocket未连接');
      message.error('消息发送失败：未连接到服务器');
      return;
    }

    const messageData = {
      fromMemberId: localStorage.getItem('memberId') || '1',
      toMemberId: toMemberId,
      messageId: 'msg_' + Date.now(),
      body: content
    };

    const messageBuffer = this.buildMessage(Command.PRIVATE_CHAT, messageData);
    this.ws.send(messageBuffer);
    console.log('私聊消息已发送给', toMemberId);
  }
  
  // Send a custom command with data
  public sendCommand(command: Command, data: any): void {
    if (!this.ws || this.ws.readyState !== WebSocketStatus.OPEN) {
      console.error('无法发送命令: WebSocket未连接');
      return;
    }
    
    const messageBuffer = this.buildMessage(command, data);
    this.ws.send(messageBuffer);
    console.log(`命令 ${command} 已发送`);
  }
  
  // Add event listener
  public addListener(eventType: string, callback: Function): void {
    if (!this.listeners.some(listener => listener.eventType === eventType && listener.callback === callback)) {
      this.listeners.push({ eventType, callback });
    }
  }
  
  // Remove event listener
  public removeListener(callback: Function): void {
    this.listeners = this.listeners.filter(listener => listener.callback !== callback);
  }
  
  // Notify all listeners of an event
  private notifyListeners(eventType: string, data?: any): void {
    this.listeners.forEach(listener => {
      if (listener.eventType === eventType) {
        listener.callback(data);
      }
    });
  }

  // Send a group message
  public sendGroupMessage(groupChatData: {
    groupId: string;
    messageId: string;
    fromMemberId: string;
    body: string;
  }): void {
    if (!this.ws || this.ws.readyState !== WebSocketStatus.OPEN) {
      console.error('WebSocket未连接，无法发送群聊消息');
      return;
    }

    const messageData = {
      command: Command.GROUP_CHAT,
      data: {
        groupId: String(groupChatData.groupId),
        messageId: groupChatData.messageId,
        fromMemberId: String(groupChatData.fromMemberId),
        body: groupChatData.body,
        sequence: String(Date.now())
      }
    };

    const messageBuffer = this.buildMessage(Command.GROUP_CHAT, messageData.data);
    this.ws.send(messageBuffer);
    console.log('发送群聊消息:', messageData);
  }
}

// Create a singleton instance
const websocketService = new WebSocketService();
export default websocketService;
