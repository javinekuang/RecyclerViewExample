package k.javine.recyclerviewexample;

/**
 * 此项目为练习新技术：
 * ------2016-03-12------------
 * 1.RecyclerView的使用  √
 * 2.butterKnife注入框架  √
 * 2.5 拍照Activity
 * 3.glide图片加载框架
 * 4.rxJava + rxAndroid + retrofit
 * 5.MVVM + DataBinding
 * 6.下拉加载控件
 * 7.网络加载CSDN博客内容 -- 客户端
 */

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRouter;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import k.javine.recyclerviewexample.camera.CameraActivity;
import k.javine.recyclerviewexample.receclyer.DividerGridItemDecoration;
import k.javine.recyclerviewexample.receclyer.DividerItemDecoration;
import k.javine.recyclerviewexample.receclyer.ItemEventCallback;

public class MainActivity extends Activity {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private MyRecyclerAdapter mAdapter;
    private List<String> mDatas;
    private View itemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        ButterKnife.bind(this);
        initView();
    }

    private void initData() {
        mDatas = new ArrayList<>();
        for (int i='A';i<'z';i++){
            mDatas.add(""+(char)i);
        }
    }

    private void initView() {
       // mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view); //change to butterKnife
        mAdapter = new MyRecyclerAdapter(this,mDatas);
        mRecyclerView.setAdapter(mAdapter);
        //setting to show ListView Horizontal
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //setting to show GridView
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        //mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        //setting to show Horizontal GridView
        //mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        //mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        //swipe item
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemEventCallback(mAdapter));
        touchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 处理点击事件 by butterKnife
     */
    @OnClick({R.id.btn_add , R.id.btn_remove})
    public void addItem(Button btn){
        switch (btn.getId()){
            case R.id.btn_add:
                //mAdapter.addItem("New Item");
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.btn_remove:
                mAdapter.removeItem(0);
                break;
        }
    }
}
