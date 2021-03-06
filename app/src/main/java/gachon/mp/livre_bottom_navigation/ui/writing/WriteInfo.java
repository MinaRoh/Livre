package gachon.mp.livre_bottom_navigation.ui.writing;

import android.net.Uri;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

public class WriteInfo {
    private String book_title;
    private String title;
    private String contents;
    private String publisher;
    private int num_heart;
    private int num_comment;
    private Timestamp upload_time;
    private String nickname;
    private String ISBN;
    private String imagePath;
    private String posts_id;
    private ArrayList userlist_heart;


    public WriteInfo(String posts_id, String ISBN, String book_title, String title, String nickname, String contents, String imagePath, String publisher, Timestamp upload_time, int num_heart, int num_comment, ArrayList userlist_heart){
        this.posts_id = posts_id;
        this.ISBN = ISBN;
        this.book_title = book_title;
        this.title = title;
        this.nickname = nickname;
        this.contents = contents;
        this.imagePath = imagePath;
        this.publisher = publisher;
        this.upload_time = upload_time;
        this.num_heart = num_heart;
        this.num_comment = num_comment;
        this.userlist_heart = userlist_heart;

    }

    public String getPosts_id() { return posts_id; }
    public void setPosts_id(String posts_id) { this.posts_id = posts_id; }
    public String getTitle(){ return this.title;}
    public void setTitle(String title){ this.title = title;}
    public String getBookTitle(){ return this.book_title;}
    public void setBookTitle(String title){ this.book_title = book_title;}
    public String getISBN(){ return this.ISBN;}
    public void setISBN(String ISBN){ this.ISBN = ISBN;}
    public String getNickname(){ return this.nickname;}
    public void setNickname(String nickname){ this.nickname = nickname;}
    public String getContents(){ return this.contents;}
    public void setContents(String contents){ this.contents = contents;}
    public String getPublisher(){ return this.publisher;}
    public void setImagePath(String imagePath){ this.imagePath = imagePath;}
    public String getImagePath(){ return this.imagePath;}
    public void setPublisher(String publisher){ this.publisher = publisher;}
    public int getNum_heart(){ return this.num_heart;}
    public void setNum_heart(Integer num_heart){ this.num_heart = num_heart;}
    public int getNum_comment(){ return this.num_comment;}
    public void setNum_comment(Integer num_comment){ this.num_comment = num_comment;}
    public Timestamp getUploadTime(){ return this.upload_time;}
    public void setUploadTime(Timestamp upload_time){ this.upload_time= upload_time;}
    public ArrayList getUserlist_heart() {
        return userlist_heart;
    }

    public void setUserlist_heart(ArrayList userlist_heart) {
        this.userlist_heart = userlist_heart;
    }
}
