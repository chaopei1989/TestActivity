package com.zero.test.jni;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.zero.R;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.os.Bundle;


public class HelloJni extends Activity
{
	private final static String TAG = "HelloJni";
	
	private final String AVDAEMON_EXE_NAME = "/lib/libsuav.so";
	private final String DAEMON_EXE_NAME = "/lib/libdeamon.so";
	private final String INJECT_EXE_NAME = "/lib/libinject.so";
	private final String LIB_NAME = "/lib/libtest.so";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	setContentView(R.layout.activity_hello_jni);
        super.onCreate(savedInstanceState);
        ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
        
        Method[] methods = HelloJni.class.getMethods();
        
        for (final Method method : methods) {
        	if (!method.getName().startsWith("native")) {
				continue;
			}
			Button b = new Button(this);
			b.setText(method.getName().substring(6));
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						method.invoke(HelloJni.this);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			});
			parent.addView(b);
		}
//        loadDeamon();
//        loadInject();
    }

    public void nativeRoot执行libdeamon_so() {
        loadDeamon(true);
	}

	private void loadInject() {
    	String path = "/data/data/"+getPackageName()+INJECT_EXE_NAME;
    	File file = new File(path);
        if (file.isFile()) {
			Log.e(TAG, path);
			try {
				Process process = Runtime.getRuntime().exec("su");
	            DataInputStream inputStream = new DataInputStream(process.getInputStream());
	            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
	            outputStream.writeBytes("./" + path + " &\n");
	            outputStream.writeBytes("exit\n");
	            outputStream.flush();
	            process.waitFor();
	            Log.e(TAG, "shell process end");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else {
			Log.e(TAG, "no so, path="+path);
		}
	}

	private void loadDeamon(boolean isRoot) {
    	String path = "/data/data/"+getPackageName()+DAEMON_EXE_NAME;
        File file = new File(path);
        if (file.isFile()) {
			try {
	            Log.d(TAG, path);
				/*Process process = Runtime.getRuntime().exec("sh");  //获得shell.
	            DataInputStream inputStream = new DataInputStream(process.getInputStream());
	            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
	            if (isRoot) {
	                outputStream.writeBytes("su\n");
                }
	            outputStream.writeBytes("./" + path + " &\n");   //保证在command在自己的数据目录里执行,才有权限写文件到当前目录
	            outputStream.writeBytes("exit\n");
	            outputStream.flush();
	            process.waitFor();
	            Log.e(TAG, "shell process end");*/
	            Process process;
			    if (!isRoot) {
			        process = Runtime.getRuntime().exec(path);
                }else {
                    process = Runtime.getRuntime().exec("su");
                    DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
                    outputStream.writeBytes("./" + path);
                }
			    BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(process.getInputStream())));
			    String line = null;
			    while (null != (line = br.readLine())) {
			        Log.d(TAG, line);
                }
                process.waitFor();
                Log.d(TAG, "shell process end");
			} catch (Exception e) {
				Log.e(TAG, "", e);
			}
		}else {
			Log.e(TAG, "no so, path="+path);
		}
	}

	public native String  nativeStringFromJNI();
	
    public native String  nativeMyFirstNDKfunction();
    
    public void nativeNotRoot直接执行libdeamon_so(){
        loadDeamon(false);
    }
    public native void printls(String path);

    public native String  nativeUnimplementedStringFromJNI();

    public native void nativeGetPid();
    
    public native int nativeGetArgMax();

    static {
        System.loadLibrary("test");
    }
}
