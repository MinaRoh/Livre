package gachon.mp.livre_bottom_navigation.ui.writing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import gachon.mp.livre_bottom_navigation.R;

/*글쓰기 완료한 포스트를 보여주는 액티비티
* 하트수, 댓글, 메뉴(수정,삭제) 기능이 연결 돼 있음*/
public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    private String posts_id;
    private FirebaseUser user;
    ImageView post_image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        TextView title = (TextView)findViewById(R.id.title);
        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        TextView nickname = (TextView)findViewById(R.id.nickname);
        TextView upload_time = (TextView)findViewById(R.id.upload_time);
        ImageButton user_menu = (ImageButton)findViewById(R.id.user_menu);
//        ImageView post_image = (ImageView)findViewById(R.id.post_image);
        TextView contents = (TextView)findViewById(R.id.contents);
        ImageButton heart = (ImageButton)findViewById(R.id.heart);
        TextView num_heart = (TextView)findViewById(R.id.num_heart);
        ImageButton comment = (ImageButton)findViewById(R.id.comment);
        TextView num_comment = (TextView)findViewById(R.id.num_comment);
        //문서의 uid를 전달 받아서 해당 문서를 보여준다.
        Intent intent = getIntent();
        posts_id = intent.getStringExtra("posts_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Posts").document(posts_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String txt_title = document.getData().get("title").toString();
                        String txt_nickname = document.getData().get("nickname").toString();
                        String txt_contents = document.getData().get("contents").toString();
                        int int_num_heart = Integer.parseInt(String.valueOf(document.getData().get("num_heart")));
                        int int_num_comment = Integer.parseInt(String.valueOf(document.getData().get("num_comment")));
                        title.setText(txt_title);
                        nickname.setText(txt_nickname);
                        upload_time.setText(getTime());
                        contents.setText(txt_contents);
                        num_heart.setText(String.valueOf(int_num_heart));
                        num_comment.setText(String.valueOf(int_num_comment));

                        //이미지 불러오기 함수 실행
                        getImage();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }


        });


    }

    public void getImage() {


        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //storage 주소와 폴더 파일명을 지정해 준다.
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mp-livre.appspot.com");
        user = FirebaseAuth.getInstance().getCurrentUser();


        String filename = ((WritingActivity) WritingActivity.mContext).filename;
        System.out.println("Post activity ************** filename : " + filename);

        //Storage 내부의 images 폴더 안의 image.jpg 파일명을 가리키는 참조 생성
        StorageReference pathReference = storageRef.child("images");
        System.out.println("Post activity **************스토리지 어느곳 참조?: " + pathReference);

        if(pathReference == null){
            toastMsg("저장소에 사진이 없습니다.");
        }else{
            StorageReference submitProfile = storage.getReferenceFromUrl("gs://mp-livre.appspot.com").child("images/" + user.getUid() + "/" + filename);
            System.out.println("Post activity ************** submitProfile : " + submitProfile);
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(PostActivity.this)
                            .load(uri)
                            .into(post_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error getting image", e);
                    toastMsg("이미지 로딩에 실패하였습니다.");
                }
            });
        }


//        // Reference to an image file in Cloud Storage
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//
//        // ImageView in your Activity
//        ImageView imageView = findViewById(R.id.post_image);
//
//        // Download directly from StorageReference using Glide
//        // (See MyAppGlideModule for Loader registration)
//        Glide.with(this /* context */)
//                .load(storageRef)
//                .into(imageView);

    }
    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return f.format(new Date());
    }
        private void toastMsg(String msg){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
}
