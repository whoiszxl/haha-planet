// 登录响应类型
export interface LoginResp {
    token: string;
    type: string;
  }
  
  // 第三方登录授权类型
  export interface SocialAuthAuthorizeResp {
    authorizeUrl: string
  }


  // 会员信息响应类型
  export interface MemberInfoResponse {
    id: string;
    username: string;
    phone: string;
    email: string;
    customUrl: string;
    personaName: string;
    realName: string;
    summary: string;
    wxCode: string;
    birthday: string;
    country: string;
    province: string;
    city: string;
    district: string;
    gender: number;
    avatar: string;
    avatarFrame: string;
    personaBg: string;
    miniPersonaBg: string;
    personaThemeColor: string;
    points: number;
    level: number;
    gameCount: number;
    screenshotCount: number;
    friendCount: number;
    registerIp: string;
    lastIp: string;
    loginCount: number;
    loginErrorCount: number;
    lastLogin: string;
  }
  