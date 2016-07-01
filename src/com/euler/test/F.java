/*
 * 
 * Copyright (c) 2015, alipay.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.euler.test;

import java.util.List;

import com.zero.App;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.util.Log;

/**
 * @author sanping.li@alipay.com
 * winnerwinner
 */
public class F {
    protected void jnitest() {
//        Log.d("F", "fix error");
        jnitest1();
    }
    
    protected void jnitest1() {
//      Log.d("F", "fix error");
      Log.i("jnitest", "fix success");
      ActivityManager am = (ActivityManager) App.getContext()
              .getSystemService("activity");
      List<RunningTaskInfo> list = am.getRunningTasks(1);
      Log.i("jnitest", "list.size="+list.size());
      if (null != list && 0 < list.size()) {
          RunningTaskInfo info = list.get(0);
          Log.d("jnitest", "pkg=" + info.topActivity.getPackageName());
          Log.d("jnitest", "class=" + info.topActivity.getClassName());
      }
  }
}
