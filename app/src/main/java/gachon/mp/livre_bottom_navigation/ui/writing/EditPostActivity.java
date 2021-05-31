package gachon.mp.livre_bottom_navigation.ui.writing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gachon.mp.livre_bottom_navigation.Protocol;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.mypage.PostActivity;
/*글 수정 액티비티
* 수정 되는 것: 제목, 사진, 내용, 업로드 시간 딱 4개.*/

public class EditPostActivity extends AppCompatActivity {
    private static final String TAG = "WritingActivity";
    final int GET_GALLERY_IMAGE=200;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    private String post_id;
    private String contents;
    private String title;
    private String imageURL;
    private ArrayList<String> pathList = new ArrayList<>();
    Uri imagePath;
    ImageView imageView;
    Button getBtnUpload;
    SimpleDateFormat formatter;
    String filename;
    StorageReference storageRef;
    EditText edit_title;
    EditText edit_contents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        /*문서 아이디 받아옴*/
        Intent intent = getIntent();
        post_id = intent.getStringExtra("posts_id");
        user = FirebaseAuth.getInstance().getCurrentUser();

        getBtnUpload=findViewById(R.id.btn_upload);// 등록 버튼
        edit_title = (EditText)findViewById(R.id.et_title);
        imageView = (ImageView)findViewById(R.id.iv_photo);
        edit_contents = (EditText)findViewById(R.id.et_contents);

    /*해당 문서 아이디에 해당하는 포스트를 서버에서 가져온다. 제목, 사진 경로, 내용만.*/
        db.collection("Posts")
                .whereEqualTo("posts_id", post_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                contents = document.get("contents").toString();
                                title = document.get("title").toString();
                                imageURL = document.get("imagePath").toString();
                                edit_title.setText(title);
                                edit_contents.setText(contents);
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                // 위의 저장소를 참조하는 파일명으로 지정
                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://mp-livre.appspot.com").child("images/"+ user.getUid() + "/" + imageURL);
                                System.out.println("***************storageReference : " + storageReference.toString());
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
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //이미지뷰 누르면 갤러리로 이동
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), GET_GALLERY_IMAGE);

            }
        });

        //등록하기 버튼 클릭
        getBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postUpdate();
                Toast.makeText(EditPostActivity.this, "수정 되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    // 결과 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagePath = data.getData();
            imageView.setImageURI(imagePath);

        } else{
            toastMsg("사진을 불러오는데 오류가 발생했습니다.");
        }
    }

    //포스트 업로드 전 체크
    private void postUpdate(){
        String title = edit_title.getText().toString();
        String contents = edit_contents.getText().toString();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firebaseFirestore.collection("Posts").document();

        if(title.length() > 0 ){
            // 이미지라면 storage에 업로드
            uploadFile();
            Timestamp upload_time = new Timestamp(new Date()); // 현재시간으로 타임스탬프 생성
            title = edit_title.getText().toString();
            contents = edit_contents.getText().toString();
            // firestore 에 업로드
            user = FirebaseAuth.getInstance().getCurrentUser();
            if(imagePath != null){
                String filePath = imagePath.toString();
                DocumentReference washingtonRef = db.collection("Posts").document(post_id);
                washingtonRef
                        .update("title", title)
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

                washingtonRef
                        .update("contents", contents)
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

                washingtonRef
                        .update("uploadTime", upload_time)
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

                washingtonRef
                        .update("imagePath", filename)
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
            else {

            }

        }else{
            toastMsg("제목 또는 내용을 입력해주세요.");
        }

    }
    private String getImagePath(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mp-livre.appspot.com");
        StorageReference pathReference = storageRef.child("images/" + user.getUid() + formatter);
        return pathReference.toString();
    }
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (imagePath != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename = user.getUid() +"_" + formatter.format(now) + ".png";
            storageRef = storage.getReferenceFromUrl("gs://mp-livre.appspot.com").child("images/"+ user.getUid() + "/" + filename);
            storageRef.putFile(imagePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                        }
                    });
        } else {
        }
    }

    private void toastMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}