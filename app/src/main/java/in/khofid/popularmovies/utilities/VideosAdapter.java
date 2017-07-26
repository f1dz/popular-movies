package in.khofid.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.khofid.popularmovies.R;

/**
 * Created by ofid on 7/25/17.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosAdapterViewHolder> {

    private Context mContext;
    private Video[] mVideos;

    final private VideosAdapterOnClickHandler mClickHandler;

    public interface VideosAdapterOnClickHandler{
        void OnClick(Video video);
    }

    public VideosAdapter(VideosAdapterOnClickHandler clickHandler) {
        this.mContext = (Context) clickHandler;
        this.mClickHandler = clickHandler;
    }

    @Override
    public VideosAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int id = R.layout.video_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(id, parent, false);
        return new VideosAdapterViewHolder(view);
    }

    class VideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.video_image) ImageView videoImage;
        @BindView(R.id.tv_video_title) TextView videoTitle;

        VideosAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Video video = mVideos[pos];
            mClickHandler.OnClick(video);
        }
    }

    @Override
    public void onBindViewHolder(VideosAdapter.VideosAdapterViewHolder holder, int pos) {
        Video entry = mVideos[pos];
        Picasso.with(mContext)
                .load(entry.img_path)
                .placeholder(R.drawable.movie_icon)
                .resize(320, 180)
                .into(holder.videoImage);
        holder.videoTitle.setText(entry.name);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(null == mVideos) return 0;
        return mVideos.length;
    }

    public void setVideosData(Video[] videos){
        mVideos = videos;
        notifyDataSetChanged();
    }
}
