package k.javine.recyclerviewexample.receclyer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by KuangYu on 2016/3/15 0015.
 */
public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{ android.R.attr.listDivider };
    private Drawable mDivider;

    public DividerGridItemDecoration(Context context){
        TypedArray t = context.obtainStyledAttributes(ATTRS);
        mDivider = t.getDrawable(0);
        t.recycle();
    }

    private int getSpanCount(RecyclerView parent){
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            spanCount = ((GridLayoutManager)layoutManager).getSpanCount();
        }else if (layoutManager instanceof StaggeredGridLayoutManager){
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c,parent);
        drawVertical(c, parent);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i=0;i<childCount;i++){
            if (isLastRaw(parent,i,4,childCount))//最后一行不画左边边框
                continue;
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin +mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i=0; i<childCount; i++){
            if (isLastColumn(parent,i,4,childCount)) //最后一列不画右边边框
                continue;
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    private boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount){

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            if ((pos+1)%spanCount == 0){
                return true;
            }
        }else if (layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL){
                if ((pos+1)%spanCount == 0){
                    return true;
                }
            }else{
                childCount = childCount - childCount%spanCount; //calculate the position of last column items
                if (pos >= childCount)
                    return true;
            }
        }

        return false;
    }

    private boolean isLastRaw(RecyclerView parent,int pos,int spanCount,int childCount){

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            childCount = childCount - childCount%spanCount;
            if (pos >= childCount){
                return true;
            }
        }else if (layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL){
                childCount = childCount - childCount%spanCount;
                if (pos >= childCount){
                    return true;
                }
            }else{
                if ((pos+1)%spanCount == 0){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect,view,parent,state);
        /*int position = parent.getChildAdapterPosition(view);
        int spanCount = getSpanCount(parent);
        int childCount = parent.getChildCount();*/
        outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicWidth());
       /* if (isLastRaw(parent,position,spanCount,childCount)){
            //outRect.set(0,0,mDivider.getIntrinsicWidth(),0);
            Log.d("Javine","lastRawPosition = "+position+" "+childCount+" "+spanCount);
        }else if (isLastColumn(parent,position,spanCount,childCount)){
            //outRect.set(0,0,0,mDivider.getIntrinsicWidth());
            Log.d("Javine", "lastColumnPosition = " + position + " " + childCount + " " + spanCount);
        }else{
            //outRect.set(0,0,mDivider.getIntrinsicWidth(),mDivider.getIntrinsicWidth());
            Log.d("Javine", "elsePosition = " + position + " " + childCount + " " + spanCount);
        }*/
    }
}
