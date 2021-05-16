package gachon.mp.livre_bottom_navigation.ui.writing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Date;
import gachon.mp.livre_bottom_navigation.Protocol;
import gachon.mp.livre_bottom_navigation.R;

import static android.content.ContentValues.TAG;

public class WritingActivity extends AppCompatActivity {
    final int GET_GALLERY_IMAGE=200;
    private FirebaseUser user;
    private String posts_id;
    private String nickname;
    ImageView getImageView;
    Button getBtnUpload;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_writing);
        user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();//사용자
        assert user != null;
        String user_id = user.getUid();//사용자 uid
        db.collection("Users")
                .whereEqualTo("uid", user_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                nickname = document.getData().get("nickname").toString();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });
        //이미지뷰 누르면 갤러리로 이동
        getImageView=this.findViewById(R.id.iv_photo);
        getImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        //등록하기 버튼 클릭
        getBtnUpload=this.findViewById(R.id.btn_upload);
        getBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileUpdate();
            }
        });
    }

    //갤러리에서 사진 불러오기
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            getImageView.setImageURI(selectedImageUri);
        }
    }

    //포스트 업로드 전 체크
    private void profileUpdate(){
        final String title = ((EditText) this.findViewById(R.id.et_title)).getText().toString();
        final String contents = ((EditText) this.findViewById(R.id.et_contents)).getText().toString();
        Timestamp upload_time = new Timestamp(new Date());
        String ISBN = "ISBNEXAMPLE"; // 예시 ISBN임. 실제로는 검색할 때 누른 책의 ISBN값을 전달 받아야함

        if(title.length() > 0 && contents.length() > 0){
            WriteInfo writeInfo = new WriteInfo(ISBN, title, nickname, contents, user.getUid(), upload_time, 0, 0);
            postUploader(writeInfo);
        }else{
            toastMsg("제목 또는 내용을 입력해주세요.");
        }

    }

    /*Firestore Posts에 포스트릏 업로드 하는 메소드*/
    private void postUploader(WriteInfo writeInfo){
        db.collection("Posts").add(writeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: "+documentReference.getId());
                        posts_id = documentReference.getId();
                        toastMsg("등록되었습니다!");
                        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                        intent.putExtra("posts_id", posts_id);//포스트 액티비티에 문서 id 전달
                        startActivityForResult(intent, Protocol.UPLOAD_POST);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        toastMsg("등록에 실패하였습니다.");
                    }
                });
    }

    private void toastMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}