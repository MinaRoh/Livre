package gachon.mp.livre_bottom_navigation.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.mypage.CommentInfo;
import gachon.mp.livre_bottom_navigation.ui.mypage.PostActivity;

public class FeedDetailAdapter extends RecyclerView.Adapter<FeedDetailAdapter.ViewHolder>{
    ArrayList<CommentInfo> items = new ArrayList<CommentInfo>();
    private static final String TAG = "FeedDetailAdapter";
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.mypage_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        CommentInfo item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;//?????????
        TextView textView;
        TextView textView2;
        ImageView imageView2;//????????? ?????????
        TextView textView3;
        TextView textView4;
        TextView textView5;
        TextView textView6;
        TextView textView7;
        FirebaseUser user;
        String profileImg;//????????? ????????? ????????? URL
        String post_id;//????????? ?????????
        String publisher_id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_profile);
            textView = itemView.findViewById(R.id.nickname);
            textView2 = (TextView)itemView.findViewById(R.id.time);
            imageView2 = (ImageView)itemView.findViewById(R.id.content_image);
            textView3 = (TextView)itemView.findViewById(R.id.title);
            textView4 = (TextView)itemView.findViewById(R.id.contents);
            textView5 = (TextView)itemView.findViewById(R.id.num_heart);
            textView6 = (TextView)itemView.findViewById(R.id.num_comment);
            textView7 = (TextView)itemView.findViewById(R.id.book_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent;
                    intent = new Intent(context, PostViewActivity.class);
                    intent.putExtra("posts_id", post_id);//????????? ??????????????? ?????? id ??????
                    Log.d(TAG, "posts_id: " + post_id);
                    context.startActivity(intent);
                }
            });
        }

        public void setItem(CommentInfo item){
            post_id = item.getPost_id();
            publisher_id = item.getPublisher_id();

            textView.setText(item.getNickname());//????????? ?????????
            textView2.setText(item.getTime());//????????? ??????
            textView3.setText(item.getTitle());//??? ??????
            textView4.setText(item.getContents());//??? ??????
            textView5.setText(String.valueOf(item.getNum_heart()));//?????? ??????
            textView6.setText(String.valueOf(item.getNum_comment()));//?????? ??????
            textView7.setText(item.getBook_title());
            /*?????? ????????? ????????? ????????????*/
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setClipToOutline(true);
            }
            FirebaseFirestore db_user = FirebaseFirestore.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            db_user.collection("Users")
                    .whereEqualTo("uid", publisher_id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    profileImg = document.get("profileImage").toString();
                                }
                                //FirebaseStorage ??????????????? ??????
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                // ?????? ???????????? ???????????? ??????????????? ??????
                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://mp-livre.appspot.com/"+profileImg);
                                //StorageReference?????? ?????? ???????????? URL ?????????
                                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            Glide.with(itemView)
                                                    .load(task.getResult())
                                                    .override(1024, 980)
                                                    .into(imageView);
                                        }
                                    }
                                });
                            }
                        }
                    });

            /*????????? ????????? ????????????*/
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://mp-livre.appspot.com");
            String filename = item.getPostImage();
            StorageReference pathReference = storageRef.child("images");
            if(filename!=null && pathReference != null){
                StorageReference submitProfile = storage.getReferenceFromUrl("gs://mp-livre.appspot.com").child("images/"+ publisher_id + "/" + filename);
                submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if(imageView2!=null){
                            Glide.with(itemView)
                                    .load(uri)
                                    .override(1024, 980)
                                    .into(imageView2);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting image", e);
                    }
                });
            }
        }


    }
    public void addItem(CommentInfo item){
        items.add(item);
    }
    public void setItems(ArrayList<CommentInfo> items){
        this.items = items;
    }

    public CommentInfo getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, CommentInfo item){
        items.set(position, item);
    }
}
