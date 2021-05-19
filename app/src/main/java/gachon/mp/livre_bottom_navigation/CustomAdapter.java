package gachon.mp.livre_bottom_navigation;

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

import gachon.mp.livre_bottom_navigation.ui.writing.WriteInfo;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder> {
    ListActivity listActivity;
    List<WriteInfo> writeInfoList;


    public CustomAdapter(ListActivity listActivity, List<WriteInfo> writeInfoList) {
        this.listActivity = listActivity;
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

                //show data in toast on clicking
                String title = writeInfoList.get(position).getTitle();
                String descr = writeInfoList.get(position).getContents();
                Date createdAt = writeInfoList.get(position).getUploadTime();
                String nickname = writeInfoList.get(position).getNickname();


                startToast(title+"\n"+descr);
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
//        viewHolder.vNickname.setText(writeInfoList.get(i).getNickname());
//
//        viewHolder.vUploadTime.setText(getTime(writeInfoList.get(i).getCreatedAt()));





    }

    @Override
    public int getItemCount() {
        return writeInfoList.size();
    }

    static String getTime(Timestamp time) {
        Date date_createdAt = time.toDate();//Date형식으로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 HH시 mm분 ss초");
        String txt_createdAt = formatter.format(date_createdAt).toString();
        return formatter.format(new Date());
    }


    private void startToast(String msg){
        Toast.makeText(listActivity,msg, Toast.LENGTH_SHORT).show();
    }

}
