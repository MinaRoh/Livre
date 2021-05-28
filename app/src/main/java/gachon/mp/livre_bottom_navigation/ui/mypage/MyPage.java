package gachon.mp.livre_bottom_navigation.ui.mypage;

public class MyPage {
    String nickname;
    String time;
    String postImage;
    String title;
    String contents;
    int num_heart;
    int num_comment;

    public MyPage(String nickname, String time, String postImage, String title, String contents, int num_heart, int num_comment) {
        this.nickname = nickname;
        this.time = time;
        this.postImage = postImage;
        this.title = title;
        this.contents = contents;
        this.num_heart = num_heart;
        this.num_comment = num_comment;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getNum_heart() {
        return num_heart;
    }

    public void setNum_heart(int num_heart) {
        this.num_heart = num_heart;
    }

    public int getNum_comment() {
        return num_comment;
    }

    public void setNum_comment(int num_comment) {
        this.num_comment = num_comment;
    }
}
