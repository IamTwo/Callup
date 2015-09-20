package dlmj.callup.Common.Interfaces;

/**
 * Created by Two on 15/8/13.
 */
public interface UIDataListener<T> {
    public void onDataChanged(T data);
    public void onErrorHappened(int errorCode, String errorMessage);
}
