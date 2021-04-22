package com.justsafe.libview.rv;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Snap
 * </p>
 * Update by zt on 2021/03/26 .
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {
    public static final int DRAG_FLAGS_HORIZONTAL = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    public static final int DRAG_FLAGS_VERTICAL = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
    public static final int DRAG_FLAGS_ALL = DRAG_FLAGS_HORIZONTAL | DRAG_FLAGS_VERTICAL;

    private int mDragFlags;
    private ItemTouchActionCallback mActionCallback;

    public ItemTouchHelperCallback(ItemTouchActionCallback callback) {
        this(0, callback);
    }

    public ItemTouchHelperCallback(int dragFlags, ItemTouchActionCallback callback) {
        mDragFlags = dragFlags;
        mActionCallback = callback;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(mDragFlags, ItemTouchHelper.START);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //长按拖拽时需要改变条目背景色什么的属性 请复写此方法，按照如下格式
        //if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null)
        //    viewHolder.itemView.setAlpha(0.7f);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        mActionCallback.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
//        mActionCallback.onMoved(fromPos, toPos);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //在此恢复视图状态
//        viewHolder.itemView.setAlpha(Color.parseColor("#EBEBEB"));
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            int translateX = mActionCallback.getMenuWidth(viewHolder);
            View contentView = mActionCallback.getContentView(viewHolder);
            if (contentView == null)
                return;
            if (dX < -translateX) {
                dX = -translateX;
            }
            contentView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }

}
