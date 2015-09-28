package dlmj.callup.UI.Fragment.SelfSave;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dlmj.callup.BusinessLogic.Cache.SceneCache;
import dlmj.callup.Common.Factory.BackColorFactory;
import dlmj.callup.Common.Interfaces.ChangeFragmentListener;
import dlmj.callup.Common.Interfaces.DialogListener;
import dlmj.callup.Common.Interfaces.UIDataListener;
import dlmj.callup.Common.Params.CodeParams;
import dlmj.callup.Common.Params.UrlParams;
import dlmj.callup.Common.Model.Bean;
import dlmj.callup.Common.Model.Scene;
import dlmj.callup.BusinessLogic.Network.NetworkHelper;
import dlmj.callup.R;
import dlmj.callup.UI.Activity.MenuActivity;
import dlmj.callup.UI.Adapter.SceneAdapter;
import dlmj.callup.UI.Fragment.CallUpFragment;
import dlmj.callup.UI.View.SetAlarmTimeDialog;

/**
 * Created by Two on 15/8/15.
 */
public class SetSceneFragment extends CallUpFragment {
    private PullToRefreshGridView mSceneGridView;
    private NetworkHelper mGetScenesNetworkHelper;
    Map<String, String> mParams = new HashMap<>();
    private List<Scene> mSceneList = new LinkedList<>();
    private SceneAdapter mSceneAdapter;
    private ChangeFragmentListener mChangeFragmentListener;
    private UIDataListener<Bean> mGetScenesListener;
    private DialogListener mDialogListener;
    private SetAlarmTimeDialog mSetTimeDialog;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_scene, null);
        initializeData();
        findView(view);
        setListener();
        return view;
    }

    public void initializeData() {
        mGetScenesNetworkHelper = new NetworkHelper(this.getActivity());
        SceneCache sceneCache = SceneCache.getInstance();
        if (sceneCache.getList().size() > 0) {
            mSceneList = sceneCache.getList();
        } else {
            mGetScenesNetworkHelper.sendGetRequest(UrlParams.GET_SCENES_URL, mParams);
        }
    }

    public void findView(View view) {
        mMenuButton = (Button) view.findViewById(R.id.menuButton);
        mSceneGridView = (PullToRefreshGridView) view.findViewById(R.id.sceneGridView);
        BackColorFactory backColorFactory = new BackColorFactory(getActivity());
        mSceneAdapter = new SceneAdapter(this.getActivity(), mSceneList, backColorFactory);
        mSceneGridView.setAdapter(mSceneAdapter);
    }

    public void setListener() {
        super.setListener();
        mGetScenesListener = new UIDataListener<Bean>() {
            @Override
            public void onDataChanged(Bean data) {
                try {
                    JSONObject result = new JSONObject(data.getResult());
                    String sceneListStr = result.getString("scene.list");
                    JSONArray sceneList = new JSONArray(sceneListStr);
                    initializeSceneList();
                    String sceneStr;
                    for (int i = 0; i < sceneList.length(); i++) {
                        sceneStr = sceneList.getString(i);
                        JSONObject scene = new JSONObject(sceneStr);
                        mSceneList.add(new Scene(
                                scene.getInt("id"),
                                scene.getString("Name"),
                                scene.getString("Logo"),
                                scene.getString("Picture"),
                                scene.getString("Audio")));
                    }
                    mSceneAdapter.notifyDataSetChanged();
                    SceneCache.getInstance().setList(mSceneList);
                    mSceneGridView.onRefreshComplete();
                } catch (JSONException e) {
                    this.onErrorHappened(CodeParams.ERROR_SAVE_SESSION_TOKEN, e.toString());
                }
            }

            @Override
            public void onErrorHappened(int errorCode, String errorMessage) {

            }
        };

        mGetScenesNetworkHelper.setUiDataListener(mGetScenesListener);

        mSceneGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {
            @Override
            public void onRefresh(PullToRefreshBase<GridView> refreshView) {
                mGetScenesNetworkHelper.sendGetRequest(UrlParams.GET_SCENES_URL, mParams);
            }
        });

        mDialogListener = new DialogListener() {
            @Override
            public void closeDialog() {
                mSetTimeDialog.dismiss();
            }
        };

        mSceneGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Scene scene = mSceneList.get(position);
                mSetTimeDialog = new SetAlarmTimeDialog(getActivity());
                mSetTimeDialog.setAlarmInfo(scene.getSceneId());
                mSetTimeDialog.setDialogListener(mDialogListener);
                mSetTimeDialog.setChangeFragmentListener(mChangeFragmentListener);
                mSetTimeDialog.show();
            }
        });
    }

    private void initializeSceneList() {
        mSceneList.clear();
        mSceneList.add(new Scene(getString(R.string.random)));
    }

}
