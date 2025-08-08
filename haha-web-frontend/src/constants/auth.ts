export const AUTH = {
    CLIENT_KEY: 'web_client_001',
    AUTH_TYPES: {
        SMS: 'SMS',
        EMAIL: 'EMAIL',
        PASSWORD: 'PASSWORD',
        SOCIAL: 'SOCIAL'
    },
    SMS: {
        MIN_LENGTH: 4,
        MAX_LENGTH: 6,
        COUNTDOWN: 60,
        PLACEHOLDER: {
            BEFORE_SEND: '请先获取验证码',
            AFTER_SEND: '请输入4-6位验证码'
        }
    },
    PHONE: {
        LENGTH: 11,
        PATTERN: /^1[3-9]\d{9}$/,
        PLACEHOLDER: '请输入11位手机号'
    },
    EMAIL: {
        PATTERN: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
        PLACEHOLDER: '请输入邮箱地址',
        CODE: {
            MIN_LENGTH: 4,
            MAX_LENGTH: 6,
            PLACEHOLDER: {
                BEFORE_SEND: '请先获取验证码',
                AFTER_SEND: '请输入4-6位验证码'
            }
        }
    }
} as const;

export const ERROR_MESSAGES = {
    PHONE: {
        EMPTY: '手机号不能为空',
        LENGTH: '手机号必须是11位',
        INVALID: '请输入正确的手机号'
    },
    SMS: {
        EMPTY: '验证码不能为空',
        MIN_LENGTH: '验证码至少4位',
        MAX_LENGTH: '验证码最多6位',
        INVALID: '验证码必须是4-6位数字',
        IN_PROGRESS: '请输入4-6位验证码'
    },
    EMAIL: {
        EMPTY: '邮箱不能为空',
        INVALID: '请输入正确的邮箱地址',
        CODE: {
            EMPTY: '验证码不能为空',
            MIN_LENGTH: '验证码至少4位',
            MAX_LENGTH: '验证码最多6位',
            INVALID: '验证码必须是4-6位数字',
            IN_PROGRESS: '请输入4-6位验证码'
        }
    }
} as const; 