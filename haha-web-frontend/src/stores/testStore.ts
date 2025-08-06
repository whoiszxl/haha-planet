import { create } from 'zustand';

interface TestStore {
  bears: number;
  increasePopulation: () => void;
  removeAllBears: () => void;
}

const useTestStore = create<TestStore>((set) => ({
  bears: 0,
  increasePopulation: () => set((state) => ({ bears: state.bears + 1 })),
  removeAllBears: () => set({ bears: 0 }),
}));

export default useTestStore;