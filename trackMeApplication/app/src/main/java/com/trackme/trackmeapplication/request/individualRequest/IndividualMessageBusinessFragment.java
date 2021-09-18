package com.trackme.trackmeapplication.request.individualRequest;

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

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseFragment;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.Settings;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.request.RequestStatus;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkIInterface;
import com.trackme.trackmeapplication.request.individualRequest.network.IndividualRequestNetworkImp;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkImp;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * Individual message business fragment handles the individual request sent by the third party. It shows all the request
 * in a recyclerView and it allows to the third party to create some one new.
 *
 * @author Mattia Tibaldi
 * @see BaseFragment
 */
public class IndividualMessageBusinessFragment extends BaseFragment {

    @BindView(R.id.listView)
    protected RecyclerView recyclerView;

    private IndividualMessageBusinessFragment.CustomRecyclerView customRecyclerView;
    private List<IndividualRequestWrapper> individualRequestWrappers = new ArrayList<>();

    private Handler handler;
    private Runnable checkNewRequest;
    private IndividualRequestNetworkIInterface individualrequestNetwork;
    private String token;


    /**
     * Custom recyclerView class for showing the individualRequestItem in the recycler.
     */
    private class CustomRecyclerView extends RecyclerView.Adapter<IndividualMessageBusinessFragment.CustomRecyclerView.MyViewHolder> {

        /**
         * The holder that searches the object in the layout and binds it.
         */
        class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView ssn;
            private TextView status;
            private ImageView download;

            /**
             * Constructor.
             *
             * @param view current view.
             */
            MyViewHolder(View view) {
                super(view);
                ssn = view.findViewById(R.id.textViewSsn);
                status = view.findViewById(R.id.textViewStatus);
                download = view.findViewById(R.id.download_request_data);
            }
        }

        private Activity context;
        private List<IndividualRequestWrapper> items;

        /**
         * Constructor.
         *
         * @param individualRequestWrappers list of item to show in the recyclerView.
         */
        CustomRecyclerView(Activity context, List<IndividualRequestWrapper> individualRequestWrappers) {
            this.context = context;
            this.items = individualRequestWrappers;
        }

        @NonNull
        @Override
        public IndividualMessageBusinessFragment.CustomRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_request_listview_layout, parent, false);
            return new IndividualMessageBusinessFragment.CustomRecyclerView.MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull IndividualMessageBusinessFragment.CustomRecyclerView.MyViewHolder holder, final int position) {
            holder.status.setText(items.get(position).getStatus().name());
            holder.status.setTextColor(items.get(position).getStatus().getColor());
            holder.ssn.setText(items.get(position).getUserSsn());

            if (items.get(position).getStatus() == RequestStatus.ACCEPTED){
                holder.download.setVisibility(View.VISIBLE);

                SharedDataNetworkInterface sharedDataNetwork = SharedDataNetworkImp.getInstance();

                holder.download.setOnClickListener(view -> {
                    try {
                        generateNoteOnSD(Constant.REQUEST_FOLDER_NAME,
                                items.get(position).getUserSsn() + " " + items.get(position).getTimestamp(),
                                sharedDataNetwork.getIndividualRequestData(token, items.get(position).extractResponseLink()));
                        showMessage(getString(R.string.download_file));
                    } catch (ConnectionException e) {
                        showMessage(getString(R.string.download_file_error));
                    }
                });
            }

            holder.ssn.setOnClickListener(view -> {
                Intent intent = new Intent(context, IndividualRequestBusinessBodyActivity.class);
                intent.putExtra(Constant.SD_INDIVIDUAL_REQUEST_KEY, items.get(position));
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

        customRecyclerView = new CustomRecyclerView(getActivity(), individualRequestWrappers);
        recyclerView.setAdapter(customRecyclerView);

        individualrequestNetwork = IndividualRequestNetworkImp.getInstance();
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
        Intent intent = new Intent(getActivity(), RequestFormActivity.class);
        startActivity(intent);
    }

    /**
     * Refresh the recyclerView when it changes.
     */
    private void refreshList() {
        individualRequestWrappers.clear();
        try {
            individualRequestWrappers.addAll(individualrequestNetwork.getIndividualRequest(token));
        } catch (ConnectionException e) {
            if (this.isAdded())
                showMessage(getString(R.string.connection_error));
        }
        customRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(checkNewRequest);
        super.onDestroy();
    }

}
