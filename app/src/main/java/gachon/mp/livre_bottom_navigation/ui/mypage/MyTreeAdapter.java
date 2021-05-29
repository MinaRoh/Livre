package gachon.mp.livre_bottom_navigation.ui.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.R;

public class MyTreeAdapter extends RecyclerView.Adapter<MyTreeAdapter.ViewHolder>{
    ArrayList<MyTree> items = new ArrayList<MyTree>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.mytree_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MyTree item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void setItem(MyTree item){
            //스토리지에 있는 사진 가져와서 imageView에 glide로 넣는 코드를 쓰슈.
        }
    }
    public void addItem(MyTree item){
        items.add(item);
    }
    public void setItems(ArrayList<MyTree> items){
        this.items = items;
    }

    public MyTree getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, MyTree item){
        items.set(position, item);
    }
}
