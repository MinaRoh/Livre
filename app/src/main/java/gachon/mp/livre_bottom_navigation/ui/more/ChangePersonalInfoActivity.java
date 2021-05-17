package gachon.mp.livre_bottom_navigation.ui.more;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;

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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import gachon.mp.livre_bottom_navigation.PasswordResetActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import gachon.mp.livre_bottom_navigation.R;

public class ChangePersonalInfoActivity extends AppCompatActivity {
    private static final String TAG = "ChangePersonalInfo";
    private FirebaseAuth mAuth;
    private String nickname;
    private boolean nicknameCheck = false;
    private String document_id;
    private FirebaseUser user;
    private String profileImg = "";
    private final int GET_GALLERY_IMAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_more_1personal_info);
        ImageView user_profile = (ImageView)findViewById(R.id.user_profile);
        ImageButton btn_gallery = (ImageButton)findViewById(R.id.btn_gallery);
        EditText editText_nickname = (EditText)findViewById(R.id.editText_nickname);
        ImageButton btn_change_pw = (ImageButton)findViewById(R.id.btn_change_pw);
        ImageButton btn_save = (ImageButton)findViewById(R.id.btn_save);
        updateProfileImg(user_profile);
        /*유저 닉네임 파이어스토어에서 가져오기*/
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();//사용자
        assert user != null;
        String user_id = user.getUid();//사용자 uid
        //현재 사용자 uid와 동일한 Users document에 접근, 닉네임 값 얻어냄
        db.collection("Users")
                .whereEqualTo("uid", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                document_id = document.getId();
                                nickname = document.getData().get("nickname").toString();
                                editText_nickname.setText(nickname);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //프로필 동그랗게 하기
        user_profile.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//롤리팝 이상만 지원하나봄?
            user_profile.setClipToOutline(true);
        }

        //비밀번호 재설정 액티비티로 던지기
        btn_change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PasswordResetActivity.class);
                startActivity(intent);
            }
        });
        //저장하기(파이어스토어 업데이트) - 프로필 사진, 닉네임
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_nickname = editText_nickname.getText().toString();
                //닉네임 안바꾼 경우
                if(new_nickname.equals(nickname)){
                    //프로필 사진만 업데이트
                    Toast.makeText(ChangePersonalInfoActivity.this, "저장되었습니다",
                            Toast.LENGTH_SHORT).show();
                }
                //닉네임 바꾼 경우 - 중복검사 필요함
                else{
                    db.collection("Users")
                            .whereEqualTo("nickname", new_nickname)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int num=0;
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            num = num+1;
                                        }
                                        if(num==0) {
                                            nicknameCheck = true;
                                        }
                                        else{
                                            Toast.makeText(ChangePersonalInfoActivity.this, "중복된 닉네임입니다",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                    if(nicknameCheck){//중복검사 통과
                        //닉네임 업데이트
                        final DocumentReference sfDocRef = db.collection("Users").document(document_id);

                        db.runTransaction(new Transaction.Function<Void>() {
                            @Override
                            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                DocumentSnapshot snapshot = transaction.get(sfDocRef);

                                // Note: this could be done without a transaction
                                //       by updating the population using FieldValue.increment()
                                transaction.update(sfDocRef, "nickname", new_nickname);
                                // Success
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Transaction success!");
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Transaction failure.", e);
                                    }
                                });

                        //프로필 사진 업데이트

                        Toast.makeText(ChangePersonalInfoActivity.this, "저장되었습니다",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 새 프로필 이미지 선택
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK &&
        data != null && data.getData() != null){
            ImageView imageView = findViewById(R.id.user_profile);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            // 파일명 생성 "profile_email.png"
            String filename = "profile_img/profile_"+user.getEmail()+".jpg";
            Uri file = data.getData();
            StorageReference riverRef = storageRef.child(filename);
            UploadTask uploadTask = riverRef.putFile(file);
            // 새로운 프로필 이미지 저장
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { // db 업데이트
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference documentRef = db.collection("Users").document(user.getUid());
                    documentRef.update("profileImage", filename);
                    updateProfileImg(imageView); // Img button 업데이트
                }
            });

        }
    }
    public void updateProfileImg(ImageView imageView){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("Users")
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
                                        // Glide 이용하여 이미지뷰에 로딩
                                        Glide.with(getApplication())
                                                .load(task.getResult())
                                                .override(1024, 980)
                                                .into(imageView);
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
