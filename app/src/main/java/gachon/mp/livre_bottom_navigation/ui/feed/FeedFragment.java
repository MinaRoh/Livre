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

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) getView().findViewById(R.id.autoCompleteTextView);
        final EditText edtsearch = getView().findViewById(R.id.autoCompleteTextView);
        // edtsearch 에 adapter 를 연결한다
//        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, autotextviewlist));
//
//        edtsearch.setClickable(false);
//        edtsearch.setFocusable(false);
//
        LinearLayout layout01 = (LinearLayout) getActivity().findViewById(R.id.linear);
        layout01.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                String query = edtsearch.getText().toString();
                Intent intent = new Intent(getActivity(), FeedSearchActivity.class);
//                intent.putExtra("query", query);
                startActivity(intent);
            }
        });


    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void startToast(String msg){
        Toast.makeText(getActivity(),msg, Toast.LENGTH_SHORT).show();
    }

}
