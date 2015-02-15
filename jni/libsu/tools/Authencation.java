package com.qihoo360.security;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Arrays;

import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.ServiceManager;
import android.util.Log;

/**
 * 验证给定 uid 的应用是否是 360 签名。用法：app_process /system/bin com.qihoo360.security.Authencation <uid>
 * 
 * @author zhangxu
 *
 */
public class Authencation {
	
	private static final String PKGNAME_PREFIX = "com.qihoo360.mobilesafe";
	
	private static final String AUTHMGR_PREFIX = "com.qihoo.root";
	
	/** 360 证书的 MD5 */
	//private static final byte [] QIHOO_SIG_HASH = new byte [] { (byte) 0xdc, 0x6d, (byte) 0xbd, 0x6e, 0x49, 0x68, 0x2a, 0x57, (byte) 0xa8, (byte) 0xb8, 0x28, (byte) 0x89, 0x04, 0x3b, (byte) 0x93, (byte) 0xa8 };
	
	 /*
     * 允许的签名的MD5值 { 卫士正式证书，卫士测试证书，安全通讯录证书, 360桌面正式证书, 手机助手正式证书，手机助手测试证书1， 手机助手测试证书2,卫士海外版正式签名,卫士海外版测试签名} ,杀毒单品证书}
     */
	private static final String[] ALLOWED_SIG = { "dc6dbd6e49682a57a8b82889043b93a8",
        "2731710b7b726b51ab58e8ccbcfeb586", "3bd87d5c8d98f7d711eff0d82d8fe7b9", "1d4dcf3a79293e05fa9744444263d048",
        "ca45263bc938da16ef1b069c95e61ba2", "85b6bfbb179f2467bd5e5e53577d8b15", "3093dc0f7ce2079d807d78a798231e9b","fec53268a38f029357056d46098c9384","bb5cf8250d16d684a7b1e28b12780636","dc27396cf677dd234244dee1e5ff01ed"};
	
	private static final String AUTHMGR_SIG = "12332c1955435e036a5a94df4e188bd7";
	
	  /**
     * 中兴手机IPackageManager需要从父类取
     *
     * @param pm
     * @return
     */
    private static IPackageManager getRealPm(IPackageManager pm) {
        Field f;
        try {
            Class<? extends IPackageManager> c = pm.getClass();
            Class<?> part = c.getSuperclass();
            f = part.getDeclaredField("mIPackageManager");
            f.setAccessible(true);
            IPackageManager iPm = (IPackageManager) f.get(pm);
            return iPm;
        } catch (Exception e) {
        }
        return null;
    }
	/**
	 * @param args   Authencation uid [DEBUG=true|false]
	 */
	public static void main(String[] args) {
		
		boolean DEBUG = false; 
		
		boolean isExit = false;
		
		if ( args != null && args.length > 2 ) {
			
			if("true".equals(args[1])){
				DEBUG = true;
			}
			
			if("exit".equals(args[2])){
				isExit = true;
			}
			//System.out.println("uid "+args[0]+" is waiting to be checked");
			if(DEBUG)
				Log.v("DEBUG", "uid "+args[0]+" is waiting to be checked");
		
			// 解析用户输入的 uid
			int uid = 0;
			try {
				uid = Integer.parseInt(args[0]);
			}
			catch (Exception e) {
				//System.out.println(e.getMessage());
				return;
			}
			
			if ( uid > 0 ) {
				
//				System.out.println("uid: " + uid);
				
				IPackageManager pm =  IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
				if ( pm != null ) {
					// 查询给定 uid 所对应的所有的包
					String[] pkgs = null;
			        try {
			        	pkgs = pm.getPackagesForUid(uid);
			        } catch (Exception tr) {
			            try{
    			            IPackageManager _pm = getRealPm(pm);
    			            if(_pm != null){
    			                pkgs = _pm.getPackagesForUid(uid);
    			            }
			            }catch(Exception e){
			                
			            }
			        }
			        
			        if(DEBUG){
			        	if(pkgs !=null &&pkgs.length >0)
			        		Log.v("DEBUG", "enter with a pkg  "+pkgs[0]);
			        	else
			        		Log.v("DEBUG","pkgs is empty");
			        }
			        if (pkgs == null || pkgs.length == 0) {
			        	
			        }else {
			        	// 对于该 uid 下的每个包，验证是否是 360 的包
			        	for (String callerPkg : pkgs) {
			        		
//			        		System.out.println("Package: " + callerPkg);
			        		
			        		// 首先根据包名
			        		if (callerPkg.startsWith("com.qihoo")) {
			        			// 然后验证签名
			        			PackageInfo pkgInfo = null;
			        			try {
			        				pkgInfo = getPackage(callerPkg,0,pm);
			        			}catch (Exception e) {
			        			}
			        			
			        			if(DEBUG){
			        				Log.v("DEBUG", "package info is "+pkgInfo);
			        			}
			        			if ( pkgInfo != null ) {
			        				Signature[] sigs = pkgInfo.signatures;
			        				if (sigs != null && sigs.length > 0) {
			        					
//			        					System.out.print("Verify signature...");
			        					
			        					for (Signature sig : sigs) {
			        						if (isSigValid(MD5(sig.toByteArray()),DEBUG)) {
//			        							System.out.println("Verified.");
			        							// 如果匹配，返回 100
			        							if(DEBUG){
			        								Log.v("DEBUG", "find a matched signatures");
			        							}
			        							if(isExit)
			        								System.exit(100);
			        							else
			        								System.out.println("100");
			        							return;
			        						}
			        						else if(isSigAuthMgr(MD5(sig.toByteArray()),DEBUG)){
//			        							System.out.println("Bad.");
			        							if(DEBUG){
			        								Log.v("DEBUG", "find a authmgr signatures");
			        							}
			        							if(isExit)
			        								System.exit(90);
			        							else
			        								System.out.println("90");
			        							return;
			        						}
			        					}
			        						
			        				}	
			        			}
			        		}
				        }
			        }
				}
//				else {
//					System.out.println("can not open PackageManager");
//				}
			}
		}
	}

