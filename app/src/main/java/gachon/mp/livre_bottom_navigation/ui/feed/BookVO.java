package gachon.mp.livre_bottom_navigation.ui.feed;

public class BookVO {
    // private String author;
    private String title;
    // private int price;
    private String image;
    private String author;
    private String description;

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

//    public int getPrice() {
//        return price;
//    }

//    public void setPrice(int price) {
//        this.price = price;
//    }

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
//        return "BookVO{" +
//                "author='" + author + '\'' +
//                ", price=" + price +
//                ", image='g" + image + '\'' +
//                '}';
        return "BookVO{" +
                "title='" + title + '\'' +
                ", author=" + author +
                ", image='" + image + '\'' +
                '}';
    }
}
