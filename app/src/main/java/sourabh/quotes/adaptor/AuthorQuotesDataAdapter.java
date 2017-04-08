package sourabh.quotes.adaptor;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import me.gujun.android.taggroup.TagGroup;
import sourabh.quotes.R;
import sourabh.quotes.data.AuthorQuote;
import sourabh.quotes.data.Quote;
import sourabh.quotes.data.Tag;
import sourabh.quotes.helper.Util;

import static sourabh.quotes.R.id.fab_author;
import static sourabh.quotes.R.id.tv_author;

public class AuthorQuotesDataAdapter extends RecyclerView.Adapter<AuthorQuotesDataAdapter.ViewHolder> {
    private AuthorQuote authorQuotes;
    private String author;
    CardView cardView;
    Context context;
    FloatingActionButton fab_author;
    TagGroup mTagGroup;



    public AuthorQuotesDataAdapter(AuthorQuote authorQuotes,Context context) {
        this.authorQuotes = authorQuotes;
        this.context = context;
    }

    @Override
    public AuthorQuotesDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.author_quote_card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuthorQuotesDataAdapter.ViewHolder viewHolder, int i) {

        Quote quote = authorQuotes.getQuotes().get(i);
        viewHolder.tv_quote.setText(quote.getQuote());
        viewHolder.tv_author.setText("- "+authorQuotes.getAuthor_name());

        int cardviewColor = Util.getRandomColor(context);
        cardView.setBackgroundColor(cardviewColor);

        viewHolder.tv_quote.setTextColor(Util.getContrastColor(cardviewColor));
        viewHolder.tv_author.setTextColor(Util.getContrastColor(cardviewColor));
        fab_author.setBackgroundTintList(ColorStateList.valueOf(cardviewColor));
        fab_author.getDrawable().mutate().setTint(Util.getContrastColor(cardviewColor));


        List<String> tags = new ArrayList<>();
        for (Tag tag:quote.getTags())
        {
            tags.add(tag.getTag_title());
        }
        mTagGroup.setTags(tags);



    }

    @Override
    public int getItemCount() {
        return authorQuotes.getQuotes().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_quote,tv_author;
        public ViewHolder(View view) {
            super(view);

            tv_quote = (TextView)view.findViewById(R.id.tv_quote);
            tv_author = (TextView)view.findViewById(R.id.tv_author);
            cardView = (CardView)view.findViewById(R.id.card);
            fab_author = (FloatingActionButton) view.findViewById(R.id.fab_author);
            mTagGroup = (TagGroup)view.findViewById(R.id.tag_group);

        }
    }

}
