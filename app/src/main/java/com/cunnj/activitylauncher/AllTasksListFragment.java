package com.cunnj.activitylauncher;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;

import org.thirdparty.LauncherIconCreator;

public class AllTasksListFragment extends Fragment implements AllTasksListAsyncProvider.Listener<AllTasksListAdapter> {
    protected ExpandableListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_list, null);

        this.list = view.findViewById(R.id.expandableListView1);

        this.list.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ExpandableListAdapter adapter = parent.getExpandableListAdapter();
                MyActivityInfo info = (MyActivityInfo) adapter.getChild(groupPosition, childPosition);
                LauncherIconCreator.launchActivity(getActivity(), info.component_name, info.name);
                return false;
            }
        });

        AllTasksListAsyncProvider provider = new AllTasksListAsyncProvider(this.getActivity(), this);
        provider.execute();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //ExpandableListView list = (ExpandableListView) getView().findViewById(R.id.expandableListView1);
        this.registerForContextMenu(this.list);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {

        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
        ExpandableListView list = getView().findViewById(R.id.expandableListView1);

        switch (ExpandableListView.getPackedPositionType(info.packedPosition)) {
            case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
                MyActivityInfo activity = (MyActivityInfo) list.getExpandableListAdapter().getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition));
                menu.setHeaderIcon(activity.icon);
                menu.setHeaderTitle(activity.name);
                menu.add(Menu.NONE, 0, Menu.NONE, R.string.context_action_shortcut);
                menu.add(Menu.NONE, 1, Menu.NONE, R.string.context_action_launch);
                break;
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
        ExpandableListView list = getView().findViewById(R.id.expandableListView1);

        switch (ExpandableListView.getPackedPositionType(info.packedPosition)) {
            case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
                MyActivityInfo activity = (MyActivityInfo) list.getExpandableListAdapter().getChild(ExpandableListView.getPackedPositionGroup(info.packedPosition), ExpandableListView.getPackedPositionChild(info.packedPosition));
                switch (item.getItemId()) {
                    case 0:
                        DialogFragment dialog = new ShortcutEditDialogFragment();
                        Bundle args = new Bundle();
                        args.putParcelable("activity", activity.component_name);
                        dialog.setArguments(args);
                        dialog.show(this.getFragmentManager(), "ShortcutEditor");
                        break;
                    case 1:
                        LauncherIconCreator.launchActivity(getActivity(), activity.component_name, activity.name);
                        break;
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onProviderFinished(AsyncProvider<AllTasksListAdapter> task, AllTasksListAdapter value) {
        this.list.setAdapter(value);
    }
}
