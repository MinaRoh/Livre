package gachon.mp.livre_bottom_navigation.ui.feed;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.pushNoti.SendMessage;
import gachon.mp.livre_bottom_navigation.ui.more.ChangePersonalInfoActivity;
import gachon.mp.livre_bottom_navigation.ui.mypage.MypageFragment;
import gachon.mp.livre_bottom_navigation.ui.writing.CommentActivity;

/*다른 사람이 쓴 포스트를 보여주는 액티비티
* 수정, 삭제 불가*/
public class PostViewActivity extends AppCompatActivity {
    private static final String TAG = "PostViewActivity";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String posts_id;
    private FirebaseUser user;
    ImageView post_image;
    ImageView user_profile;
    int int_num_heart;
    int int_num_comment;
    String imagePath;
    Boolean heart_clicked = false;
    String nick;
    String publisher_uid;
    String profileImage;
    String txt_title;
    ArrayList userlist_heart;
    String sender_uid;
    Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);
        TextView title = (TextView)findViewById(R.id.title);
        TextView book_title = (TextView)findViewById(R.id.book_title);
        user_profile = (ImageView)findViewById(R.id.user_profile);
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

        DocumentReference docRef = db.collection("Posts").document(posts_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        txt_title = document.getData().get("title").toString();
                        String txt_book = document.getData().get("bookTitle").toString();
                        String txt_nickname = document.getData().get("nickname").toString();
                        publisher_uid = document.getData().get("publisher").toString();
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        sender_uid = user.getUid(); // 알림을 보내는 사람의 uid
                        upload_profile();
                        nick = txt_nickname;
                        Timestamp time = (Timestamp) document.getData().get("uploadTime");
                        String txt_contents = document.getData().get("contents").toString();
                        imagePath = document.getData().get("imagePath").toString();

                        userlist_heart = (ArrayList<String>) document.get("userlist_heart");


                        System.out.println("userlist_heart.size(): "+userlist_heart.size());
                        System.out.println("userlist_heart.isEmpty(): "+userlist_heart.isEmpty());
                        System.out.println("userlist_heart: "+userlist_heart);
                        if(userlist_heart.isEmpty()){// 하트 리스트가 비어있다면(디폴트)
                            heart.setImageResource(R.drawable.baseline_favorite_border_24);
                            heart_clicked=false;
                        }
                        else if(userlist_heart.contains(user.getUid())){//현재 유저가 해당 글에 이미 하트를 눌렀다면
                            heart.setImageResource(R.drawable.baseline_favorite_24); //하트 채워두기
                            heart_clicked=true;
                        }


//                        int_num_heart = Integer.parseInt(String.valueOf(document.getData().get("num_heart")));
                        int_num_comment = Integer.parseInt(String.valueOf(document.getData().get("num_comment")));
                        int_num_heart = userlist_heart.size();

                        title.setText(txt_title);
                        nickname.setText(txt_nickname);
                        upload_time.setText(getTime(time));
                        contents.setText(txt_contents);
                        num_heart.setText(String.valueOf(int_num_heart));
                        num_comment.setText(String.valueOf(int_num_comment));
                        book_title.setText(txt_book);
                        //이미지 불러오기 함수 실행
                        if(imagePath != ""){
                            handler.postDelayed(new Runnable(){
                                public void run(){
                                    //스플래시 -- 로딩중입니다.
                                    getImage();
                                }
                            }, 1000); // 1sec
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


        /*하트 눌렀을 때*/
        heart.setOnClickListener(new View.OnClickListener() {
            //문제점: 이건 한정적으로 방금 자기가 업로드한 글에만 적용되는 코드
            //자기가 눌렀던 걸 기억할 수 있으면 다른 사람 글이나 옛날에 올린 자기 글에도 적용가능.
            @Override
            public void onClick(View view) {
                    if (!heart_clicked) {
                        int_num_heart++;//하트 수 올리고
                        String category = "heart";
                        final String[] senderNickk = new String[1];
                        senderNickk[0] = getSenderNick(); //알림을 보내는 사람의 닉네임(현재 유저의 닉네임)
                        System.out.println("getSenderNick 에서 받아온 sender nick: " + senderNickk[0]);
                        String senderNick = senderNickk[0];
                        String receiverNick = publisher_uid;
                        SendMessage sendMessage = new SendMessage(senderNick, Collections.singletonList(publisher_uid), category, txt_title);
                        System.out.println("sender nick: " + senderNick);
                        System.out.println("postActivity에서 sendMessage 완료");
                        heart.setImageResource(R.drawable.baseline_favorite_24);

                        //posts에 좋아요 누른사람 arraylist 만들어서 uid 넣기
                        userlist_heart.add(sender_uid);
                        docRef.update("userlist_heart", FieldValue.arrayUnion(sender_uid)); //파이어스토어에 추가


                    } else {
                        int_num_heart--;//하트 수 내리고
                        userlist_heart.remove(sender_uid);
                        docRef.update("userlist_heart", FieldValue.arrayRemove(sender_uid)); //파이어스토어에서 삭제
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
       /*댓글 버튼 눌렀을 때*/
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostViewActivity.this, CommentActivity.class);
                intent.putExtra("post_title", txt_title); //글 제목 전달
                intent.putExtra("posts_id", posts_id);//코멘트 액티비티에 문서 id 전달
                intent.putExtra("publisher_uid", publisher_uid);//글 작성자 uid 전달
                startActivity(intent);

            }
        });
    }

    private String getSenderNick() {
        String nick = ((MainActivity)MainActivity.mainContext).nickname;
        return nick;
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
            StorageReference submitProfile = storage.getReferenceFromUrl("gs://mp-livre.appspot.com").child("images/"+ publisher_uid + "/" + imagePath);
            System.out.println("Post activity ************** submitProfile : " + submitProfile);
            
            submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    try {
                        Glide.with(getApplicationContext())
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
                }
            });
        }
    }
    static String getTime(Timestamp time) {
        Date date_createdAt = time.toDate();//Date형식으로 변경
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        String txt_createdAt = formatter.format(date_createdAt).toString();
        return txt_createdAt;
    }
        private void toastMsg(String msg){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
      public void upload_profile(){
          /*글쓴이 프로필 이미지 불러오기*/
          FirebaseFirestore db_user = FirebaseFirestore.getInstance();
          db_user.collection("Users")
                  .whereEqualTo("uid", publisher_uid) //포스트의 닉네임과 동일한 행을 Users에서 찾는다
                  .get()
                  .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<QuerySnapshot> task) {
                          if (task.isSuccessful()) {
                              for (QueryDocumentSnapshot document : task.getResult()) {
                                  profileImage = document.get("profileImage").toString();
                              }
                              //FirebaseStorage(사진) 인스턴스를 생성
                              FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                              // 위의 저장소를 참조하는 파일명으로 지정
                              StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://mp-livre.appspot.com/" + profileImage);
                              System.out.println("***************storageReference : " + storageReference.toString());
                              storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Uri> task) {
                                      if (task.isSuccessful()) {
                                          // Glide 이용하여 이미지뷰에 로딩
                                          Glide.with(getApplication())
                                                  .load(task.getResult())
                                                  .override(1024, 980)
                                                  .into(user_profile);
                                      } else {
                                          // URL을 가져오지 못하면 토스트 메세지
                                          Toast.makeText(getApplication(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                      }
                                  }
                              });
                          }
                      }
                  });
      }
}