	private static byte [] MD5(byte[] input) {
		if(input == null)
			return null;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(input);
            return md.digest();
        } catch (Exception e) {
        	//System.out.println(e.getMessage());
        }
        return null;
    }
	
	
	private static String bytesToHexString(byte[] bytes) {
        if (bytes == null)
            return null;
        String table = "0123456789abcdef";
        StringBuilder ret = new StringBuilder(2 * bytes.length);

        for (int i = 0; i < bytes.length; i++) {
            int b;
            b = 0x0f & (bytes[i] >> 4);
            ret.append(table.charAt(b));
            b = 0x0f & bytes[i];
            ret.append(table.charAt(b));
        }

        return ret.toString();
    }
	
	
	public static boolean isSigValid(byte[] sigb,boolean debug){
		if(sigb == null)
			return false;
		String sig = bytesToHexString(sigb);
		if(debug){
			Log.v("DEBUG", "checking sig for "+sig);
		}
		
		if(sig == null)
			return false;
		
		for(String _sig:ALLOWED_SIG){
			if(sig.equals(_sig))
				return true;
		}
		return false;
	}
	
	public static boolean isSigAuthMgr(byte[] sigb,boolean debug){
		if(sigb == null)
			return false;
		String sig = bytesToHexString(sigb);
		if(debug){
			Log.v("DEBUG", "checking auth sig for "+sig);
		}
		
		if(sig == null)
			return false;
		
		if(sig.equals(AUTHMGR_SIG))
			return true;
		return false;
	}
	
	private static PackageInfo getPackage(String packages,int uid,IPackageManager pm){
		if(packages == null)
			return null;
		Method method = null;
		try {
			Method[] methods = pm.getClass().getDeclaredMethods();
			int len = methods.length;
			for (int i =0 ;i<len;i++){
				if("getPackageInfo".equals(methods[i].getName())){
					method = methods[i];
					break;
				}
			}
			
			if (method == null) {
                pm = getRealPm(pm);
                methods = pm.getClass().getDeclaredMethods();
                len = methods.length;
                for (int i = 0; i < len; i++) {
                    if ("getPackageInfo".equals(methods[i].getName())) {
                        method = methods[i];
                        break;
                    }
                }
            }
			
			Object[] objs = null;
			
			int paramLen = method.getParameterTypes().length;
			if(paramLen == 2){
				objs = new Object[]{packages,PackageManager.GET_SIGNATURES};
			}else{
				objs = new Object[]{packages,PackageManager.GET_SIGNATURES,0};
			}
			Object res = method.invoke(pm, objs);
			
			if(res != null)
				return (PackageInfo)res;
		} catch (Exception e1) {
			return null;
		}
		return null;
	} 
}
