package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context context;
    List<Tweet> tweet_feed;

    public TweetsAdapter(Context context, List<Tweet> tweet_feed) {
        this.context = context;
        this.tweet_feed = tweet_feed;
    }

    // for each row, inflate its layout
    @NonNull @NotNull @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false));
    }

    // bind values based on element's position
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(tweet_feed.get(position));
    }

    @Override
    public int getItemCount() {
        return tweet_feed.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_photo;
        TextView tweet_content, user_handle;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            profile_photo = itemView.findViewById(R.id.profile_photo);
            tweet_content = itemView.findViewById(R.id.tweet_content);
            user_handle = itemView.findViewById(R.id.user_handle);
        }

        public void bind(Tweet tweet) {
            tweet_content.setText(tweet.getContent());
            user_handle.setText(tweet.getUser().getHandle());
            Glide.with(context).load(tweet.getUser().getProfile_photo_url()).into(profile_photo);
        }
    }
}
