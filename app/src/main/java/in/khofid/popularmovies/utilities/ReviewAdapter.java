package in.khofid.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.khofid.popularmovies.R;

/**
 * Created by ofid on 7/26/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{
    private Context mContext;
    private Review[] mReviews;

    public ReviewAdapter(Context context){
        this.mContext = context;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int id = R.layout.review_item;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(id,parent,false);
        return new ReviewAdapterViewHolder(view);
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_review_author) TextView reviewAuthor;
        @BindView(R.id.tv_review_content) TextView reviewContent;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        Review review = mReviews[position];
        holder.reviewAuthor.setText(review.author);
        holder.reviewContent.setText(Html.fromHtml(review.content));
    }

    @Override
    public int getItemCount() {
        if(null == mReviews) return 0;
        return mReviews.length;
    }

    public void setReviewsData(Review[] reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }
}
