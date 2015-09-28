package dlmj.callup.BusinessLogic.Cache;

import dlmj.callup.Common.Model.Conversation;

/**
 * Created by Two on 15/9/21.
 */
public class ConversationCache extends BaseCache<Conversation>{
    private static ConversationCache mInstance;

    public ConversationCache(){
        super();
    }

    public static ConversationCache getInstance(){
        synchronized (ConversationCache.class){
            if(mInstance == null){
                mInstance = new ConversationCache();
            }
        }
        return mInstance;
    }

    public void updateConversation(Conversation conversation) {
        for(Conversation item : mList) {
            if(item.getFriendUserId() == conversation.getFriendUserId()) {
                item.setLastMessage(conversation.getLastMessage());
                mList.remove(item);
                break;
            }
        }

        mList.add(conversation);
    }
}
