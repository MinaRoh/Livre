package gachon.mp.livre_bottom_navigation.ui.writing;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gachon.mp.livre_bottom_navigation.Protocol;
import gachon.mp.livre_bottom_navigation.R;



public class WritingActivity extends AppCompatActivity {
    private static final String TAG = "WritingActivity";
    final int GET_GALLERY_IMAGE=200;
    private FirebaseUser user;
    private String posts_id;
    private String nickname = "샘플닉네임";
    Uri imagePath;
    ImageView getImageView;
    Button getBtnUpload;
    private String contents;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    SimpleDateFormat formatter;
    String filename;
    StorageReference storageRef;
    public static Context mContext;    //외부참조용 context 선언


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);
        mContext = this;        //외부참조용 context 초기화 (onCreate 에서 해야함)
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = mAuth.getCurrentUser();//사용자

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
        getImageView=findViewById(R.id.iv_photo);
        getImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), GET_GALLERY_IMAGE);

            }
        });

        //등록하기 버튼 클릭
        getBtnUpload=findViewById(R.id.btn_upload);
        getBtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postUpdate();
            }
        });
    }


    // 결과 처리
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagePath = data.getData();
            Log.d(TAG, "**************이미지 uri:"+String.valueOf(imagePath));
            // 이미지뷰에 띄우기
            getImageView.setImageURI(imagePath);

        } else{
            toastMsg("사진을 불러오는데 오류가 발생했습니다.");
        }
    }

    //포스트 업로드 전 체크
    private void postUpdate(){
        final String title = ((EditText) this.findViewById(R.id.et_title)).getText().toString();
        final String contents = ((EditText) this.findViewById(R.id.et_contents)).getText().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = firebaseFirestore.collection("Posts").document();

        Date upload_time = new Date();
        String ISBN = "ISBNEXAMPLE"; // 예시 ISBN임. 실제로는 검색할 때 누른 책의 ISBN값을 전달 받아야함

        if(title.length() > 0 ){
//            // While the file names are the same, the references point to different files
//            imageRef.getName().equals(imageRef.getName());    // true


            // 이미지라면 storage에 업로드
            uploadFile();
            // 이미지 주소를 추가해서 upload하기(아직 안함 이미지 주소 들어갈곳도 없을걸?)
            // uploadFile()의 리턴으로 이미지 주소를 받아서 postUploader에 넣으면 되려나?
            // 일단 위에 text 넣을때 imagePath도 추가함. Uri 형태. 저렇게 해서 잘 되면 무조건 사진도 최소 하나 넣게 하면 되것지.


            // firestore 에 업로드
            user = FirebaseAuth.getInstance().getCurrentUser();

            if(imagePath != null){
                String filePath = imagePath.toString(); //Uri to String
                System.out.println("************************filePath: " + filePath);
                WriteInfo writeInfo = new WriteInfo(ISBN, title, nickname, contents, getImagePath(), user.getUid(), upload_time, 0, 0);
                postUploader(writeInfo);
            }
            else {
                WriteInfo writeInfo = new WriteInfo(ISBN, title, nickname, contents, "", user.getUid(), upload_time, 0, 0);
                postUploader(writeInfo);
            }

        }else{
            toastMsg("제목 또는 내용을 입력해주세요.");
        }

    }

    //storage에 올린 사진 주소를 posts에 imagePath에 넣자
    private String getImagePath(){


        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //storage 주소와 폴더 파일명을 지정해 준다.
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mp-livre.appspot.com");

        //Storage 내부의 images 폴더 안의 image.jpg 파일명을 가리키는 참조 생성
        StorageReference pathReference = storageRef.child("images/" + user.getUid() + formatter);
        System.out.println("**************스토리지 에서 가져온 주소: " + pathReference);
        return pathReference.toString();
    }

    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (imagePath != null) {
//            //업로드 진행 Dialog 보이기
//            final ProgressDialog progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("업로드중...");
//            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename = user.getUid() +"_" + formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            storageRef = storage.getReferenceFromUrl("gs://mp-livre.appspot.com").child("images/"+ user.getUid() + "/" + filename);
            //올라가거라...
            storageRef.putFile(imagePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
//                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            //Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    /*Firestore Posts에 포스트를 업로드 하는 메소드*/
    private void postUploader(WriteInfo writeInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
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