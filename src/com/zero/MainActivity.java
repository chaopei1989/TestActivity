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
    
    ListView list;
    BaseAdapter adapter;
    List<IListData> datas;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppEnv.DEBUG) {
            Log.d(TAG, "onCreate");
        }
        setContentView(R.layout.activity_main);
        initData();
        initViews();
        super.onCreate(savedInstanceState);
        /*IStopPackageService s = (IStopPackageService) ServiceManager.getService(StopPackageService.SERVICE_ID);
        try {
            //s有可能为空????????????
            s.killSysNoWait();
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/
    }

    private void initData() {
        datas = AMain.testList();
    }

    private void initViews() {
        list = (ListView) findViewById(android.R.id.list);
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
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_list_item, null);
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
