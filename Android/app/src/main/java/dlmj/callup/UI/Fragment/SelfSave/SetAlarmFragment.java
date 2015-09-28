package dlmj.callup.UI.Fragment.SelfSave;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dlmj.callup.BusinessLogic.Cache.AlarmCache;
import dlmj.callup.Common.Factory.BackColorFactory;
import dlmj.callup.Common.Factory.FragmentFactory;
import dlmj.callup.Common.Factory.FrequentFactory;
import dlmj.callup.Common.Interfaces.ChangeFragmentListener;
import dlmj.callup.Common.Interfaces.DialogListener;
import dlmj.callup.Common.Interfaces.UIDataListener;
import dlmj.callup.Common.Params.UrlParams;
import dlmj.callup.Common.Model.Alarm;
import dlmj.callup.Common.Model.Bean;
import dlmj.callup.BusinessLogic.Network.NetworkHelper;
import dlmj.callup.Common.Util.LogUtil;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.MenuActivity;
import dlmj.callup.UI.Adapter.AlarmAdapter;
import dlmj.callup.UI.Fragment.CallUpFragment;
import dlmj.callup.UI.View.SetAlarmTimeDialog;

/**
 * Created by Two on 15/8/23.
 */
public class SetAlarmFragment extends CallUpFragment{
    private final static String TAG = "SetAlarmFragment";
    private PullToRefreshListView mPullToRefreshListView;
    private Button mAddButton;
    private AlarmAdapter mAlarmAdapter;
    private List<Alarm> mAlarmList = new LinkedList<>();
    private ChangeFragmentListener mChangeFragmentListener;
    private DialogListener mDialogListener;
    private BackColorFactory mBackColorFactory;
    private FrequentFactory mFrequentFactory;
    private SetAlarmTimeDialog mSetAlarmTimeDialog;

    private NetworkHelper mDeleteAlarmNetworkHelper;
    private NetworkHelper mGetAlarmsNetworkHelper;
    private AdapterView.AdapterContextMenuInfo mSelectedMenuInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_alarm, null);
        initializeData();
        findView(view);
        setListener();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mChangeFragmentListener = ((MenuActivity) activity).changeFragmentListener;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement changeFragmentListener");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                   ContextMenu.ContextMenuInfo menuInfo) {
        Log.v(TAG, "populate context menu");
        menu.setHeaderTitle(R.string.alarm_operation);
        menu.add(0, 1, Menu.NONE, R.string.delete);
        mSelectedMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(menuInfo == null) {
            menuInfo = mSelectedMenuInfo;
        }
        int selectedPosition = menuInfo.position - 1;
        Log.v(TAG, "The ID of the selected item is "+ selectedPosition);

        switch(item.getItemId()) {
            case 1:
                Map<String, String> params = new HashMap<>();
                params.put("id", mAlarmList.get(selectedPosition).getAlarmId() + "");
                mDeleteAlarmNetworkHelper.sendPostRequest(UrlParams.DELETE_ALARM_URL, params);
                mAlarmList.remove(selectedPosition);
                mAlarmAdapter.notifyDataSetChanged();
            break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private void findView(View view) {
        mMenuButton = (Button) view.findViewById(R.id.menuButton);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pullRefreshList);
        ListView actualListView = mPullToRefreshListView.getRefreshableView();
        mAddButton = (Button) view.findViewById(R.id.addAlarmButton);
        mAlarmAdapter = new AlarmAdapter(this.getActivity(), mAlarmList,
                mBackColorFactory, mFrequentFactory);
        actualListView.setAdapter(mAlarmAdapter);
        mSetAlarmTimeDialog = new SetAlarmTimeDialog(getActivity());
    }

    public void setListener() {
        super.setListener();
        this.registerForContextMenu(mPullToRefreshListView.getRefreshableView());

        mSetAlarmTimeDialog.setChangeFragmentListener(mChangeFragmentListener);

        mDialogListener = new DialogListener() {
            @Override
            public void closeDialog() {
                mSetAlarmTimeDialog.dismiss();
            }
        };
//        mSetAlarmTimeDialog.setChangeFragmentListener(new ChangeFragmentListener() {
//            @Override
//            public void ChangeFragment(FragmentFactory.FragmentName fragmentName) {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, ).commit();
//            }
//        });

        mGetAlarmsNetworkHelper.setUiDataListener(new UIDataListener<Bean>() {

            @Override
            public void onDataChanged(Bean data) {
                try{
                    JSONObject result = new JSONObject(data.getResult());
                    String alarmListStr = result.getString("alarm.list");
                    JSONArray alarmList = new JSONArray(alarmListStr);
                    mAlarmList.clear();
                    String alarmStr;
                    for (int i = 0; i < alarmList.length(); i++) {
                        alarmStr = alarmList.getString(i);
                        JSONObject alarm = new JSONObject(alarmStr);
                        mAlarmList.add(new Alarm(
                                alarm.getInt("id"),
                                alarm.getInt("Scene_id"),
                                alarm.getString("Logo"),
                                alarm.getString("Picture"),
                                alarm.getString("Name"),
                                alarm.getString("Time"),
                                alarm.getString("frequent_type"),
                                alarm.getString("Audio")));
                    }
                    mAlarmAdapter.notifyDataSetChanged();
                    AlarmCache.getInstance().setList(mAlarmList);
                    Log.d("result", result.toString());
                    mPullToRefreshListView.onRefreshComplete();
                }catch(JSONException ex) {
                    LogUtil.e(TAG, ex.getMessage());
                }
            }

            @Override
            public void onErrorHappened(int errorCode, String errorMessage) {

            }
        });

        mDeleteAlarmNetworkHelper.setUiDataListener(new UIDataListener<Bean>() {
            @Override
            public void onDataChanged(Bean data) {
            }

            @Override
            public void onErrorHappened(int errorCode, String errorMessage) {
            }
        });

        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                Map<String, String> params = new HashMap<>();
                mGetAlarmsNetworkHelper.sendGetRequest(UrlParams.GET_ALARMS_URL, params);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeFragmentListener.ChangeFragment(FragmentFactory.FragmentName.SetScene);
            }
        });

        mPullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Alarm alarm = mAlarmList.get(position - 1);
                mSetAlarmTimeDialog.setAlarmInfo(alarm.getSceneId(),
                        alarm.getTime(), alarm.getFrequent(), alarm.getAlarmId());
                mSetAlarmTimeDialog.setDialogListener(mDialogListener);
                mSetAlarmTimeDialog.show();
            }
        });
    }

    private void initializeData() {
        mGetAlarmsNetworkHelper = new NetworkHelper(this.getActivity());
        mDeleteAlarmNetworkHelper = new NetworkHelper(this.getActivity());

        mBackColorFactory = new BackColorFactory(getActivity());
        mFrequentFactory = new FrequentFactory(getActivity());
        AlarmCache alarmCache = AlarmCache.getInstance();
        if (alarmCache.getList().size() > 0) {
            mAlarmList = alarmCache.getList();
        } else {
            Map<String, String> params = new HashMap<>();
            mGetAlarmsNetworkHelper.sendGetRequest(UrlParams.GET_ALARMS_URL, params);
        }
    }

}
