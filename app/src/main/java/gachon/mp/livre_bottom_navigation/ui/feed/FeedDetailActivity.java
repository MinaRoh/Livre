package gachon.mp.livre_bottom_navigation.ui.feed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import gachon.mp.livre_bottom_navigation.R;
import gachon.mp.livre_bottom_navigation.ui.writing.WritingActivity;

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

    Button writing_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

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

        //ISBN과 책 제목을 WritingActivity로 전달
        writing_report = findViewById(R.id.btn_write_report);
        writing_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isbn_toWriting=isbnFromMain;
                String title_toWriting=titleFromMain;

                Intent writing_intent=new Intent(getApplicationContext(), WritingActivity.class);
                writing_intent.putExtra("isbn", isbn_toWriting);
                writing_intent.putExtra("book_title", title_toWriting);

                startActivity(writing_intent);
            }
        });
    }

    public void mOnClick(View view) {
        finish();
    }
}
