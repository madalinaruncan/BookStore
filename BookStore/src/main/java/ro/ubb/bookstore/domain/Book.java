package ro.ubb.bookstore.domain;

public class Book extends BaseEntity<Long> {

    private String title;
    private String author_firstname;
    private String author_lastname;
    private int release_year;
    private double price;

    public Book(){}

    public Book(String title, String author_firstname, String author_lastname, int release_year, double price) {
        this.title = title;
        this.author_firstname = author_firstname;
        this.author_lastname = author_lastname;
        this.release_year = release_year;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor_firstname() {
        return author_firstname;
    }

    public void setAuthor_firstname(String author_firstname) {
        this.author_firstname = author_firstname;
    }

    public String getAuthor_lastname() {
        return author_lastname;
    }

    public void setAuthor_lastname(String author_lastname) {
        this.author_lastname = author_lastname;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + super.getId() + '\'' +
                "title='" + title + '\'' +
                ", author_firstname='" + author_firstname + '\'' +
                ", author_lastname='" + author_lastname + '\'' +
                ", release_year=" + release_year +
                ", price=" + price +
                '}';
    }
}
