package edu.sjsu.cmpe.fourhorsemen.connectivity.fragments.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.sjsu.cmpe.fourhorsemen.connectivity.R;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    //Array of dummy posts
    public static final List<DummyPost> POSTS = new ArrayList<DummyPost>();

    private static int userPhotoResIDs[] = {R.drawable.emma, R.drawable.lavery, R.drawable.lillie};
    private static String userNames[] = {"Jane Doe", "Emma Watson", "Dr. Ford"};

    /**
     * A map of posts, by ID.
     */
    public static final Map<Integer, DummyPost> POST_MAP = new HashMap<Integer, DummyPost>();

    private static final int COUNT = 20;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyPost(i));
        }
    }

    private static void addItem(DummyPost post) {
        POSTS.add(post);
        POST_MAP.put(post.postId, post);
    }

    private static DummyPost createDummyPost(int position) {
        return new DummyPost(position, userPhotoResIDs[(int) (Math.random()*3)], userNames[(int) (Math.random()*3)], generateRandomContent(position));
    }

    private static String generateRandomContent(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("This post is about ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyPost {
        public final int postId;
        public final String postContent;
        public final String userName;
        public final int userPhotoResID;

        public DummyPost(int postId, int userPhotoResID, String userName, String postContent) {
            this.postId = postId;
            this.postContent = postContent;
            this.userName = userName;
            this.userPhotoResID = userPhotoResID;
        }
    }
}
