import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { getMemberInfo } from '../apis/auth/auth';
import { MemberInfoResponse } from '../apis/auth/type';

  
// 定义 store 类型
interface MemberStore {
  member: MemberInfoResponse | null;
  isLoading: boolean;
  error: string | null;
  fetchMemberData: () => Promise<void>;
  clearMemberData: () => void;
}

const useMemberStore = create<MemberStore>()(
  persist(
    (set) => ({
      member: null,
      isLoading: false,
      error: null,
      fetchMemberData: async () => {
        console.log("开始获取用户信息");
        set({ isLoading: true, error: null });
        try {
          const response = await getMemberInfo(); // 替换成你的接口地址
          console.log("获取到用户信息", response);
          set({ member: response.data, isLoading: false });
        } catch (error) {
          set({ error: 'Failed to fetch member data', isLoading: false });
        }
      },
      clearMemberData: () => set({ member: null }),
    }),
    {
      name: 'member-info-storage', // 使用 localStorage 持久化数据
      // 添加这个配置来避免额外的 state 包装
      partialize: (state) => ({
        member: state.member,
        isLoading: state.isLoading,
        error: state.error,
      }),
    }
  )
);

export default useMemberStore;
