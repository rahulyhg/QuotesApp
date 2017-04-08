
package sourabh.quotes.data;

import java.util.List;

public class AuthorQuote {

    private Integer author_id;
    private String author_name;
    private String author_description;
    private String author_image;
    private Integer author_likes_count;
    private String created_at;
    private List<Quote> quotes = null;

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_description() {
        return author_description;
    }

    public void setAuthor_description(String author_description) {
        this.author_description = author_description;
    }

    public String getAuthor_image() {
        return author_image;
    }

    public void setAuthor_image(String author_image) {
        this.author_image = author_image;
    }

    public Integer getAuthor_likes_count() {
        return author_likes_count;
    }

    public void setAuthor_likes_count(Integer author_likes_count) {
        this.author_likes_count = author_likes_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

}
