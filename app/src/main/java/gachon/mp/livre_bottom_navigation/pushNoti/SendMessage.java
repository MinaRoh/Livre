package gachon.mp.livre_bottom_navigation.pushNoti;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import gachon.mp.livre_bottom_navigation.UserToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendMessage {
    private static final String TAG = "Send Message";
    List<String> uidList = new ArrayList<>();
    List<String> tokenList = new ArrayList<>();
    FirebaseFirestore db;
    Handler handler= new Handler();
    Timestamp uploadTime;
    String category;
    String senderNick;
    String postTitle;
    String msgTitle;
    String msgContent;


//    public SendMessage(List uidList, String title, String message, Timestamp uploadTime){
//        this.uidList=uidList;
//        this.title=title;
//        this.message=message;
//        this.uploadTime=uploadTime;
//        store();// store token
//    }
    //보내는사람, 받는사람, 카테고리, 글제목
    public SendMessage(String senderNick, List uidList, String category, String postTitle){
        this.senderNick = senderNick;
        this.uidList=uidList;
        this.category=category;
        this.postTitle=postTitle;
        setMessage();
        Timestamp uploadTime = new Timestamp(new Date());
        store();// store token
    }

    private void setMessage(){
        if(category.equals("heart")){
            msgTitle = "좋아요 알림";
            msgContent = senderNick + "님이 나의'" +postTitle+ "' 글에 좋아요를 누르셨습니다";
        }else if(category.equals("comment")){
            msgTitle = "댓글 알림";
            msgContent = senderNick + "님이 나의'" +postTitle+ "' 글에 댓글을 남기셨습니다";
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
}
