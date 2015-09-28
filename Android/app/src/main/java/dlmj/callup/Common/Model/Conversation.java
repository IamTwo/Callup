package dlmj.callup.Common.Model;

/**
 * Created by Two on 15/9/21.
 */
public class Conversation {
    private int mFriendUserId;

    private String mLastMessage;

    public Conversation(int friendUserId, String lastMessage) {
        mFriendUserId = friendUserId;
        mLastMessage = lastMessage;
    }

    public int getFriendUserId() {
        return mFriendUserId;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public void setLastMessage(String lastMessage) {
        mLastMessage = lastMessage;
    }
}
