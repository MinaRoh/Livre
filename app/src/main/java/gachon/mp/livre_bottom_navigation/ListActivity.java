package gachon.mp.livre_bottom_navigation;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import gachon.mp.livre_bottom_navigation.ui.writing.WriteInfo;

public class ListActivity extends AppCompatActivity {
    private static final String TAG="ListActivity";
    private FirebaseAuth mAuth;

    List<WriteInfo> writeInfoList = new ArrayList<>();
    RecyclerView mRecyclerView;
    //layout manager for recyclerview
    RecyclerView.LayoutManager layoutManager;

    //firestore instance
    FirebaseFirestore db;
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //init firestore
        db = FirebaseFirestore.getInstance();


        //initialize views
        mRecyclerView = findViewById(R.id.recycler_view);
        //set recycler view properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //show data in recyclerVeiw
        showData();



    }

    private void showData() {
        final DocumentReference documentReference = db.collection("Posts").document();

        db.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        //show data
                        for(DocumentSnapshot doc:task.getResult()){

                            try {
                                WriteInfo writeInfo = new WriteInfo(
                                        doc.getString("posts_id"),
                                        doc.getString("isbn"),
                                        doc.getString("book_title"),
                                        doc.getString("title"),
                                        doc.getString("nickname"),
                                        doc.getString("contents"),
                                        doc.getString("imagePath"),
                                        doc.getString("publisher"),
                                        doc.getTimestamp("uploadTime"),
                                        doc.getLong("num_heart").intValue(),
                                        doc.getLong("num_comment").intValue(),
                                        (ArrayList<String>) doc.get("userlist_heart"));

                                writeInfoList.add(writeInfo);

                            } catch (RuntimeException e){
                                System.out.println(e);
                            }

                        }
                        //adapter
                        adapter = new CustomAdapter(gachon.mp.livre_bottom_navigation.ListActivity.this, writeInfoList);
                        //set adapter to recyclerview
                        mRecyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //called when there is any error while retrieving
                        startToast(e.getMessage());
                    }
                });
    }




    private void startToast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }


}