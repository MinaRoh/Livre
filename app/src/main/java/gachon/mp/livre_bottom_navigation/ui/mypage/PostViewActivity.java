package gachon.mp.livre_bottom_navigation.ui.mypage;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

import gachon.mp.livre_bottom_navigation.R;

/*미리보기 리스트에서 누른 포스트를 보여주는 액티비티
* 글쓴이는 수정, 삭제가 가능해야 함*/
public class PostViewActivity extends AppCompatActivity {
    private static final String TAG = "PostViewActivity";
    private String posts_id;
    private FirebaseUser user;
    private String publisher_email = "";
    ImageView post_image;
    int int_num_heart;
    int int_num_comment;
    String imagePath;
    Boolean heart_clicked = false;
    String nick;
    Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        TextView title = (TextView)findViewById(R.id.title);
        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        TextView nickname = (TextView)findViewById(R.id.nickname);
        TextView upload_time = (TextView)findViewById(R.id.upload_time);
        ImageButton user_menu = (ImageButton)findViewById(R.id.user_menu);
        TextView contents = (TextView)findViewById(R.id.contents);
        ImageButton heart = (ImageButton)findViewById(R.id.heart);
        TextView num_heart = (TextView)findViewById(R.id.num_heart);
        ImageButton comment = (ImageButton)findViewById(R.id.comment);
        TextView num_comment = (TextView)findViewById(R.id.num_comment);
        post_image = (ImageView)findViewById(R.id.post_image);
        /*문서의 uid를 전달 받아서 해당 문서를 보여준다.*/
        Intent intent = getIntent();
        posts_id = intent.getStringExtra("posts_id");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //그 전에 posts_id 필드 추가
        DocumentReference docRef = db.collection("Posts").document(posts_id);

        // 보여주기
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String txt_title = document.getData().get("title").toString();
                        String txt_nickname = document.getData().get("nickname").toString();
                        nick = txt_nickname;
                        Timestamp time = (Timestamp) document.getData().get("uploadTime");
                        String txt_contents = document.getData().get("contents").toString();
                        imagePath = document.getData().get("imagePath").toString();
                        int_num_heart = Integer.parseInt(String.valueOf(document.getData().get("num_heart")));
                        int_num_comment = Integer.parseInt(String.valueOf(document.getData().get("num_comment")));
                        title.setText(txt_title);
                        nickname.setText(txt_nickname);
                        upload_time.setText(getTime(time));
                        contents.setText(txt_contents);
                        num_heart.setText(String.valueOf(int_num_heart));
                        num_comment.setText(String.valueOf(int_num_comment));
                        //이미지 불러오기 함수 실행
                        if(imagePath != ""){
                            handler.postDelayed(new Runnable(){
                                public void run(){
                                    //스플래시 -- 로딩중입니다.
                                    getImage();
                                }
                            }, 3000); // 1sec
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });

        //프로필 동그랗게 하기
        user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            user_profile.setClipToOutline(true);
        }

        /*글쓴이 프로필 이미지 불러오기*/
        FirebaseFirestore db_user = FirebaseFirestore.getInstance();
        db_user.collection("Users")
                .whereEqualTo("nickname", nick) //포스트의 닉네임과 동일한 행을 Users에서 찾는다
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                publisher_email = document.get("email").toString(); //글쓴이의 이메일 주소 얻어옴
                                Log.d(TAG, "user_email: " + publisher_email);
                            }
                            //FirebaseStorage(사진) 인스턴스를 생성
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            // 위의 저장소를 참조하는 파일명으로 지정
                            StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://mp-livre.appspot.com/").child("profile_img/profile_" + publisher_email + ".jpg");
                            System.out.println("***************storageReference : " + storageReference.toString());
                            //StorageReference에서 파일 다운로드 URL 가져옴
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(PostViewActivity.this)
                                            .load(task.getResult())
                                            .override(1024, 980)
                                            .into(user_profile);
                                }
                            });
                        }
                    }
                });
        /*하트 눌렀을 때*/
        heart.setOnClickListener(new View.OnClickListener() {
            //문제점: 이건 한정적으로 방금 자기가 업로드한 글에만 적용되는 코드
            //자기가 눌렀던 걸 기억할 수 있으면 다른 사람 글이나 옛날에 올린 자기 글에도 적용가능.
            @Override
            public void onClick(View view) {
                heart_clicked = !(heart_clicked);
                if (heart_clicked) {
                    int_num_heart++;//하트 수 올리고
                    heart.setImageResource(R.drawable.baseline_favorite_24);
                } else {
                    int_num_heart--;//하트 수 내리고
                    heart.setImageResource(R.drawable.baseline_favorite_border_24);
                }
                num_heart.setText(String.valueOf(int_num_heart));//포스트에 반영
                docRef
                        .update("num_heart", int_num_heart)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });

    }
/*포스트의 이미지를 불러오는 메소드
* 이미지를 올리지 않은 경우는 아무 동작하지 않음*/
    public void getImage() {
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //storage 주소와 폴더 파일명을 지정해 준다.
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mp-livre.appspot.com");
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Storage 내부의 images 폴더 안의 image.jpg 파일명을 가리키는 참조 생성
        StorageReference pathReference = storageRef.child("images");
        System.out.println("Post activity **************스토리지 어느곳 참조?: " + pathReference);

        if(pathReference == null){
            toastMsg("저장소에 사진이 없습니다.");
        }else{
            //Storage 내부의 images 폴더 안의 image.jpg 파일명을 가리키는 참조 생성
            StorageReference submitProfile = storage.getReferenceFromUrl("gs://mp-livre.appspot.com").child("images/"+ user.getUid().toString() + "/" + imagePath);
            System.out.println("Post activity ************** submitProfile : " + submitProfile);
            
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        Glide.with(PostViewActivity.this)
                                .load(uri)
                                .into(post_image);
                    }
                    catch (IllegalArgumentException e){
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error getting image", e);
                    toastMsg("이미지 로딩에 실패하였습니다.");
                }
            });
        }
    }
    static String getTime(Timestamp time) {
        Date date_createdAt = time.toDate();//Date형식으로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
        String txt_createdAt = formatter.format(date_createdAt).toString();
        return txt_createdAt;
    }
        private void toastMsg(String msg){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
}
