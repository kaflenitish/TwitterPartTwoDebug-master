package com.codepath.apps.restclienttemplate.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.supportLibs.GlideApp;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.supportLibs.TimeFormatter;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TweetAdaptor extends  RecyclerView.Adapter<TweetAdaptor.ViewHolder> {

    Context context;
    ArrayList<Tweet> tweets;

    public TweetAdaptor(Context context, ArrayList<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);

        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear()
    {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> tweets)
    {
        this.tweets.addAll(tweets);
        notifyDataSetChanged();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfilePic, ivMedia;
        TextView tvUsername,tvBody,tvRelAge;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvUsername = itemView.findViewById(R.id.tvName);
            tvRelAge = itemView.findViewById(R.id.tvRelAge);
            tvBody = itemView.findViewById(R.id.rvTweetItem);

        }

        public void bind(Tweet tweet)
        {
            tvRelAge.setText(TimeFormatter.getTimeDifference(tweet.timestamp));
            tvBody.setText(tweet.body);
            tvUsername.setText(tweet.user.name);
            GlideApp.with(context).load(tweet.user.profilePicture).override(400,400).apply(RequestOptions.circleCropTransform()).into(ivProfilePic);
        }
    }

}
