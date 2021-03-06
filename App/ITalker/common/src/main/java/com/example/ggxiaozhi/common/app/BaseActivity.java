package com.example.ggxiaozhi.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.ggxiaozhi.common.widget.convention.PlaceHolderView;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 工程名 ： ITalker
 * 包名   ： com.example.ggxiaozhi.common.app
 * 作者名 ： 志先生_
 * 日期   ： 2017/12
 * 功能   ：
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected PlaceHolderView mPlaceHolderView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前调用初始化窗口
        initWindow();
        if (initArgs(getIntent().getExtras())) {
            //等到界面Id并设置到Activity中
            int layoutId = getContentLayoutId();
            setContentView(layoutId);
            initBefore();
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 在初始化布局之前
     */
    protected void initBefore() {

    }

    /**
     * 初始化参数
     *
     * @param bundle 传入的参数bundle
     * @return 返回参数正确true 错误false
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 初始化窗口
     */
    protected void initWindow() {

    }

    /**
     * 得到哦当前资源文件ID
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 当点击界面导航返回和底部导航返回时
     *
     * @return true 不向下传递 已经消费了此次点击事件
     */
    @Override
    public boolean onSupportNavigateUp() {
        //，Finish当前页面
        finish();

        return super.onSupportNavigateUp();
    }


    /**
     * 点击返回键的逻辑
     */
    @Override
    public void onBackPressed() {
        //得到当前Activity下的所有Fragment
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        // 判断是否为空
        if (fragments != null & fragments.size() > 0) {
            //判断是否为我们能够处理的Fragment
            for (android.support.v4.app.Fragment fragment : fragments) {
                if (fragment instanceof Fragment) {
                    //判断Fragment是否拦截了返回按钮
                    if (((Fragment) fragment).onBackPressed()) {
                        //如果有就直接return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }

    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.mPlaceHolderView = placeHolderView;
    }


}
