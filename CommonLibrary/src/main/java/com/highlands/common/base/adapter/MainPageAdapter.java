package com.highlands.common.base.adapter;

import com.highlands.common.base.fragment.BaseFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * ViewPageAdapter
 *
 * @author xuliangliang
 * @date 2020/11/3
 * copyright(c) Highlands
 */
public class MainPageAdapter extends FragmentStatePagerAdapter {

    private final List<BaseFragment> mFragmentList;

    public MainPageAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragmentList = fragments;
    }

    @NotNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList != null ? mFragmentList.size() : 0;
    }
}
