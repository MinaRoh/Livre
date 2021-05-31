package gachon.mp.livre_bottom_navigation.pushNoti;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import gachon.mp.livre_bottom_navigation.NotiInfo;
import gachon.mp.livre_bottom_navigation.Protocol;
import gachon.mp.livre_bottom_navigation.UserToken;
import gachon.mp.livre_bottom_navigation.ui.mypage.PostActivity;
import gachon.mp.livre_bottom_navigation.ui.writing.WriteInfo;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendMessage {
    private static final String TAG = "Send Message";
    List<String> uidList = new ArrayList<>();
    List<String> tokenList = new ArrayList<>();
    private FirebaseUser user;
    FirebaseFirestore db;
    Handler handler= new Handler();
    Timestamp uploadTime;
    String category;
    String senderNick;
    String receiverNick;
    String postTitle;
    String msgTitle;
    String msgContent;

    //보내는사람, 받는사람, 카테고리, 글제목
    public SendMessage(String senderNick, List uidList, String category, String postTitle){
        this.senderNick = senderNick;
        this.uidList=uidList;
        this.category=category;
        this.postTitle=postTitle;
        setMessage();
        uploadTime = new Timestamp(new Date());

//        if(checkSetting()) {//알림설정 켜져있으면
//            handler.postDelayed(new Runnable(){
//                public void run(){
//                    store();// store token // 푸시알림 전송
//                }
//            }, 2000); // 1sec
//
//        }
        store();// store token // 푸시알림 전송
        //안켜져있어도 파베에는 넣어야함
        saveNoti(); //save notification to firestore Alarm
    }

//    //보내는사람, 받는사람, 카테고리, 글제목
//    public SendMessage(String senderNick, String receiverNick, List uidList, String category, String postTitle){
//        this.senderNick = senderNick;
//        this.receiverNick = receiverNick;
//        this.uidList=uidList;
//        this.category=category;
//        this.postTitle=postTitle;
//        setMessage();
//        uploadTime = new Timestamp(new Date());
//
////        if(checkSetting()) {//알림설정 켜져있으면
////            handler.postDelayed(new Runnable(){
////                public void run(){
////                    store();// store token // 푸시알림 전송
////                }
////            }, 2000); // 1sec
////
////        }
//        store();// store token // 푸시알림 전송
//        //안켜져있어도 파베에는 넣어야함
//        saveNoti(); //save notification to firestore Alarm
//    }

    private void setMessage(){
        if(category.equals("heart")){
            msgTitle = "좋아요 알림";
            msgContent = senderNick + "님이 나의 '" +postTitle+ "' 글에 좋아요를 누르셨습니다";
        }else if(category.equals("comment")){
            msgTitle = "댓글 알림";
            msgContent = senderNick + "님이 나의 '" +postTitle+ "' 글에 댓글을 남기셨습니다";
        }
    }

    private void store() {// extract token list to Users

        db=FirebaseFirestore.getInstance();
        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                UserToken userToken = new UserToken(
                                        doc.getString("uid"),
                                        doc.getString("token")
                                );
                                Log.d(TAG, doc.getId() + " => " + doc.getData());
                                if(uidList.contains(userToken.getUid())){
                                    addTokenList(userToken.getToken());
                                    Log.d(TAG, doc.getId() + " ===> " + userToken.getToken());
                                }
                            }
                            send(); // send to token user
                            System.out.println("store에서 send 완료");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void send(){
        Log.d(TAG, "Token List Size = "+tokenList.size());
        for(int i=0;i<tokenList.size();i++){
            SendNotification.sendNotification(tokenList.get(i), msgTitle, msgContent);
            Log.d(TAG, "Send to "+tokenList.get(i));
        }
    }
    private void addTokenList(String newToken){
        tokenList.add(newToken);
    }

    public boolean checkSetting(){
        final boolean[] status = {true};
        db.collection("Users")
                .whereEqualTo("uid", user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //comment 알림은 아직
//                                String current_comment = document.get("commentAlarm").toString();
//                                if (current_comment.equals("on")) status[0] = true;
//                                else status[0] = false;

                                String current_heart = document.get("heartAlarm").toString();
                                if(current_heart.equals("on")) status[0] = true;
                                else status[0] = false;

                            }
                        }
                    }
                });
        return status[0];


    }


    // 1. 보내는 알람 파베에 저장하기. Alarm -> uid 안에.
    private void saveNoti(){

        user = FirebaseAuth.getInstance().getCurrentUser();
        NotiInfo notiInfo = new NotiInfo(uploadTime, category, msgTitle, msgContent, senderNick);
        uploader(notiInfo);
    }

    /*Firestore Posts에 포스트를 업로드 하는 메소드*/
    private void uploader(NotiInfo notiInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Alarm").add(notiInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: "+documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });
    }
}
