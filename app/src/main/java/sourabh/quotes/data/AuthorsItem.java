package sourabh.quotes.data;

import java.util.ArrayList;

/**
 * Created by Kajo on 2/2/2017.
 */

public class AuthorsItem {

    private String id_author;
    private String author_name;
    private String author_description;
    private String author_image;

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

    private String author_likes_count;
    private String created_on;

    private ArrayList<AuthorQuotesItem> authorQuotesItemArrayList;

    public ArrayList<AuthorQuotesItem> getAuthorQuotesItemArrayList() {
        return authorQuotesItemArrayList;
    }

    public void setAuthorQuotesItemArrayList(ArrayList<AuthorQuotesItem> authorQuotesItemArrayList) {
        this.authorQuotesItemArrayList = authorQuotesItemArrayList;
    }

    public AuthorsItem() {

    }

    public String getId_author() {
        return id_author;
    }

    public void setId_author(String id_author) {
        this.id_author = id_author;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_likes_count() {
        return author_likes_count;
    }

    public void setAuthor_likes_count(String author_likes_count) {
        this.author_likes_count = author_likes_count;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }
}
