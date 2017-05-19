package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;
import edu.sjsu.cmpe.fourhorsemen.connectivity.beans.Post;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    //Array of dummy posts
    public static final List<Post> POSTS = new ArrayList<Post>();

    private static int userPhotoResIDs[] = {R.drawable.emma, R.drawable.lavery, R.drawable.lillie};
    private static String userScreenNames[] = {"Jane Doe", "Emma Watson", "Dr. Ford"};

    /**
     * A map of posts, by ID.
     */
    public static final Map<Integer, Post> POST_MAP = new HashMap<Integer, Post>();

    private static final int COUNT = 20;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyPost(i));
        }
    }

    private static void addItem(Post post) {
        POSTS.add(post);
        POST_MAP.put(post.getPostID(), post);
    }

    private static Post createDummyPost(int position) {
        String time = (int)(Math.random() * 23) + ":" + (int)(Math.random() * 59);
        return new Post(position,userScreenNames[(int) (Math.random() * 3)], userPhotoResIDs[(int)(Math.random() * 3)], "This is a sample post to test the layout of the post card.This is a sample post to test the layout of the post card.This is a sample post to test the layout of the post card.This is a sample post to test the layout of the post card.", time);
    }

    private static String generateRandomContent(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("This post is about ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

}
