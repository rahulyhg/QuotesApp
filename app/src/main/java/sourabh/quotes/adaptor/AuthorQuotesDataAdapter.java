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

import butterknife.Bind;
import sourabh.quotes.R;
import sourabh.quotes.helper.Util;

import static sourabh.quotes.R.id.card;
import static sourabh.quotes.R.id.fab_author;
import static sourabh.quotes.R.id.tv_author;

public class AuthorQuotesDataAdapter extends RecyclerView.Adapter<AuthorQuotesDataAdapter.ViewHolder> {
    private ArrayList<String> quotes;
    private String author;
    CardView cardView;
    Context context;
    FloatingActionButton fab_author;


    public AuthorQuotesDataAdapter(ArrayList<String> quotes, String author, Context context) {
        this.quotes = quotes;
        this.author = author;

        this.context = context;
    }

    @Override
    public AuthorQuotesDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.author_quote_card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuthorQuotesDataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.tv_quote.setText(quotes.get(i));
        viewHolder.tv_author.setText("- "+author);

        int cardviewColor = Util.getRandomColor(context);
        cardView.setBackgroundColor(cardviewColor);

        viewHolder.tv_quote.setTextColor(Util.getContrastColor(cardviewColor));
        viewHolder.tv_author.setTextColor(Util.getContrastColor(cardviewColor));
fab_author.setBackgroundTintList(ColorStateList.valueOf(cardviewColor));
        fab_author.getDrawable().mutate().setTint(Util.getContrastColor(cardviewColor));
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_quote,tv_author;
        public ViewHolder(View view) {
            super(view);

            tv_quote = (TextView)view.findViewById(R.id.tv_quote);
            tv_author = (TextView)view.findViewById(R.id.tv_author);
            cardView = (CardView)view.findViewById(R.id.card);
            fab_author = (FloatingActionButton) view.findViewById(R.id.fab_author);

        }
    }

}
