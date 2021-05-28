package gachon.mp.livre_bottom_navigation;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import gachon.mp.livre_bottom_navigation.ui.feed.FeedFragment;
import gachon.mp.livre_bottom_navigation.ui.writing.WriteInfo;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    ListActivity listActivity;
    List<WriteInfo> writeInfoList;
    FeedFragment feedFragment;

    //ListActivity용
    public CustomAdapter(ListActivity listActivity, List<WriteInfo> writeInfoList) {
        this.listActivity = listActivity;
        this.writeInfoList = writeInfoList;
    }

    //FeedFragment용
    public CustomAdapter(FeedFragment feedFragment, List<WriteInfo> writeInfoList) {
        this.feedFragment = feedFragment;
        this.writeInfoList = writeInfoList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate layout
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.model_layout, viewGroup,false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        //handle item clicks here

        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //this will be called when user click item

                Intent intent = new Intent(view.getContext(), ListDetailActivity.class);
                intent.putExtra("nickname",writeInfoList.get(position).getNickname());
                intent.putExtra("publisher",writeInfoList.get(position).getPublisher());
                intent.putExtra("title",writeInfoList.get(position).getTitle());
                intent.putExtra("contents",writeInfoList.get(position).getContents());
                intent.putExtra("publisher",writeInfoList.get(position).getPublisher());
                intent.putExtra("num_comment",writeInfoList.get(position).getNum_comment());
                intent.putExtra("num_heart",writeInfoList.get(position).getNum_heart());
                intent.putExtra("upload_time",getTime(writeInfoList.get(position).getUploadTime()));
                intent.putExtra("imagePath",writeInfoList.get(position).getImagePath());
                intent.putExtra("posts_id", writeInfoList.get(position).getPosts_id());
                System.out.println("adapter 에서의 posts id: "+ writeInfoList.get(position).getPosts_id());

//                listActivity.startActivity(intent);


                startToast(position + "번째 아이템 클릭");


            }

            @Override
            public void onItemLongClick(View view, int position) {
                //this will be called when user long click item

            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //bind views /set data
        viewHolder.vTitle.setText(writeInfoList.get(i).getTitle());
        viewHolder.vContents.setText(writeInfoList.get(i).getContents());
        viewHolder.vNickname.setText(writeInfoList.get(i).getNickname());
        viewHolder.vUploadTime.setText(getTime(writeInfoList.get(i).getUploadTime()));

    }

    @Override
    public int getItemCount() {
        return writeInfoList.size();
    }

    //timestamp를 getExtra로 불러올수없음. 전달하기전에 미리 형변환.
    static String getTime(Timestamp time) {
        Date date_createdAt = time.toDate();//Date형식으로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 HH시 mm분 ss초");
        String txt_createdAt = formatter.format(date_createdAt).toString();
        return txt_createdAt;
    }


    private void startToast(String msg){
        Toast.makeText(listActivity,msg, Toast.LENGTH_SHORT).show();
    }

}
