package com.zero;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zero.test.AMain;

public class MainActivity extends Activity implements OnItemClickListener, OnItemLongClickListener{

    private static final String TAG = "MainActivity";
    
    private static final boolean Life_Debug = false;
    
    ListView list;
    BaseAdapter adapter;
    List<IListData> datas;
    
//    ViewGroup bg_layout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Life_Debug) {
            Log.d(TAG, "onCreate", new Throwable());
        }
        setContentView(R.layout.activity_main);
        initData();
        initViews();
        super.onCreate(savedInstanceState);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepareMainLooper();
                Looper.loop();
            }
        }).start();*/
    }

    @Override
    protected void onStart() {
        if (Life_Debug) {
            Log.d(TAG, "onStart", new Throwable());
        }
        super.onStart();
    }

    @Override
    protected void onRestart() {
        if (Life_Debug) {
            Log.d(TAG, "onRestart", new Throwable());
        }
        super.onRestart();
    }

    @Override
    protected void onResume() {
        if (Life_Debug) {
            Log.d(TAG, "onResume", new Throwable());
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (Life_Debug) {
            Log.d(TAG, "onPause", new Throwable());
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (Life_Debug) {
            Log.d(TAG, "onStop", new Throwable());
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (Life_Debug) {
            Log.d(TAG, "onDestroy", new Throwable());
        }
        super.onDestroy();
    }


    private void initData() {
        datas = AMain.testList();
    }

    private void initViews() {
        list = (ListView) findViewById(android.R.id.list);
//        bg_layout = (ViewGroup) findViewById(R.id.bg_layout);
        adapter = new TestAdapter();
        initHeader();
        initFooter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);
    }

    private void initFooter() {
    }

    private void initHeader() {
    }

    class TestAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null || null == convertView.getTag()) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_list_item, list, false);
                holder = new Holder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.desc = (TextView) convertView.findViewById(R.id.desc);
            }else {
                holder = (Holder) convertView.getTag();
            }
            IListData data = (IListData) getItem(position);
            holder.title.setText(data.getTitle());
            holder.desc.setText(data.getDesc());
            return convertView;
        }
        
        class Holder{
            TextView title;
            TextView desc;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        datas.get(position).longClickGo(MainActivity.this);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        datas.get(position).clickGo(MainActivity.this);
    }
}
