export interface ModeItem {
  title: string
  icon: string
  subtitle: string
  value?: string
  type: 'phone' | 'email' | 'password' | 'google'
  jumpMode?: 'link' | 'modal'
  status: boolean
  statusString: string
}
