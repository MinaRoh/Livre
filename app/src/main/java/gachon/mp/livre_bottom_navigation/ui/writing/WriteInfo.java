package gachon.mp.livre_bottom_navigation.ui.writing;

import com.google.firebase.Timestamp;
import com.google.type.DateTime;

public class WriteInfo {
    private String title;
    private String contents;
    private String publisher;
    private int num_heart;
    private int num_comment;
    private Timestamp upload_time;
    public WriteInfo(String title, String contents, String publisher, Timestamp upload_time, int num_heart, int num_comment){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.upload_time = upload_time;
        this.num_heart = num_heart;
        this.num_comment = num_comment;
    }

    public String getTitle(){ return this.title;}
    public void setTitle(String title){ this.title = title;}
    public String getContents(){ return this.contents;}
    public void setContents(String contents){ this.contents = contents;}
    public String getPublisher(){ return this.publisher;}
    public void setPublisher(String publisher){ this.publisher = publisher;}
    public int getNum_heart(){ return this.num_heart;}
    public void setNum_heart(Integer num_heart){ this.num_heart = num_heart;}
    public int getNum_comment(){ return this.num_comment;}
    public void setNum_comment(Integer num_comment){ this.num_comment = num_comment;}
    public Timestamp getUploadTime(){ return this.upload_time;}
    public void setUploadTime(Timestamp upload_time){ this.upload_time= upload_time;}
}
