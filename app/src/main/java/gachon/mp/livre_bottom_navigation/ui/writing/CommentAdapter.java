package gachon.mp.livre_bottom_navigation.ui.writing;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import gachon.mp.livre_bottom_navigation.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    ArrayList<CommentInfo> items = new ArrayList<CommentInfo>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.comment_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        CommentInfo item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;//프로필
        TextView textView;
        TextView textView2;
        TextView textView3;

        String profileImg;//프로필 이미지 저장소 URL
        String post_id;//포스트 아이디

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_profile); //프로필 사진
            textView = itemView.findViewById(R.id.nickname); // 닉네임
            textView2 = (TextView) itemView.findViewById(R.id.comment); //댓글 내용
            textView3 = (TextView) itemView.findViewById(R.id.time);// 댓글 단 시간

        }

        public void setItem(CommentInfo item) {
            post_id = item.getPost_id();
            profileImg = item.getProfile_image();
            textView.setText(item.getNickname());// 닉네임
            textView2.setText(item.getComment());//댓글 내용
            textView3.setText(item.getTime());// 댓글 단 시간

            /*유저 프로필 이미지 불러오기*/
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setClipToOutline(true);
            }
            //FirebaseStorage 인스턴스를 생성
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            // 위의 저장소를 참조하는 파일명으로 지정
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://mp-livre.appspot.com/" + profileImg);
            //StorageReference에서 파일 다운로드 URL 가져옴
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Glide.with(itemView)
                                .load(task.getResult())
                                .override(1024, 980)
                                .into(imageView);
                    }
                }
            });

        }

    }

    public void addItem(CommentInfo item) {
        items.add(item);
    }

    public void setItems(ArrayList<CommentInfo> items) {
        this.items = items;
    }

    public CommentInfo getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, CommentInfo item) {
        items.set(position, item);
    }
}
