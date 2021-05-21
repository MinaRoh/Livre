package gachon.mp.livre_bottom_navigation.ui.feed;
// feed main (posts)
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import gachon.mp.livre_bottom_navigation.MainAdapter;
import gachon.mp.livre_bottom_navigation.CustomAdapter;
import gachon.mp.livre_bottom_navigation.PostInfo;
import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.writing.WriteInfo;

public class FeedFragment extends Fragment {
    final String TAG = "FeedPostFragment";
    //firestore instance
    FirebaseFirestore db;
    CustomAdapter adapter;


    private FirebaseAuth mAuth;
    List<WriteInfo> writeInfoList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recyclerview
    RecyclerView.LayoutManager layoutManager;


    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    Button btn;
    // 자동완성
    // 데이터를 넣은 리스트 변수
    private List<String> autotextviewlist;
    ArrayList<String> participants;
    String posts_id;
    int int_numOfRecruits, int_curRecruits;
    TextView peopleNum;
    TextView status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_feed, container, false);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 자동완성
        // 리스트를 생성한다
        autotextviewlist = new ArrayList<String>();

        // 리스트에 검색될 데이터(단어)를 추가한다
        settingList();

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextView);

        // edtsearch 에 adapter 를 연결한다
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, autotextviewlist));

        final EditText edtsearch = getView().findViewById(R.id.autoCompleteTextView);
        edtsearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String query = edtsearch.getText().toString();
                Intent intent = new Intent(getActivity(), FeedSearchActivity.class);
                intent.putExtra("query", query);

                startActivity(intent);
                return false;
            }
        });



    }

    // 검색에 사용될 데이터를 리스트에 추가한다.
    public void settingList() {
        autotextviewlist.add("언어의 온도");
        autotextviewlist.add("자존감 수업");
        autotextviewlist.add("나는 나로 살기로 했다");
        autotextviewlist.add("빨간 머리 앤");
        autotextviewlist.add("Hello 마더구스 세트");
        autotextviewlist.add("Hello Coding 프로그래밍");
        autotextviewlist.add("Hello 부동산 Bravo! 멋진 인생");
        autotextviewlist.add("Hello! 처음 만나는 전기기기");
        autotextviewlist.add("우리는 안녕");
        autotextviewlist.add("안녕, 나의 빨강머리 앤");
        autotextviewlist.add("안녕, 앤");
        autotextviewlist.add("안녕, 나는 익명이고 너를 조아해");
        autotextviewlist.add("안녕, 우주");
        autotextviewlist.add("안녕, 소중한 사람");
        autotextviewlist.add("책 먹는 여우의 겨울 이야기");
        autotextviewlist.add("매우 예민한 사람들을 위한 책");
        autotextviewlist.add("사소해서 물어보지 못했지만 궁금했던 이야기");
        autotextviewlist.add("주린이도 술술 읽는 주식책");
        autotextviewlist.add("유저를 끌어당기는 모바일 게임 기획");
        autotextviewlist.add("모바일 리얼리티");
        autotextviewlist.add("모바일 게임 기획의 모든 것");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init firestore
        db = FirebaseFirestore.getInstance();


        //initialize views
        mRecyclerView = getActivity().findViewById(R.id.recycler_view);
        //set recycler view properties
        mRecyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //show data in recyclerVeiw
        //showData();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
//                            myStartActivity(MemberInitActivity.class);
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

//        recyclerView = getActivity().findViewById(R.id.post_view);
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

//    public void onResume(){
//        super.onResume();
//
//        if (firebaseUser != null) {
//            CollectionReference collectionReference = firebaseFirestore.collection("Posts");
//            collectionReference.orderBy("uploadTime", Query.Direction.DESCENDING).get() // uploadTime 내림차순으로 정렬
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                ArrayList<PostInfo> postList = new ArrayList<>();
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                    postList.add(new PostInfo(
//                                            document.getData().get("title").toString(),
//                                            (ArrayList<String>) document.getData().get("contents"),
//                                            document.getData().get("publisher").toString(),
//                                            new Date(document.getDate("createdAt").getTime())));
//
//
////                                        public PostInfo(String title, ArrayList<String> contents, ArrayList<String> formats, String publisher, Date createdAt){
////                                }
//
//
//                                RecyclerView.Adapter mAdapter = new MainAdapter(, postList);
//                                recyclerView.setAdapter(mAdapter);
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
//        }
//
//    }

//
//    private void myStartActivity(Class c) {
//        Intent intent = new Intent(this, c);
//        startActivity(intent);
//    }


//    private OnFragmentInteractionListener mListener;
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void messageFromChildFragment(Uri uri);
//    }

//161번째 줄도 주석 풀어야 함
//    private void showData() {
//        final DocumentReference documentReference = firebaseFirestore.collection("Posts").document();
//
//        firebaseFirestore.collection("Posts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                        //show data
//                        for(DocumentSnapshot doc:task.getResult()){
//
//                            try {
//                                WriteInfo writeInfo = new WriteInfo(
//                                        doc.getString("posts_id"),
//                                        doc.getString("isbn"),
//                                        doc.getString("title"),
//                                        doc.getString("nickname"),
//                                        doc.getString("contents"),
//                                        doc.getString("imagePath"),
//                                        doc.getString("publisher"),
//                                        doc.getTimestamp("uploadTime"),
//                                        doc.getLong("num_heart").intValue(),
//                                        doc.getLong("num_comment").intValue());
//
//                                writeInfoList.add(writeInfo);
//
//                            } catch (RuntimeException e){
//                                System.out.println(e);
//                            }
//
//                        }
//                        // 지금은 오류남
////                        //adapter
////                        adapter = new CustomAdapter(gachon.mp.livre_bottom_navigation.FeedFragment.this, writeInfoList);
////                        //set adapter to recyclerview
////                        mRecyclerView.setAdapter(adapter);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //called when there is any error while retrieving
//                        startToast(e.getMessage());
//                    }
//                });
//    }


    private void startToast(String msg){
        Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
    }

}
