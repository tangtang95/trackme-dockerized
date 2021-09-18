package com.trackme.trackmeapplication.home.userHome;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

import com.trackme.trackmeapplication.R;
import com.trackme.trackmeapplication.baseUtility.BaseFragment;
import com.trackme.trackmeapplication.localdb.database.AppDatabase;
import com.trackme.trackmeapplication.localdb.database.DatabaseManager;
import com.trackme.trackmeapplication.localdb.entity.HealthData;

import java.lang.ref.WeakReference;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * UserHome fragment is the first fragment load after the login, that shows to the user the possibility
 * of seeing the last value register in the system of its health.
 *
 * @author Mattia Tibaldi
 * @see BaseFragment
 */
public class UserHomeFragment extends BaseFragment {

    private static final int ANIMATION_DURATION = 2000;

    @BindView(R.id.textViewPulseValue)
    protected TextView pulseValue;
    @BindView(R.id.textViewBloodValue)
    protected TextView bloodPressureValue;
    @BindView(R.id.textViewPulseText)
    protected TextView pulseText;
    @BindView(R.id.textViewPressureText)
    protected TextView bloodPressureText;
    @BindView(R.id.textViewOxygenLevel)
    protected TextView bloodOxygenLevelText;
    @BindView(R.id.textViewOxygenLevelValue)
    protected TextView bloodOxygenLevelValue;
    @BindView(R.id.imageViewHeart)
    protected ImageView image;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_user_home;
    }

    @Override
    protected void setUpFragment() {

    }

    @OnClick(R.id.home_check_button)
    public void onCheckYourStatusButtonClick() {
        new MyAsyncTask(this).execute();
    }


    /**
     * Handle the image animation in the view.
     */
    private void handleAnimation() {
        bloodPressureText.setAlpha(0f);
        pulseText.setAlpha(0f);
        bloodPressureValue.setAlpha(0f);
        pulseValue.setAlpha(0f);
        bloodOxygenLevelText.setAlpha(0f);
        bloodPressureValue.setAlpha(0f);
        image.setAlpha(1f);

        image.animate().alpha(0f).setDuration(ANIMATION_DURATION);
        bloodPressureText.animate().alpha(1f).setDuration(ANIMATION_DURATION);
        pulseText.animate().alpha(1f).setDuration(ANIMATION_DURATION);
        bloodPressureValue.animate().alpha(1f).setDuration(ANIMATION_DURATION);
        pulseValue.animate().alpha(1f).setDuration(ANIMATION_DURATION);
        bloodOxygenLevelText.animate().alpha(1f).setDuration(ANIMATION_DURATION);
        bloodOxygenLevelValue.animate().alpha(1f).setDuration(ANIMATION_DURATION);
    }

    private static class MyAsyncTask extends AsyncTask<String, Void, HealthData>{

        private WeakReference<UserHomeFragment> weakReference;

        MyAsyncTask(UserHomeFragment context) {
            this.weakReference = new WeakReference<>(context);
        }

        @Override
        protected HealthData doInBackground(String... strings) {
            UserHomeFragment userHomeFragment = weakReference.get();
            if (userHomeFragment == null)
                return null;
            AppDatabase appDatabase = DatabaseManager.getInstance(userHomeFragment.getmContext());

            return appDatabase.getHealthDataDao().getLast();
        }

        @Override
        protected void onPostExecute(HealthData healthData) {
            UserHomeFragment userHomeFragment = weakReference.get();
            if (userHomeFragment != null){
                if (healthData != null) {
                    CharSequence textView = userHomeFragment.pulseValue.getText();
                    userHomeFragment.pulseValue.setText(String.format(Locale.getDefault(), "%d",
                            healthData.getHeartbeat()));
                    String pressure = healthData.getPressureMax() + "/" + healthData.getPressureMin();
                    userHomeFragment.bloodPressureValue.setText(pressure);
                    userHomeFragment.bloodOxygenLevelValue.setText(String.format(Locale.getDefault(), "%d",
                            healthData.getBloodOxygenLevel()));
                    userHomeFragment.handleAnimation();
                }
                else {
                    userHomeFragment.pulseValue.setText("n.v.");
                    userHomeFragment.bloodPressureValue.setText("n.v.");
                    userHomeFragment.bloodOxygenLevelValue.setText("n.v.");
                    userHomeFragment.handleAnimation();
                }

            }
        }
    }

}
