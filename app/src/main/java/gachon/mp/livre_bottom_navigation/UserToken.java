package gachon.mp.livre_bottom_navigation;

public class UserToken {
    private String uid;
    private String token;

    UserToken(){}

    public UserToken(String uid, String token){
        this.uid=uid;
        this.token=token;
    }

    public String getToken() {
        return token;
    }
    public String getUid() {
        return uid;
    }

}
