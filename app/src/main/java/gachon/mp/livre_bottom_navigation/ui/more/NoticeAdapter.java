//package gachon.mp.livre_bottom_navigation.ui.more;
//
//import android.content.Context;
//import android.graphics.drawable.ShapeDrawable;
//import android.graphics.drawable.shapes.OvalShape;
//import android.net.Uri;
//import android.os.Build;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.Timestamp;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import gachon.mp.livre_bottom_navigation.NotiInfo;
//import gachon.mp.livre_bottom_navigation.R;
//import gachon.mp.livre_bottom_navigation.ui.feed.Feed;
//import gachon.mp.livre_bottom_navigation.ui.mypage.CommentInfo;
//
//public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
//    ArrayList<Feed> items = new ArrayList<Feed>();
//    AlarmActivity alarmActivity;
//    List<NotiInfo> writeNotiList;
//    private static final String TAG = "NoticeAdapter";
//
//    public NoticeAdapter(AlarmActivity alarmActivity, List<NotiInfo> writeNotiList) {
//        this.alarmActivity = alarmActivity;
//        this.writeNotiList = writeNotiList;
//    }
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
//        View itemView = inflater.inflate(R.layout.notice_item, viewGroup, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
//        Feed item = items.get(position);
//        viewHolder.setItem(item);
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//
//        ImageView imageView;//프로필
//        TextView textView;
//        TextView textView2;
//        TextView textView3;
//        TextView textView4;
//        TextView textView7;
//        FirebaseUser user;
//        String profileImg;//프로필 이미지 저장소 URL
//        String post_id;//포스트 아이디
//        String publisher_id;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.user_profile);
//            textView = itemView.findViewById(R.id.nickname);
//            textView2 = (TextView)itemView.findViewById(R.id.time);
//            textView3 = (TextView)itemView.findViewById(R.id.title);
//            textView4 = (TextView)itemView.findViewById(R.id.contents);
//            textView7 = (TextView)itemView.findViewById(R.id.book_title);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Context context = view.getContext();
//
//                }
//            });
//        }
//
//        public void setItem(Feed item){
//            post_id = item.getPost_id();
//            publisher_id = item.getPublisher_id();
//
//            textView.setText(item.getNickname());//작성자 닉네임
//            textView2.setText(item.getTime());//업로드 시간
//            textView3.setText(item.getTitle());//글 제목
//            textView4.setText(item.getContents());//글 내용
//            textView7.setText(item.getBook_title());
//            /*유저 프로필 이미지 불러오기*/
//            imageView.setBackground(new ShapeDrawable(new OvalShape()));
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                imageView.setClipToOutline(true);
//            }
//            FirebaseFirestore db_user = FirebaseFirestore.getInstance();
//            user = FirebaseAuth.getInstance().getCurrentUser();
//            db_user.collection("Users")
//                    .whereEqualTo("uid", publisher_id)
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    profileImg = document.get("profileImage").toString();
//                                }
//                                //FirebaseStorage 인스턴스를 생성
//                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//                                // 위의 저장소를 참조하는 파일명으로 지정
//                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://mp-livre.appspot.com/"+profileImg);
//                                //StorageReference에서 파일 다운로드 URL 가져옴
//                                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Uri> task) {
//                                        if (task.isSuccessful()) {
//                                            Glide.with(itemView)
//                                                    .load(task.getResult())
//                                                    .override(1024, 980)
//                                                    .into(imageView);
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//
//
//            db_user.collection("Alarm")
//                    .whereEqualTo("receiver", user.getUid())
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    profileImg = document.get("profileImage").toString();
//
//
//
//                                    this.uploadTime = uploadTime;
//                                    this.category = category;
//                                    this.msgTitle = msgTitle;
//                                    this.msgContent = msgContent;
//                                    this.sender = sender;
//                                    this.receiver = receiver;
//
//
//                                    Timestamp time = (Timestamp) document.getData().get("uploadTime");
//                                    String title = document.getData().get("msgTitle").toString();
//                                    String txt_book = document.getData().get("category").toString();
//                                    String contents = document.getData().get("msgContent").toString();
//                                    String sender = document.getData().get("sender").toString();
//                                    String receiver = document.getData().get("receiver").toString();
//
//                                    adapter.addItem(new CommentInfo(user.getUid(), post_id, nickname, upload_time, imagePath, title, txt_book, contents, num_heart, num_comment));
//                                    recyclerView.setAdapter(adapter);
//
//                                }
//                                //FirebaseStorage 인스턴스를 생성
//                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//                                // 위의 저장소를 참조하는 파일명으로 지정
//                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://mp-livre.appspot.com/"+profileImg);
//                                //StorageReference에서 파일 다운로드 URL 가져옴
//                                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Uri> task) {
//                                        if (task.isSuccessful()) {
//                                            Glide.with(itemView)
//                                                    .load(task.getResult())
//                                                    .override(1024, 980)
//                                                    .into(imageView);
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//
//
//        }
//
//    }
//    public void addItem(Feed item){
//        items.add(item);
//    }
//    public void setItems(ArrayList<Feed> items){
//        this.items = items;
//    }
//
//    public Feed getItem(int position){
//        return items.get(position);
//    }
//    public void setItem(int position, Feed item){
//        items.set(position, item);
//    }
//}
