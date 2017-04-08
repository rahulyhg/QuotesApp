
package sourabh.quotes.data;

import java.util.List;

public class Quote {

    private Integer quote_id;
    private Integer author_id;
    private String quote;
    private Integer quote_likes_count;
    private String created_at;
    private List<Tag> tags = null;

    public Integer getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(Integer quote_id) {
        this.quote_id = quote_id;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Integer getQuote_likes_count() {
        return quote_likes_count;
    }

    public void setQuote_likes_count(Integer quote_likes_count) {
        this.quote_likes_count = quote_likes_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

}
