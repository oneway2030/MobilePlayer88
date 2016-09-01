package com.itheima.mobileplayer88.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.itheima.mobileplayer88.R;
import com.itheima.mobileplayer88.adapter.MainPagerAdapter;
import com.itheima.mobileplayer88.ui.BaseActivity;
import com.itheima.mobileplayer88.ui.fragment.AudioListFragment;
import com.itheima.mobileplayer88.ui.fragment.VideoListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private String test="";
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private MainPagerAdapter mAdapter;
    private TextView tv_video;
    private TextView tv_audio;
    private View indicate;

    /**返回当前 Activity 使用的布局   */
    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    /**
     * 所有的 findViewById 操作必须在这个方法处理
     */
    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        tv_video = (TextView) findViewById(R.id.main_tv_video);
        tv_audio = (TextView) findViewById(R.id.main_tv_audio);
        indicate = findViewById(R.id.main_indicate);
    }

    /**
     * 注册监听器、适配器、广播接收者
     */
    @Override
    public void initListener() {
        tv_video.setOnClickListener(this);
        tv_audio.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new OnMainPageChangeListener());

        fragmentList = new ArrayList<>();
        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mAdapter);
    }

    /**
     * 获取数据，初始化界面
     */
    @Override
    public void initData() {
        fragmentList.add(new VideoListFragment());
        fragmentList.add(new AudioListFragment());
        mAdapter.notifyDataSetChanged();

        // 初始化界面时高亮视频tab
        updateTabs(0);

        // 初始化指示器的宽度
        int screenW = getWindowManager().getDefaultDisplay().getWidth();
        indicate.getLayoutParams().width = screenW / fragmentList.size();
        indicate.requestLayout();
//        indicate.invalidate();
    }

    /**
     * 在Base没有处理的点击事件，在这个方法统一处理
     */
    @Override
    public void processClick(View view) {
        switch (view.getId()){
            case R.id.main_tv_video:
                viewPager.setCurrentItem(0);
                break;
            case R.id.main_tv_audio:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    private class OnMainPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        // 当界面滚动时执行
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            logE("OnMainPageChangeListener.onPageScrolled,position="+position+";offset="+positionOffset+";pixs="+positionOffsetPixels);
//            指示器位置 = 起始位置 + 偏移位置
//            起始位置 = position * 指示器宽度
//            偏移位置 = 页面滑动的百分比 * 指示器宽度
            int offsetX = (int) (positionOffset * indicate.getWidth());
            int startX = position * indicate.getWidth();
            int translateX = startX + offsetX;
            ViewCompat.setTranslationX(indicate, translateX);
        }

        @Override
        // 当界面被选中时执行
        public void onPageSelected(int position) {
//            logE("OnMainPageChangeListener.onPageSelected,position="+position);
            // 选中的控件要高亮并放大，没选中的要缩小并变暗

            updateTabs(position);
        }

        @Override
        // 当滚动状态发生变化时执行
        public void onPageScrollStateChanged(int state) {
//            logE("OnMainPageChangeListener.onPageScrollStateChanged,state="+state);
        }
    }

    /** 高亮 position 的tab，其他tab都变暗 */
    private void updateTabs(int position) {
        updateTab(position, 0, tv_video);
        updateTab(position, 1, tv_audio);
    }

    /** 根据要比较的 tabPosition 是否是当前选中的 position，修改 tab 的颜色和大小*/
    private void updateTab(int position, int tabPosition, TextView tab) {
        if (position == tabPosition){
            // 视频被选中
            tab.setTextColor(getResources().getColor(R.color.green));
            ViewCompat.animate(tab).scaleX(1.1f).scaleY(1.1f).setDuration(200).start();
        }else{
            tab.setTextColor(getResources().getColor(R.color.half_white));
            ViewCompat.animate(tab).scaleX(0.9f).scaleY(0.9f).setDuration(200).start();
        }
    }
}
