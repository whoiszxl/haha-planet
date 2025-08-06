export interface Message {
  id: string;
  role: 'user' | 'assistant' | 'system';
  content: string;
  timestamp: Date;
  isStreaming?: boolean;
}

export interface Test {
  id: string;
  title: string;
  messages: Message[];
  favorite: boolean;
  lastUpdated: Date;
} 