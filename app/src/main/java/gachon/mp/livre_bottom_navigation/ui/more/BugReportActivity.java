package gachon.mp.livre_bottom_navigation.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gachon.mp.livre_bottom_navigation.R;

public class BugReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_more_6bug_report);

        TextView email = (TextView) findViewById(R.id.textView);
        email.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                String[] address = {"email@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                startActivity(email);
            }
        });
    }

}
