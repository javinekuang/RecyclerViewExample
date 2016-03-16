package k.javine.recyclerviewexample.receclyer;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import k.javine.recyclerviewexample.MyRecyclerAdapter;

/**
 * Created by KuangYu on 2016/3/15 0015.
 */
public class ItemEventCallback extends ItemTouchHelper.Callback {
    private MyRecyclerAdapter adapter;
    private View itemView;

    public ItemEventCallback(MyRecyclerAdapter adapter){
        this.adapter = adapter;
        itemView = null;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        adapter.moveItem(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        Log.d("Javine", "onMove");
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.removeItem(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
            itemView = viewHolder.itemView;
            itemView.setSelected(true); //让item处于选中状态，提示用户可以移动

        }else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE){
            if (itemView != null){
                itemView.setSelected(false); //处理结束，恢复item初始状态
                itemView = null;
            }
        }
    }
}
