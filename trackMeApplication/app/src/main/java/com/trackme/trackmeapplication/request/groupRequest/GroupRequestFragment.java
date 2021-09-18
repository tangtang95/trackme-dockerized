package com.trackme.trackmeapplication.request.groupRequest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseFragment;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.Settings;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.RequestStatus;
import com.trackme.trackmeapplication.request.groupRequest.network.GroupRequestNetworkImp;
import com.trackme.trackmeapplication.request.groupRequest.network.GroupRequestNetworkInterface;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkImp;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * Group request fragment handles the group request sent by the third party. It shows all the request
 * in a recyclerView and it allows to the third party to create some one new.
 *
 * @author Mattia Tibaldi
 * @see BaseFragment
 */
public class GroupRequestFragment extends BaseFragment {

    @BindView(R.id.listView)
    protected RecyclerView recyclerView;

    private GroupRequestFragment.CustomRecyclerView customRecyclerView;
    private List<GroupRequestWrapper> groupRequestWrappers = new ArrayList<>();

    private Handler handler;
    private Thread checkNewRequest;
    private String token;
    GroupRequestNetworkInterface groupRequestNetwork;

    /**
     * Custom recyclerView class for showing the groupRequestItem in the recycler.
     */
    private class CustomRecyclerView extends RecyclerView.Adapter<GroupRequestFragment.CustomRecyclerView.MyViewHolder> {

        /**
         * The holder that searches the object in the layout and binds it.
         */
        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView status;
            private TextView receiver;
            private ImageView download;

            /**
             * Constructor.
             *
             * @param view current view.
             */
            MyViewHolder(View view) {
                super(view);
                status = view.findViewById(R.id.textViewStatus);
                receiver = view.findViewById(R.id.textViewSsn);
                download = view.findViewById(R.id.download_request_data);
            }
        }

        private Activity context;
        private List<GroupRequestWrapper> items;

        /**
         * Constructor.
         *
         * @param requestItems list of item to show in the recyclerView.
         */
        CustomRecyclerView(Activity context, List<GroupRequestWrapper> requestItems) {
            this.context = context;
            this.items = requestItems;
        }

        @NonNull
        @Override
        public GroupRequestFragment.CustomRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_request_listview_layout, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupRequestFragment.CustomRecyclerView.MyViewHolder holder, final int position) {
            holder.status.setText(items.get(position).getStatus().name());
            holder.status.setTextColor(items.get(position).getStatus().getColor());
            holder.receiver.setText(getText(R.string.app_name));

            if (items.get(position).getStatus() == RequestStatus.ACCEPTED){
                holder.download.setVisibility(View.VISIBLE);

                SharedDataNetworkInterface sharedDataNetwork = SharedDataNetworkImp.getInstance();

                holder.download.setOnClickListener(view -> {
                    try {
                        generateNoteOnSD(Constant.REQUEST_FOLDER_NAME,
                                getString(R.string.app_name) + " " + items.get(position).getCreationTimestamp(),
                                sharedDataNetwork.getGroupRequestData(token, items.get(position).extractGroupRequestLink()));
                        showMessage(getString(R.string.download_file));
                    } catch (ConnectionException e) {
                        Toast.makeText(getmContext(), getString(R.string.download_file_error), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            holder.receiver.setOnClickListener(view -> {
                Intent intent = new Intent(context, GroupRequestBodyActivity.class);
                intent.putExtra(Constant.SD_GROUP_REQUEST_KEY, items.get(position));
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_business_message;
    }

    /**
     * This method setUp the layout and create a thread in order to periodically
     * refresh the list of items.
     */
    @Override
    protected void setUpFragment() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        customRecyclerView = new CustomRecyclerView(getActivity(), groupRequestWrappers);
        recyclerView.setAdapter(customRecyclerView);

        groupRequestNetwork = GroupRequestNetworkImp.getInstance();
        SharedPreferences sp = getmContext().getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, MODE_PRIVATE);
        token = sp.getString(Constant.SD_BUSINESS_TOKEN_KEY, null);

        handler = new Handler();
        checkNewRequest = new Thread() {
            @Override
            public void run() {
                refreshList();
                handler.postDelayed(this, Settings.getRefreshItemTime());
            }
        };
        handler.post(checkNewRequest);
    }


    /**
     * Handle the add individual request click event.
     */
    @OnClick(R.id.add_individual_request)
    public void onAddIndividualRequestClick(){
        Intent intent = new Intent(getActivity(), GroupRequestFormActivity.class);
        startActivity(intent);
    }

    /**
     * Refresh the recyclerView when it changes.
     */
    private void refreshList() {
        groupRequestWrappers.clear();
        try {
            groupRequestWrappers.addAll(groupRequestNetwork.getGroupRequest(token));
        } catch (ConnectionException e) {
            if (this.isAdded())
                Toast.makeText(getmContext(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
        customRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(checkNewRequest);
        super.onDestroy();
    }
}
