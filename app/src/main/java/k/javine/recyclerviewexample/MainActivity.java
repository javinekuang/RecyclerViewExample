package k.javine.recyclerviewexample;

/**
 * 此项目为练习新技术：
 * ------2016-03-12------------
 * 1.RecyclerView的使用
 * 2.butterKnife注入框架
 * 3.glide图片加载框架
 * 4.rxJava + rxAndroid + retrofit
 * 5.MVVM + DataBinding
 * 6.下拉加载控件
 * 7.网络加载CSDN博客内容 -- 客户端
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import k.javine.recyclerviewexample.receclyer.DividerItemDecoration;

public class MainActivity extends Activity {
    private RecyclerView mRecyclerView;

    private List<String> mDatas;
    private MyRecyclerAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i='A';i<'z';i++){
            mDatas.add(""+(char)i);
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new MyRecyclerAdapter(this,mDatas);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }
}
