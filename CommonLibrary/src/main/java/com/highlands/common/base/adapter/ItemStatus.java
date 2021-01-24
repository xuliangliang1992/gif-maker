package com.highlands.common.base.adapter;

/**
 * item属性
 *
 * @author xuliangliang
 * @date 2019-10-25
 * copyright(c) Highlands
 */
public class ItemStatus {
    public static final int VIEW_TYPE_GROUP_ITEM = 0;
    public static final int VIEW_TYPE_SUB_ITEM = 1;

    /**
     * 类型
     */
    private int viewType;
    /**
     * 组position
     */
    private int groupPosition = 0;
    /**
     * 子position
     */
    private int childPosition = -1;

    private boolean expanded;

    public ItemStatus() {
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }

    public int getChildPosition() {
        return childPosition;
    }

    public void setChildPosition(int childPosition) {
        this.childPosition = childPosition;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
