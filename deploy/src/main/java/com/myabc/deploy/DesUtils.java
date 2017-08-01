package com.myabc.deploy;

import java.math.BigDecimal;
import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;

import com.myabc.config.config;

/**
 * DES加密和解密工�?可以对字符串进行加密和解密操�?�?
 */
public class DesUtils {
    /** 字符串默认键�? */
    private static String strDefaultKey = "national";
    
    /** 加密工具 */
    private Cipher encryptCipher = null;
    
    /** 解密工具 */
    private Cipher decryptCipher = null;
    private static DesUtils desUtil;
    
    /**
     * 将byte数组转换为表�?6进制值的字符串， 如：byte[]{8,18}转换为：0813�?和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可�?的转换过�?
     * 
     * @param arrB
     *            �?��转换的byte数组
     * @return 转换后的字符�?
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛�?
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，�?��字符串的长度是数组长度的两�?
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数�?��在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }
    
    /**
     * 将表�?6进制值的字符串转换为byte数组�?和public static String byteArr2HexStr(byte[] arrB)
     * 互为可�?的转换过�?
     * 
     * @param strIn
     *            �?��转换的字符串
     * @return 转换后的byte数组
     * @throws Exception
     *             本方法不处理任何异常，所有异常全部抛�?
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes("UTF-8");
        int iLen = arrB.length;
        
        // 两个字符表示�?��字节，所以字节数组长度是字符串长度除�?
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }
    
    /**
     * 默认构�?方法，使用默认密�?
     * 
     * @throws Exception
     */
    public DesUtils() throws Exception {
        this(strDefaultKey);
    }
    
    /**
     * 指定密钥构�?方法
     * 
     * @param strKey
     *            指定的密�?
     * @throws Exception
     */
    public DesUtils(String strKey) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());
        
        encryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        
        decryptCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        
    }
    /**
     * 
     */
    public  static synchronized DesUtils Get_Instance()
    {
		if(desUtil==null)
			{
			try {
				desUtil=new DesUtils(config.getPropertieByName("DesPassword"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			return desUtil;
    	
    }
    /**
     * 加密字节数组
     * 
     * @param arrB
     *            �?��密的字节数组
     * @return 加密后的字节数组
     * @throws Exception
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }
    
    /**
     * 加密字符�?
     * 
     * @param strIn
     *            �?��密的字符�?
     * @return 加密后的字符�?
     * @throws Exception
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes("UTF-8")));
    }
    
    /**
     * 解密字节数组
     * 
     * @param arrB
     *            �?��密的字节数组
     * @return 解密后的字节数组
     * @throws Exception
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }
    
    /**
     * 解密字符�?
     * 
     * @param strIn
     *            �?��密的字符�?
     * @return 解密后的字符�?
     * @throws Exception
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }
    
    /**
     * 从指定字符串生成密钥，密钥所�?��字节数组长度�?�?不足8位时后面�?，超�?位只取前8�?
     * 
     * @param arrBTmp
     *            构成该字符串的字节数�?
     * @return 生成的密�?
     * @throws java.lang.Exception
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建�?��空的8位字节数组（默认值为0�?
        byte[] arrB = new byte[8];
        
        // 将原始字节数组转换为8�?
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        
        return key;
    }
    
    /**
     * main方法
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            // ���
            // String test =
            // "{lsh:\"GD111EASD11111111\",yhwd:\"007\",yhgy:\"011111111\",gsdm:\"6016\",yhbm:\"6002003110\",qfyskId:\"241099671,247164366L\",zje:\"27\"}";//
            // String test =
            // "{yhwd:\"007\",yhgy:\"011111111\",yhbm:\"6002003110\",gsdm:\"6016\",zje:\"20\",lsh:\"GD11201511020908070611009991\"}";//
            // Ԥ��
            // String test =
            // "{lsh:\"HQ2017032331508846\",yhwd:\"002\",yhgy:\"18327\",yhbm:\"1300900903221\",gsdm:\"6016\",je:\"20\"}";//
            // ic卡缴费
            // String test =
            // "{lsh:\"HQ2017032331508846\",yhwd:\"002\",yhgy:\"18327\",gsdm:\"6006\",yhbm:\"1300900903221\",jflb:\"0\",kh:\"00903221\",icinfoid:\"230026865\",zje:\"500.00\"}";
            // Ĩ��
            // icka query
            // String test =
            // "{yhwd:\"002\",yhgy:\"0001\",gsdm:\"6006\",yhbm:\"1309809800011\",icgsdm:\"6006\", kh:\"09800011\", bsh:\"60184530\", fflb:\"0\"}";
            // ��ѯ query 水厂
            // String test = "{yhbm:\"110080813\",yhwd:\"002\"}";
            // huayou query
            String test = "{yhwd:\"007\",yhgy:\"0001\",gsdm: \"6003\",yhbm:\"46400116\"}";
            // jiaofei
            // String test =
            // "{lsh:\"GD111EASD1111111121\",yhwd:\"009\",yhgy:\"0001\",gsdm:\"7001\",yhbm:\"510000595\",qfyskId:\"0\",zje:\"697\"}";//
            // yucun
            // String test =
            // "{lsh:\"GD111EASD111111112\",yhwd:\"009\",yhgy:\"0001\",gsdm:\"7001\",yhbm:\"510000595\",zje:\"100.111\"}";//

            // String test = "{yhbm:\"110001098\",yhwd:\"001\"}";
            DesUtils des = new DesUtils("111111");// 加密代码 //

            System.out.println("加密前的字符：" + test);
            System.out.println("加密后的字符：" + des.encrypt(test));
            System.out.println("解密后的字符：" + des.decrypt("f04130b07ad55ea118e22e2ebc24d6b2fb1e2449657e18d6d16920bc8ad67fa139104b9e3095471ea5f1a0d4c656267501bb2f1efb616d407a4b6f236d67ada2f3c36b97f5086aea39b617980aac01f50ace37ba13970edf56edba6c8e0930cc153e6a8d6f879d623610f57d58dc61211d9742f94c568496d842a5096927e5fb02a1510440824795d218673d6df44c6d999c50c957f6b0032f920fcdd4e6d32fa04011bf4b46f72c21a42dd0b89dee7e7398f8ef1bd525679af3d8b1fe34966989fb753c39070e98f51c014f7fb55aed8d4e0281a4015e6f7b3182b53491e489c9ef5e8e754e5faaa134ec4f4534c48cfcc12081279650bf989105234c3ee896d7774902d13030302a2dc82a34a5583668a76e784415efe783ec62fdced88eea17bbcb20e5f5783fee1199a9dc3604eb"));

            
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
