package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.activities.CreateNewPostActivity;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Post;
import edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.PostFragment.OnListFragmentInteractionListener;
import edu.sjsu.cmpe.fourhorsemen.connectivity.utilities.PreferenceHandler;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Post} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MySearchedFriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = MyPostRecyclerViewAdapter.class.getSimpleName();

    private static final int CREATE_POST_CARD = 0;
    private static final int POST_CARD = 1;
    private final List<Post> posts;
    private final OnListFragmentInteractionListener mListener;

    public MySearchedFriendsAdapter(List<Post> posts, OnListFragmentInteractionListener listener) {
        this.posts = posts;
        mListener = listener;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView userName;
        TextView timestamp;
        TextView postContent;
        ImageView userPhoto;

        PostViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_post);
            userName = (TextView)itemView.findViewById(R.id.sn_label);
            timestamp = (TextView)itemView.findViewById(R.id.timestamp);
            postContent = (TextView)itemView.findViewById(R.id.post_content);
            userPhoto = (ImageView)itemView.findViewById(R.id.user_photo);
        }
    }

    public class CreatePostViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        EditText newPostContent;
        ImageView userPhoto;
        Context context;

        CreatePostViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = (CardView)itemView.findViewById(R.id.cv_post);
            userPhoto = (ImageView)itemView.findViewById(R.id.user_photo);
            newPostContent = (EditText)itemView.findViewById(R.id.new_post_content);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if(viewType == POST_CARD){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_post, parent, false);
            return new PostViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_do_post, parent, false);
            return new CreatePostViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        byte[] decodedString;
        switch (getItemViewType(position)){

            case POST_CARD:
                PostViewHolder pvholder = (PostViewHolder) holder;
                pvholder.userName.setText(posts.get(position-1).getFrom().getScreen_name());
                pvholder.timestamp.setText(posts.get(position-1).getTimestamp());
                pvholder.postContent.setText(posts.get(position-1).getContent());
                decodedString = Base64.decode(posts.get(position-1).getFrom().getProfile_pic(), Base64.DEFAULT);
                pvholder.userPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));

//                pvholder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (null != mListener) {
//                            // Notify the active callbacks interface (the activity, if the
//                            // fragment is attached to one) that an item has been selected.
//                            Log.i(TAG, String.valueOf(v.getId()));
//                            Log.i(TAG, String.valueOf(R.id.new_post_content));
//                            mListener.onListFragmentInteraction();
//                        }
//                    }
//                });

                break;
            case CREATE_POST_CARD:
                final CreatePostViewHolder cpvholder = (CreatePostViewHolder) holder;
                decodedString = Base64.decode(PreferenceHandler.getProfilePic(), Base64.DEFAULT);
                cpvholder.userPhoto.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                cpvholder.newPostContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(cpvholder.context, CreateNewPostActivity.class);
                        //Intent intent = new Intent(cpvholder.context, OtherUserProfileActivity.class);
                        cpvholder.context.startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return posts.size() + 1;
    }

    @Override
    public int getItemViewType(int pos){
        if(pos == 0)
            return CREATE_POST_CARD;
        else
            return POST_CARD;
    }
}
