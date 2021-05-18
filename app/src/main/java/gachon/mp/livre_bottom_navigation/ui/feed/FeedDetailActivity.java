package gachon.mp.livre_bottom_navigation.ui.feed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import gachon.mp.livre_bottom_navigation.R;

public class FeedDetailActivity extends AppCompatActivity {
    String titleFromMain;
    String descriptionFromMain;
    String authorFromMain;
    String imageFromMain;
    String isbnFromMain;

    TextView title;
    TextView author;
    TextView description;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_feed_detail);

        // activity_book_detail 에 put 할 것들
        title = findViewById(R.id.txttitle);
        description = findViewById(R.id.txtdescription);
        author = findViewById(R.id.txtauthor);
        image = findViewById(R.id.image_detail);

        Intent intent = getIntent();
        titleFromMain = intent.getStringExtra("title");
        descriptionFromMain = intent.getStringExtra("description");
        authorFromMain = intent.getStringExtra("author");
        imageFromMain = intent.getStringExtra("image");
        // isbn
        isbnFromMain = intent.getStringExtra("isbn");
        // test
        System.out.println("***********************************************************************");
        System.out.println(isbnFromMain);

        title.setText(titleFromMain);
        description.setText(descriptionFromMain);
        author.setText(authorFromMain);

        // load image
        Picasso.get().load(imageFromMain).into(image);
    }

    public void mOnClick(View view) {
        finish();
    }
}
