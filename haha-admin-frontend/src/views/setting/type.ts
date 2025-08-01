export interface ModeItem {
  title: string
  icon: string
  subtitle: string
  value?: string
  type: 'phone' | 'email' | 'gitee' | 'github' | 'password' | 'google'
  jumpMode?: 'link' | 'modal'
  status: boolean
  statusString: string
}
