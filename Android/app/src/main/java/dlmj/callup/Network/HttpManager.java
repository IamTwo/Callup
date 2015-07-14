package dlmj.callup.Network;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

/**
 * Created by apple on 15/7/14.
 */
public class HttpManager {
    public String postAPI(String url, List<NameValuePair> params){
        HttpPost httpPost = new HttpPost(url);

        return "";
    }
}
