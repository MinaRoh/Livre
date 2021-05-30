package gachon.mp.livre_bottom_navigation.ui.mypage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.Protocol;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.writing.PostActivity;
import gachon.mp.livre_bottom_navigation.ui.writing.WriteInfo;
import gachon.mp.livre_bottom_navigation.ui.writing.WritingActivity;

public class MyPageAdapter extends RecyclerView.Adapter<MyPageAdapter.ViewHolder>{
    ArrayList<MyPage> items = new ArrayList<MyPage>();
    private static final String TAG = "MyPageAdapter";
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;//프로필
        TextView textView;
        TextView textView2;
        ImageView imageView2;//포스트 이미지
        TextView textView3;
        TextView textView4;
        TextView textView5;
        TextView textView6;
        TextView textView7;
        FirebaseUser user;
        String profileImg;//프로필 이미지 저장소 URL
        String post_id;//포스트 아이디
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_profile);
            textView = itemView.findViewById(R.id.nickname);
            textView2 = (TextView)itemView.findViewById(R.id.time);
            imageView2 = (ImageView)itemView.findViewById(R.id.content_image);
            textView3 = (TextView)itemView.findViewById(R.id.title);
            textView4 = (TextView)itemView.findViewById(R.id.contents);
            textView5 = (TextView)itemView.findViewById(R.id.num_heart);
            textView6 = (TextView)itemView.findViewById(R.id.num_comment);
            textView7 = (TextView)itemView.findViewById(R.id.book_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent;
                    intent = new Intent(context, PostViewActivity.class);
                    intent.putExtra("posts_id", post_id);//포스트 액티비티에 문서 id 전달
                    Log.d(TAG, "posts_id: " + post_id);
                    context.startActivity(intent);
                }
            });
        }

        public void setItem(MyPage item){
            post_id = item.getPost_id();

            textView.setText(item.getNickname());//작성자 닉네임
            textView2.setText(item.getTime());//업로드 시간
            textView3.setText(item.getTitle());//글 제목
            textView4.setText(item.getContents());//글 내용
            textView5.setText(String.valueOf(item.getNum_heart()));//하트 개수
            textView6.setText(String.valueOf(item.getNum_comment()));//댓글 개수
            textView7.setText(item.getBook_title());
            /*유저 프로필 이미지 불러오기*/
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setClipToOutline(true);
            }
            FirebaseFirestore db_user = FirebaseFirestore.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            db_user.collection("Users")
                    .whereEqualTo("uid", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    profileImg = document.get("profileImage").toString();
                                }
                                //FirebaseStorage 인스턴스를 생성
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                // 위의 저장소를 참조하는 파일명으로 지정
                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://mp-livre.appspot.com/"+profileImg);
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
                    });

            /*포스트 이미지 불러오기*/
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://mp-livre.appspot.com");
            String filename = item.getPostImage();
            StorageReference pathReference = storageRef.child("images");
            if(filename!=null && pathReference != null){
                StorageReference submitProfile = storage.getReferenceFromUrl("gs://mp-livre.appspot.com").child("images/"+ user.getUid() + "/" + filename);
                submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if(imageView2!=null){
                            Glide.with(itemView)
                                    .load(uri)
                                    .override(1024, 980)
                                    .into(imageView2);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting image", e);
                    }
                });
            }
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
