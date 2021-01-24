package com.highlands.common.base.adapter;

/**
 * @author xuliangliang
 * @date 2019/9/4
 * copyright(c) Highlands
 */
public interface IListChangeCallback<T> {
    /**
     * Called whenever a change of unknown type has occurred, such as the entire list being
     * set to new values.
     *
     * @param sender The changing list.
     */
    void onChanged(T sender);

    /**
     * Called whenever one or more items in the list have changed.
     *
     * @param sender        The changing list.
     * @param positionStart The starting index that has changed.
     * @param itemCount     The number of items that have changed.
     */
    void onItemRangeChanged(T sender, int positionStart, int itemCount);

    /**
     * Called whenever items have been inserted into the list.
     *
     * @param sender        The changing list.
     * @param positionStart The insertion index
     * @param itemCount     The number of items that have been inserted
     */
    void onItemRangeInserted(T sender, int positionStart, int itemCount);

    /**
     * Called whenever items in the list have been moved.
     *
     * @param sender       The changing list.
     * @param fromPosition The position from which the items were moved
     * @param toPosition   The destination position of the items
     * @param itemCount    The number of items moved
     */
    void onItemRangeMoved(T sender, int fromPosition, int toPosition, int itemCount);

    /**
     * Called whenever items in the list have been deleted.
     *
     * @param sender        The changing list.
     * @param positionStart The starting index of the deleted items.
     * @param itemCount     The number of items removed.
     */
    void onItemRangeRemoved(T sender, int positionStart, int itemCount);
}
