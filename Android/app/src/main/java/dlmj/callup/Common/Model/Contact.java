package dlmj.callup.Common.Model;

/**
 * Created by Two on 15/9/8.
 */
public class Contact {
    private String mAccount;
    private String mNickName;

    private String mToken;

    public Contact(String account) {
        mAccount = account;
    }

    public String getNickName() {
        return mNickName;
    }

}
