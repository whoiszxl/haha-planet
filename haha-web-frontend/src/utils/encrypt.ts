import JSEncrypt from 'jsencrypt';

const PUBLIC_KEY = 'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM51dgYtMyF+tTQt80sfFOpSV27a7t9u'
  + 'aUVeFrdGiVxscuizE7H8SMntYqfn9lp8a5GH5P1/GGehVjUD2gF/4kcCAwEAAQ=='


/**
 * RSA 加密
 * @param text 要加密的文本
 * @returns 加密后的文本
 */
export function encrypt(text: string): string | false {
  try {
    const encryptor = new JSEncrypt();
    encryptor.setPublicKey(PUBLIC_KEY);
    return encryptor.encrypt(text);
  } catch (error) {
    console.error('RSA 加密失败:', error);
    return false;
  }
}
