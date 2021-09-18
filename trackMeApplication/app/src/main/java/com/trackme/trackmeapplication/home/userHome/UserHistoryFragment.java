package com.trackme.trackmeapplication.home.userHome;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseFragment;
import com.trackme.trackmeapplication.baseUtility.Constant;
import com.trackme.trackmeapplication.httpConnection.Settings;
import com.trackme.trackmeapplication.httpConnection.exception.ConnectionException;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkImp;
import com.trackme.trackmeapplication.sharedData.network.SharedDataNetworkInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static android.content.Context.MODE_PRIVATE;

/**
 * User history fragment handles the data get from the application. It shows the last feasible
 * data in a recyclerView.
 *
 * @author Mattia Tibaldi
 * @see BaseFragment
 */
public class UserHistoryFragment extends BaseFragment {

    @BindView(R.id.listView)
    protected RecyclerView recyclerView;

    private CustomRecyclerView customRecyclerView;
    private List<HistoryItem> historyItems = new ArrayList<>();

    private Handler handler;
    private Runnable checkNewHistoryItem;

    /**
     * Custom recyclerView class for showing the historyItem in the recycler.
     */
    private class CustomRecyclerView extends RecyclerView.Adapter<CustomRecyclerView.MyViewHolder> {

        /**
         * The holder that searches the object in the layout and binds it.
         */
        class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView date;
            private TextView info;

            /**
             * Constructor.
             *
             * @param view current view.
             */
            MyViewHolder(View view) {
                super(view);
                date = view.findViewById(R.id.textViewDate);
                info = view.findViewById(R.id.textViewInfo);
            }
        }

        private List<HistoryItem> items;

        /**
         * Constructor.
         *
         * @param historyItems list of item to show.
         */
        CustomRecyclerView(List<HistoryItem> historyItems) {
            this.items = historyItems;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_listview_layout, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.date.setText(items.get(position).getTimestamp());
            holder.info.setText(items.get(position).getCompactInfo());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_user_history;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void setUpFragment() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        customRecyclerView = new CustomRecyclerView(historyItems);
        recyclerView.setAdapter(customRecyclerView);

        handler = new Handler();
        checkNewHistoryItem = new Thread() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                Date today = cal.getTime();
                cal.add(Calendar.DATE, -7);
                Date lastWeek = cal.getTime();
                String endDate = new SimpleDateFormat("yyyy-MM-dd").format(today);
                String startDate = new SimpleDateFormat("yyyy-MM-dd").format(lastWeek);
                refreshList(startDate, endDate);
                handler.postDelayed(this, Settings.getRefreshItemTime());
            }
        };
        handler.post(checkNewHistoryItem);
    }

    /**
     * Refresh the recyclerView when it changes.
     */
    private void refreshList(String startDate, String endDate) {
        SharedDataNetworkInterface sharedDataNetwork = SharedDataNetworkImp.getInstance();
        SharedPreferences sp = getmContext().getSharedPreferences(Constant.LOGIN_SHARED_DATA_NAME, MODE_PRIVATE);
        String token = sp.getString(Constant.SD_USER_TOKEN_KEY, null);
        historyItems.clear();
        try {
            customRecyclerView.notifyDataSetChanged();
            historyItems.addAll(sharedDataNetwork.getUserData(token, startDate, endDate));
        } catch (ConnectionException e) {
            if (this.isAdded())
                Toast.makeText(getmContext(), getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(checkNewHistoryItem);
        super.onDestroy();
    }
}
