package gachon.mp.livre_bottom_navigation.ui.home;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.feed.Book;
import gachon.mp.livre_bottom_navigation.ui.mypage.CommentInfo;
import gachon.mp.livre_bottom_navigation.ui.mypage.MyPageAdapter;
import gachon.mp.livre_bottom_navigation.ui.mypage.PostActivity;
import gachon.mp.livre_bottom_navigation.ui.writing.WritingActivity;

public class ReadingBookAdapter extends RecyclerView.Adapter<ReadingBookAdapter.ViewHolder>{
    private static final String TAG = "ReadingBookAdapter";
    ArrayList<Book> items = new ArrayList<Book>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;
    @NonNull
    @Override
    public ReadingBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.readingbook_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReadingBookAdapter.ViewHolder viewHolder, int position) {
        Book item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView reading_img;
        TextView reading_book_title;
        TextView reading_author;
        String isbn;
        Button btn_toWriting;
        String book_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reading_img = (ImageView) itemView.findViewById(R.id.reading_img);
            reading_book_title = (TextView) itemView.findViewById(R.id.reading_book_title);
            reading_author = (TextView)itemView.findViewById(R.id.reading_author);

            /* writing activity 로 를 누르면 해당 책의 writing activity로 이동한다 */
            btn_toWriting = (Button) itemView.findViewById(R.id.btn_toWriting);
            btn_toWriting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent writing_intent = new Intent(context, WritingActivity.class);
                    writing_intent.putExtra("isbn", isbn);
                    writing_intent.putExtra("book_title", book_title);
                    context.startActivity(writing_intent);
                }
            });
        }

        // 읽고 있는 책 정보 db 에서 가져오기
        // user id 가 같은 경우에~
        public void setItem(Book item) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            String user_id = user.getUid();//사용자 uid
            book_title = item.getBook_title();
            reading_book_title.setText(item.getBook_title());
            reading_author.setText(item.getAuthor());
            isbn = item.getIsbn();

            Picasso.get().load(item.getImageUrl()).resize(200,270).into(reading_img);
        }

    }

    public void addItem(Book item){
        items.add(item);
    }

}
