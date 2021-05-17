package gachon.mp.livre_bottom_navigation.ui.mypage;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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

import gachon.mp.livre_bottom_navigation.R;

public class MypageFragment extends Fragment {

    private MypageViewModel mypageViewModel;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String profileImg = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mypageViewModel =
                new ViewModelProvider(this).get(MypageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);
        final TextView textView = root.findViewById(R.id.text_mypages);
        mypageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
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
                            ImageView imageView = root.findViewById(R.id.iv_profile);
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
                                        Glide.with(getActivity())
                                                .load(task.getResult())
                                                .override(1024, 980)
                                                .into(imageView);
                                    } else {
                                        // URL을 가져오지 못하면 토스트 메세지
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });


        return root;
    }
}