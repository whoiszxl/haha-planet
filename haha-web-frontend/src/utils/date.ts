// 添加一个智能时间格式化函数
export const formatSmartTime = (dateString: string): string => {
    if (!dateString) return '';
    
    const date = new Date(dateString);
    const now = new Date();
    
    // 检查日期是否有效
    if (isNaN(date.getTime())) return dateString;
    
    // 获取时和分，并确保是两位数
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    
    // 检查是否是今天
    const isToday = date.getDate() === now.getDate() && 
                    date.getMonth() === now.getMonth() && 
                    date.getFullYear() === now.getFullYear();
    if (isToday) {
        return `${hours}:${minutes}`;
    }
    
    // 计算日期差异（天数）
    const dayDiff = Math.floor((now.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
    
    // 检查是否是本周内（7天内）
    if (dayDiff < 7) {
        const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
        return weekdays[date.getDay()];
    }
    
    // 检查是否是今年
    const isThisYear = date.getFullYear() === now.getFullYear();
    if (isThisYear) {
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${month}-${day}`;
    }
    
    // 不是今年，显示年月日
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
};