import dayjs from 'dayjs'

/**
 * 格式化日期时间
 * @param date 日期时间
 * @param format 格式化模板
 * @returns 格式化后的日期时间字符串
 */
export const formatDateTime = (date?: string | number | Date, format = 'YYYY-MM-DD HH:mm:ss'): string => {
  if (!date) return '-'
  return dayjs(date).format(format)
}

/**
 * 格式化金额
 * @param amount 金额
 * @param precision 精度
 * @returns 格式化后的金额字符串
 */
export const formatAmount = (amount?: number, precision = 8): string => {
  if (amount === undefined || amount === null) return '-'
  return amount.toFixed(precision)
}

/**
 * 格式化文件大小
 * @param bytes 字节数
 * @returns 格式化后的文件大小字符串
 */
export const formatFileSize = (bytes?: number): string => {
  if (!bytes) return '-'
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let size = bytes
  let unitIndex = 0
  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024
    unitIndex++
  }
  return `${size.toFixed(2)} ${units[unitIndex]}`
}

/**
 * 格式化百分比
 * @param value 数值
 * @param precision 精度
 * @returns 格式化后的百分比字符串
 */
export const formatPercent = (value?: number, precision = 2): string => {
  if (value === undefined || value === null) return '-'
  return `${(value * 100).toFixed(precision)}%`
}

/**
 * 格式化手机号
 * @param phone 手机号
 * @returns 格式化后的手机号字符串
 */
export const formatPhone = (phone?: string): string => {
  if (!phone) return '-'
  return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
}

/**
 * 格式化银行卡号
 * @param cardNo 银行卡号
 * @returns 格式化后的银行卡号字符串
 */
export const formatBankCard = (cardNo?: string): string => {
  if (!cardNo) return '-'
  return cardNo.replace(/(\d{4})\d+(\d{4})/, '$1 **** **** $2')
} 