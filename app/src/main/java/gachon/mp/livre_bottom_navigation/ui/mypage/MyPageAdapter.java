package gachon.mp.livre_bottom_navigation.ui.mypage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.writing.WriteInfo;

public class MyPageAdapter extends RecyclerView.Adapter<MyPageAdapter.ViewHolder>{
    ArrayList<MyPage> items = new ArrayList<MyPage>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.mypage_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MyPage item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        TextView textView2;
        TextView textView3;
        TextView textView4;
        TextView textView5;
        TextView textView6;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.nickname);
            textView2 = (TextView)itemView.findViewById(R.id.time);
            textView3 = (TextView)itemView.findViewById(R.id.title);
            textView4 = (TextView)itemView.findViewById(R.id.contents);
            textView5 = (TextView)itemView.findViewById(R.id.num_heart);
            textView6 = (TextView)itemView.findViewById(R.id.num_comment);

        }

        public void setItem(MyPage item){
            textView.setText(item.getNickname());//작성자 닉네임
            textView2.setText(item.getTime());//업로드 시간
            textView3.setText(item.getTitle());//글 제목
            textView4.setText(item.getContents());//글 내용
            textView5.setText(String.valueOf(item.getNum_heart()));//하트 개수
            textView6.setText(String.valueOf(item.getNum_comment()));//댓글 개수
        }
    }

    public void addItem(MyPage item){
        items.add(item);
    }
    public void setItems(ArrayList<MyPage> items){
        this.items = items;
    }

    public MyPage getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, MyPage item){
        items.set(position, item);
    }
}
