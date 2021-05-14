package gachon.mp.livre_bottom_navigation.ui.writing;

public class WriteInfo {
    private String title;
    private String contents;
    private String publisher;
    private int num_heart;

    public WriteInfo(String title, String contents, String publisher, int num_heart){
        this.title = title;
        this.contents = contents;
        this.publisher = publisher;
        this.num_heart = num_heart;
    }

    public String getTitle(){ return this.title;}
    public void setTitle(String title){ this.title = title;}
    public String getContents(){ return this.contents;}
    public void setContents(String contents){ this.contents = contents;}
    public String getPublisher(){ return this.publisher;}
    public void setPublisher(String publisher){ this.publisher = publisher;}
    public int getNum_heart(){ return this.num_heart;}
    public void setNum_heart(String publisher){ this.num_heart = num_heart;}

}
