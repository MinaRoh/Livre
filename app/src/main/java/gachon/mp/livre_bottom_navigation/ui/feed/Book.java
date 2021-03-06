package gachon.mp.livre_bottom_navigation.ui.feed;

public class Book {
    private String book_title;
    private String author;
    private String imageUrl;
    private String uid;
    private String isbn;

    public Book (String book_title, String author, String imageUrl, String uid, String isbn) {
        this.book_title = book_title;
        this.author = author;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.isbn = isbn;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsbn() { return isbn; }

    public void setIsbn(String isbn) { this.isbn = isbn; }
}
