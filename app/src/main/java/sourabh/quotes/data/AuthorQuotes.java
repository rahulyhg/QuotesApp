
package sourabh.quotes.data;

import java.util.List;

public class AuthorQuotes {

    private List<AuthorQuote> author_quotes = null;
    private List<AuthorQuote> authors = null;
    private List<AuthorQuote> quote_of_the_day = null;
    private List<AuthorQuote> most_fevourite_quote = null;
    private List<AuthorQuote> random_quote = null;

    public List<AuthorQuote> getQuote_of_the_day() {
        return quote_of_the_day;
    }

    public void setQuote_of_the_day(List<AuthorQuote> quote_of_the_day) {
        this.quote_of_the_day = quote_of_the_day;
    }

    public List<AuthorQuote> getMost_fevourite_quote() {
        return most_fevourite_quote;
    }

    public void setMost_fevourite_quote(List<AuthorQuote> most_fevourite_quote) {
        this.most_fevourite_quote = most_fevourite_quote;
    }

    public List<AuthorQuote> getRandom_quote() {
        return random_quote;
    }

    public void setRandom_quote(List<AuthorQuote> random_quote) {
        this.random_quote = random_quote;
    }

    public List<AuthorQuote> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorQuote> authors) {
        this.authors = authors;
    }

    public List<AuthorQuote> getAuthor_quotes() {
        return author_quotes;
    }

    public void setAuthor_quotes(List<AuthorQuote> author_quotes) {
        this.author_quotes = author_quotes;
    }

}
