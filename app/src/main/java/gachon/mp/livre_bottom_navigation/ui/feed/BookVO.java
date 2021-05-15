package gachon.mp.livre_bottom_navigation.ui.feed;

public class BookVO {
    private String title;
    private String image;
    private String author;
    private String description;
    private int isbn;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIsbn() { return isbn; }

    public void setIsbn(int isbn) { this.isbn = isbn; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public String toString() {
        return "BookVO{" +
                "title='" + title + '\'' +
                ", author=" + author +
                ", image='" + image + '\'' +
                '}';
    }
}
