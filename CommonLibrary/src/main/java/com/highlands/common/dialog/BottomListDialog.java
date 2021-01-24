package com.highlands.common.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.highlands.common.R;
import com.highlands.common.base.adapter.BaseSingleBindingAdapter;
import com.highlands.common.databinding.DialogBottomListBinding;
import com.highlands.common.databinding.ItemBottomListBinding;
import com.highlands.common.dialog.base.BottomBaseDialog;
import com.highlands.common.view.decoration.HorizontalDividerItemDecoration;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;


/**
 * 底部有取消按钮的弹出框 从底部弹出
 *
 * @author xll
 * @date 2018/1/1
 */
public class BottomListDialog extends BottomBaseDialog {
    private DialogBottomListBinding mBinding;
    private ObservableArrayList<String> titles;
    private ItemClickListener mClickListener;

    public BottomListDialog(Context context, ObservableArrayList<String> titles, ItemClickListener itemClickListener) {
        super(context);
        this.titles = titles;
        this.mClickListener = itemClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView() {
        View view = View.inflate(context, R.layout.dialog_bottom_list, null);
        mBinding = DataBindingUtil.bind(view);
        mBinding.rvList.setLayoutManager(new LinearLayoutManager(context));
        HorizontalDividerItemDecoration decoration = new HorizontalDividerItemDecoration.Builder(context)
                .color(ContextCompat.getColor(context, R.color.base_bg_e6))
                .size(1).build();
        mBinding.rvList.addItemDecoration(decoration);
        ItemAdapter mItemAdapter = new ItemAdapter();
        mBinding.rvList.setAdapter(mItemAdapter);
        mItemAdapter.refresh(titles);
        return view;
    }

    @Override
    public boolean setUiBeforeShow() {
        return true;
    }

    class ItemAdapter extends BaseSingleBindingAdapter<String, ItemBottomListBinding> {

        @Override
        protected int getItemLayout() {
            return R.layout.item_bottom_list;
        }

        @Override
        protected void onBindItem(ItemBottomListBinding binding, final int position) {
            binding.setString(mItems.get(position));
            binding.executePendingBindings();
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mClickListener != null) {
                        mClickListener.onItemClickListener(v, position);
                    }
                }
            });
        }
    }
}
