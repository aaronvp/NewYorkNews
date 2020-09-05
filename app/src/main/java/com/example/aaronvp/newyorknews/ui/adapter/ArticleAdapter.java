package com.example.aaronvp.newyorknews.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.aaronvp.newyorknews.ArticleProcessor;
import com.example.aaronvp.newyorknews.R;
import com.example.aaronvp.newyorknews.model.Article;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import static com.example.aaronvp.newyorknews.ApplicationConstants.ARTICLE_DATE_FORMAT;
import static com.example.aaronvp.newyorknews.ApplicationConstants.ARTICLE_DATE_HOUR_CUTOFF;
import static com.example.aaronvp.newyorknews.ApplicationConstants.ARTICLE_DATE_ICON_PADDING;
import static com.example.aaronvp.newyorknews.ApplicationConstants.ARTICLE_DATE_MINUTE_CUTOFF;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    Context context;
    private List<Article> articleList;
    private boolean splitScreen;

    private final ListItemClickListener listItemClickListener;

    public ArticleAdapter(ListItemClickListener listItemClickListener, boolean splitScreen) {
        this.listItemClickListener = listItemClickListener;
        this.splitScreen = splitScreen;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.article_card, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int position) {
        Article article = articleList.get(position);
        articleViewHolder.bind(article);

        if (splitScreen) {
            int selectedPosition = 0;
            if (position == selectedPosition) {
                articleViewHolder.itemView.setSelected(true);
            } else {
                articleViewHolder.itemView.setSelected(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (articleList != null) ? articleList.size() : 0;
    }

    /**
     * Caching of the children views for a RecipeStep item
     */
    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView ivArticleImage;
        final TextView tvArticleHeadline;
        final TextView tvArticleDate;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            ivArticleImage = itemView.findViewById(R.id.image_article);
            tvArticleHeadline = itemView.findViewById(R.id.article_headline);
            tvArticleDate = itemView.findViewById(R.id.article_date);
            itemView.setOnClickListener(this);
        }

        void bind(Article article) {
            String imageURL = ArticleProcessor.getArticleImageURL(article);
            Glide.with(this.itemView)
                    .load(imageURL)
                    .centerCrop()
                    .placeholder(R.drawable.newspaper)
                    .error(R.drawable.newspaper)
                    .into(ivArticleImage);
            tvArticleHeadline.setText(article.getHeadline().getMain());
            tvArticleDate.setText(getDateText(article.getPubDate()));
            tvArticleDate.setCompoundDrawablesWithIntrinsicBounds
                    (R.drawable.ic_baseline_time_16, 0, 0, 0);
            tvArticleDate.setCompoundDrawablePadding(ARTICLE_DATE_ICON_PADDING);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickedPosition);
        }
    }

    public void setArticleData(List<Article> articleList) {
        this.articleList = articleList;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * Get the Article Date information in local time
     *
     * @param publishedDate publishedDate
     * @return String date representation
     */
    private String getDateText(String publishedDate) {
        try {
            DateTime articleDateTime = DateTime.parse(publishedDate);
            LocalDateTime publishedDateLocalTime = articleDateTime.
                    withZone(DateTimeZone.getDefault()).toLocalDateTime();

            int minutes = Minutes.minutesBetween(publishedDateLocalTime, LocalDateTime.now()).getMinutes();
            if (minutes < ARTICLE_DATE_MINUTE_CUTOFF) {
                return String.valueOf(minutes).concat(context.getString(R.string.article_time_min));
            } else {
                int hours = Hours.hoursBetween(publishedDateLocalTime, LocalDateTime.now()).getHours();
                if (hours < ARTICLE_DATE_HOUR_CUTOFF) {
                    return String.valueOf(hours).concat(context.getString(R.string.article_time_hour));
                } else {
                    DateTimeFormatter fmt = DateTimeFormat.forPattern(ARTICLE_DATE_FORMAT);
                    return publishedDateLocalTime.toString(fmt);
                }
            }
        } catch (Exception e) {
            Log.e("Date Parsing", Log.getStackTraceString(e));
        }
        return publishedDate;
    }

}
