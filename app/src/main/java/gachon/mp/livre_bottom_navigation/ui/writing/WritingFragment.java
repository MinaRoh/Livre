package gachon.mp.livre_bottom_navigation.ui.writing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;

import gachon.mp.livre_bottom_navigation.MainActivity;
import gachon.mp.livre_bottom_navigation.R;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class WritingFragment extends Fragment {
    MainActivity activity;

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    LinearLayout imageLayout;
    LinearLayout textLayout;
    ImageButton createEditText;
    ImageButton createImageView;

    @Override
    public void onStart() {
        super.onStart();

        imageLayout=(LinearLayout)this.getView().findViewById(R.id.imageLayout);
        textLayout=(LinearLayout)this.getView().findViewById(R.id.textLayout);

        createImageView=(ImageButton)this.getView().findViewById(R.id.createImageView);
        createImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createImageView();

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        createEditText=(ImageButton)this.getView().findViewById(R.id.createEditText);
        createEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditText();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_writing, container, false);
    }

    //이미지뷰 동적 생성
    ImageView imageViewNm;
    private void createImageView(){
        imageViewNm=new ImageView(activity.getApplicationContext());
        imageViewNm.setId(0);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        imageViewNm.setImageResource(R.drawable.default_image);
        imageViewNm.setLayoutParams(params);
        imageViewNm.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageLayout.addView(imageViewNm);
    }

    //갤러리에서 사진 불러오기
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    imageViewNm.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //텍스트뷰 동적 생성
    private void createEditText(){
        EditText editTextNm=new EditText(activity.getApplicationContext());
        editTextNm.setHint("내용");
        editTextNm.setTextSize(18);
        editTextNm.setTypeface(null);
        editTextNm.setId(0);

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        editTextNm.setLayoutParams(params);
        editTextNm.setBackgroundColor(Color.parseColor("#4197B843"));
        textLayout.addView(editTextNm);
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.upload_btn:
//                    profileUpdate();
                    break;
            }
        }
    };

    // 민하 작업중
//    private void profileUpdate(){
//        final String title = ((EditText) getActivity().findViewById(R.id.txttitle)).getText().toString();
//        final String contents = ((EditText) getActivity().findViewById(R.id.createEditText)).getText().toString();
//
//    }
//
//    private void uploader(WriteInfo writeInfo){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection("posts").add(writeInfo)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d(TAG, "DocumentSnapshot written with ID: "+documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//    }
}