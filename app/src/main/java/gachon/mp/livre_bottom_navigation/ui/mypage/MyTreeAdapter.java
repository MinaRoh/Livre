package gachon.mp.livre_bottom_navigation.ui.mypage;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.R;

public class MyTreeAdapter extends RecyclerView.Adapter<MyTreeAdapter.ViewHolder>{
    ArrayList<MyTree> items = new ArrayList<MyTree>();
    private static final String TAG = "MyTreeAdapter";

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.mytree_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MyTree item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void setItem(MyTree item) {
            String tree_url = item.getTree_url();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://mp-livre.appspot.com");
            StorageReference pathReference = storageRef.child("tree_complete");
            if(tree_url!=null && pathReference != null){
                StorageReference submitProfile = storage.getReferenceFromUrl("gs://mp-livre.appspot.com/"+tree_url);
                submitProfile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if(imageView!=null){
                            Glide.with(itemView)
                                    .load(uri)
                                    .override(1024, 980)
                                    .into(imageView);
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
    public void addItem(MyTree item){
        items.add(item);
    }
    public void setItems(ArrayList<MyTree> items){
        this.items = items;
    }

    public MyTree getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, MyTree item){
        items.set(position, item);
    }
}
