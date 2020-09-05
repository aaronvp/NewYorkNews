package com.example.aaronvp.newyorknews.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.aaronvp.newyorknews.ArticleProcessor;
import com.example.aaronvp.newyorknews.R;
import com.example.aaronvp.newyorknews.model.Article;
import com.example.aaronvp.newyorknews.ui.activity.ArticleDetailActivity;
import com.example.aaronvp.newyorknews.ui.activity.ArticleListActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.aaronvp.newyorknews.ApplicationConstants.DATABASE;
import static com.example.aaronvp.newyorknews.ApplicationConstants.DATABASE_CHILD;
import static com.example.aaronvp.newyorknews.ApplicationConstants.DATABASE_SORT_BY;
import static com.example.aaronvp.newyorknews.ApplicationConstants.LINE_BREAK;

/**
 * A fragment representing a single Article detail screen.
 * This fragment is either contained in a {@link ArticleListActivity}
 * in two-pane mode (on tablets) or a {@link ArticleDetailActivity}
 * on handsets.
 */
public class ArticleDetailFragment extends Fragment {

    private View rootView;
    private ImageView articleImage;
    private TextView articleByLine;
    private TextView articleBody;
    private Toolbar toolbar;
    private LinearLayout metaBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int metaBarColor;
    private FloatingActionButton floatingActionButton;
    private boolean isBookMarked = false;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference dbArticles;
    private Article article;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.article_detail_fragment, container, false);
        collapsingToolbarLayout = rootView.findViewById(R.id.collapsing_toolbar);
        toolbar = rootView.findViewById(R.id.appToolbar);

        if (dbArticles == null) {
            dbArticles = FirebaseDatabase.getInstance().getReference("articles");
        }

        firebaseDatabase = FirebaseDatabase.getInstance();

        if (getActivity() instanceof ArticleDetailActivity) {
            ArticleDetailActivity articleDetailActivity = (ArticleDetailActivity) getActivity();
            articleDetailActivity.setSupportActionBar(toolbar);
        }

        AppBarLayout mAppBarLayout = rootView.findViewById(R.id.toolbar_container);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    // articleDetailActivity.showOption(R.id.action_info);
                    toolbar.setBackgroundColor(metaBarColor);
                } else if (isShow) {
                    isShow = false;
                    // articleDetailActivity.hideOption(R.id.action_info);
                    toolbar.getBackground().setAlpha(0);
                }
            }
        });

        articleImage = rootView.findViewById(R.id.articleDetailPhoto);
        articleByLine = rootView.findViewById(R.id.article_byline);
        articleBody = rootView.findViewById(R.id.article_body);
        metaBar = rootView.findViewById(R.id.meta_bar);
        floatingActionButton = rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(v -> {
            if (isBookMarked) {
                dbArticles.child(getArticleId(article)).removeValue();
                isBookMarked = false;
            } else {
                dbArticles.child(getArticleId(article)).setValue(article);
                isBookMarked = true;
            }
            floatingActionButton.setSelected(isBookMarked);
        });

        rootView.setAlpha(0);
        rootView.setVisibility(View.VISIBLE);
        rootView.animate().alpha(1);
        this.rootView = rootView;
        return rootView;
    }

    /**
     * Set the Article Data
     * @param article article
     */
    public void setArticleData(Article article) {
        this.article = article;
        isArticleBookmarked(article);
        String image = ArticleProcessor.getArticleImageURL(article);

        // Load article Image
        Glide.with(rootView)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.newspaper)
                .error(R.drawable.newspaper)
                .into(articleImage);

        // Convert Image into Bitmap to determine appropriate text color to use
        Glide.with(rootView)
                .asBitmap()
                .load(image)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        setTextColorForImage(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        collapsingToolbarLayout.setTitle(article.getHeadline().getMain());
        articleByLine.setText(article.getByLine().getOriginal());
        String bodyText = article.getLeadParagraph().concat(LINE_BREAK).concat(LINE_BREAK).concat(article.getSnippet());
        articleBody.setText(bodyText);
    }


    private void setTextColorForImage(Bitmap firstPhoto) {
        Palette.from(firstPhoto)
                .generate(palette -> {
                    if (palette != null) {
                        metaBarColor = palette.getDominantColor(Color.WHITE);
                        metaBar.setBackgroundColor(metaBarColor);
                    }
                });
    }

    private void isArticleBookmarked(Article article) {
        DatabaseReference getDbArticles = firebaseDatabase.getReference(DATABASE_CHILD);
        getDbArticles.orderByChild(DATABASE_SORT_BY).equalTo(article.getArticleId())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.i(DATABASE, "Bookmarked Article " + snapshot.getKey());
                        isBookMarked = true;
                        floatingActionButton.setSelected(true);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private String getArticleId(Article article) {
        return article.getArticleId().substring(14);
    }


}