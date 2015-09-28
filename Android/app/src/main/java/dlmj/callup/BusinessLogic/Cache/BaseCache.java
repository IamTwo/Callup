package dlmj.callup.BusinessLogic.Cache;

import java.util.LinkedList;
import java.util.List;

import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Model.Conversation;

/**
 * Created by Two on 15/9/21.
 */
public class BaseCache<T> {
    protected List<T> mList;

    public BaseCache() {
        mList = new LinkedList<>();
    }

    public List<T> getList(){
        return mList;
    }

    public void setList(List<T> list) {
        mList = list;
    }

    public void addItem(T item) {
        mList.add(item);
    }

    public void removeItem(int id) {
        mList.remove(id);
    }

}
