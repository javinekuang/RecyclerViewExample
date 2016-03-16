package k.javine.recyclerviewexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by KuangYu on 2016/3/12 0012.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private Context context;
    private List<String> mDatas;

    public MyRecyclerAdapter(Context context,List<String> mDatas){
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(mDatas.get(position));
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }

    public void addItem(String item){
        mDatas.add(1,item);
        notifyItemInserted(1);
    }

    public void removeItem(int position){
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void moveItem(int from,int to){
        Collections.swap(mDatas,from,to);
        notifyItemMoved(from,to);
    }
}
