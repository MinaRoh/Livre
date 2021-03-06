package gachon.mp.livre_bottom_navigation;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    public static Context mainContext;
    public static Activity Main_Activity;
    public Bundle mBundle;
    public String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Main_Activity = MainActivity.this;
        mainContext = this;
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_feed,
                R.id.navigation_mypage,
                R.id.navigation_more)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

//          navigation ?????? ????????????
//        final String prevLabel = (String) navController.getCurrentDestination().getLabel();
//        String currLabel = (String) navController.getCurrentDestination().getLabel();
//        System.out.println("************** prevLabel : " + prevLabel);
//        System.out.println("************** currLabel : " + currLabel);



        NavigationUI.setupWithNavController(navView, navController);


        final String[] userNick = new String[1];

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){

        }else{
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("Users").document(user.getUid());
            docRef.get().addOnCompleteListener((task) -> {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document!=null){
                        if(document.exists()){
                            userNick[0] = document.getString("nickname");
                            System.out.println("Main????????? db ???????????? ???????????? userNick: "+ userNick[0]);
                            nickname = userNick[0];
                            Log.d(TAG, "DocumentSnapshot data: "+document.getData());
                        } else{
                            Log.d(TAG, "No such document");

                        }
                    }


//                    //post list ????????????
//                    db.collection("posts")
//                            .get()
//                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if(task.isSuccessful()){
//                                        for(QueryDocumentSnapshot document : task.getResult()){
//                                            Log.d(TAG, document.getID() + " => " + document.getData());
//                                            postList.add();
//                                        }
//                                    }
//                                }
//                            })


                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        }

//        System.out.println("Main????????? userNick: "+ userNick[0]);


    }


    // ???????????? ?????? ????????? ?????? ?????? ????????? ?????????????????????.
    @Override
    public void onBackPressed() {
        // AlertDialog ????????? ????????? ????????? ???????????? ?????? ?????????
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("?????????????????????????");

        // "???" ????????? ????????? ???????????? ?????????
        alBuilder.setPositiveButton("???", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // ?????? ??????????????? ????????????. (MainActivity?????? ???????????? ????????? ????????????????????? ????????????.)
                System.exit(0);
            }
        });
        // "?????????" ????????? ????????? ???????????? ?????????
        alBuilder.setNegativeButton("?????????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; // ????????? ????????? ?????? ?????? ????????????
            }
        });
        alBuilder.setTitle("???????????? ??????");
        alBuilder.show(); // AlertDialog.Bulider??? ?????? AlertDialog??? ????????????.
    }



}