package com.xll.gif.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.highlands.common.base.BaseInterstitialAdActivity;
import com.highlands.common.base.adapter.MainPageAdapter;
import com.highlands.common.base.fragment.BaseFragment;
import com.xll.gif.R;
import com.xll.gif.databinding.ActivityMainBinding;
import com.xll.gif.databinding.MainTabItemBinding;
import com.xll.gif.fragment.GifCreateFragment;
import com.xll.gif.fragment.GifEditFragment;
import com.xll.gif.fragment.GifListFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;

public class MainActivity extends BaseInterstitialAdActivity {

    private ActivityMainBinding binding;

    private String[] titles;
    private int[] imgRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initData();

    }

    protected void initData() {
        List<BaseFragment> fragments = new ArrayList<>();
        titles = new String[]{"CREATE", "GIPHY", "EDIT GIF"};
        imgRes = new int[]{
                R.drawable.selector_main_tab_train,
                R.drawable.selector_main_tab_information,
                R.drawable.selector_main_tab_ask
        };

        BaseFragment homeFragment = GifListFragment.newInstance();
        BaseFragment informationFragment = GifCreateFragment.newInstance();
        BaseFragment trainFragment = GifEditFragment.newInstance();

        fragments.add(informationFragment);
        fragments.add(homeFragment);
        fragments.add(trainFragment);

        MainPageAdapter mAdapter = new MainPageAdapter(getSupportFragmentManager(), fragments);
        binding.vpMain.setAdapter(mAdapter);
        binding.vpMain.setOffscreenPageLimit(2);
        binding.tlMain.setupWithViewPager(binding.vpMain, true);
        for (int i = 0; i < titles.length; i++) {
            binding.tlMain.getTabAt(i).setCustomView(getTabView(i));
        }
    }

    private View getTabView(int position) {

        MainTabItemBinding tabItemBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.main_tab_item, binding.tlMain, false);
        tabItemBinding.imgTitle.setImageResource(imgRes[position]);
        tabItemBinding.txtTitle.setText(titles[position]);
        return tabItemBinding.getRoot();
    }
}