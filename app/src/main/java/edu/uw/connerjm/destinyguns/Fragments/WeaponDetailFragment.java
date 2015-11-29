package edu.uw.connerjm.destinyguns.Fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.connerjm.destinyguns.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeaponDetailFragment extends Fragment
{

    private static final String url =
            "http://cssgate.insttech.washington.edu/~connerjm/getWeaponInfo.php";

    private TextView mName;
    private TextView mFlavour;
    private TextView mExclusive;
    private TextView mDamage;
    private TextView mType;
    private TextView mPerks;
    private TextView mAcquire;
    private TextView mStatOne;
    private TextView mStatTwo;
    private TextView mStatThree;
    private TextView mStatFour;
    private TextView mStatFive;
    private TextView mStatSix;
    private TextView mStatSeven;
    private TextView mDetails;

    private String mRarity;
    private int mRank;

    public WeaponDetailFragment() {/* Required empty public constructor */}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_weapon_detail, container, false);

        mName = (TextView) v.findViewById(R.id.detail_weapon_name);
        mFlavour = (TextView) v.findViewById(R.id.detail_flavour_text);
        mExclusive = (TextView) v.findViewById(R.id.detail_exlusive_text);
        mDamage = (TextView) v.findViewById(R.id.detail_damage_text);
        mType = (TextView) v.findViewById(R.id.detail_type_text);
        mPerks = (TextView) v.findViewById(R.id.detail_perks_text);
        mAcquire = (TextView) v.findViewById(R.id.detail_acquire_text);
        mStatOne = (TextView) v.findViewById(R.id.detail_stat_one_text);
        mStatTwo = (TextView) v.findViewById(R.id.detail_stat_two_text);
        mStatThree = (TextView) v.findViewById(R.id.detail_stat_three_text);
        mStatFour = (TextView) v.findViewById(R.id.detail_stat_four_text);
        mStatFive = (TextView) v.findViewById(R.id.detail_stat_five_text);
        mStatSix = (TextView) v.findViewById(R.id.detail_stat_six_text);
        mStatSeven = (TextView) v.findViewById(R.id.detail_stat_seven_text);
        mDetails = (TextView) v.findViewById(R.id.detail_details_text);

        Button mFavourite = (Button) v.findViewById(R.id.detail_favourite_button);


        Button mOwned = (Button) v.findViewById(R.id.detail_owned_button);


        Button mWishlist = (Button) v.findViewById(R.id.detail_wishlist_button);


        return v;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Bundle args = getArguments();
        String name = args.getString("name"), myurl = url;
        final String TAG = "Testing name encoding.";
        Log.d(TAG, name);
        name = name.replaceAll("\'", "\\\\%27");
        Log.d(TAG, name);
        name = name.replaceAll(" ", "%20");
        Log.d(TAG, name);
        myurl += "?name=" + name;
        Log.d(TAG, myurl);

        if(networkInfo != null && networkInfo.isConnected())
        {
            new DetailWebTask().execute(myurl);
        }
        else
        {
            Toast.makeText(getActivity(), "No network connection available.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private class DetailWebTask extends AsyncTask<String, Void, String>
    {

        private static final String TAG = "DetailWebTask";

        @Override
        protected String doInBackground(String...urls)
        {
            try
            {
                return downloadUrl(urls[0]);
            }
            catch(IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        private String downloadUrl(String myurl) throws IOException
        {
            InputStream is = null;
            int len = 1000;

            try
            {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is= conn.getInputStream();

                String contentAsString = readIt(is, len);
                Log.d(TAG, "The string is: " + contentAsString);
                return contentAsString;
            }
            catch(Exception e)
            {
                Log.d(TAG, "Something happened " + e.getMessage());
            }
            finally
            {
                if (is != null)
                {
                    is.close();
                }
            }
            return null;
        }

        public String readIt(InputStream stream, int len) throws IOException
        {
            Reader reader  = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            final String TAG = "Getting fields from JSON";

            try
            {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                String name = (String) jsonObject.get("name");
                mRarity = (String) jsonObject.get("rarity");
                mType.setText((String) jsonObject.get("guntype"));
                mDamage.setText((String) jsonObject.get("damagetype"));
                mFlavour.setText((String) jsonObject.get("flavour"));
                mRank = Integer.parseInt((String) jsonObject.get("rank"));
                mName.setText(name + " (" + mRank + ")");
                if(mRarity.equalsIgnoreCase("Legendary"))
                {
                    mName.setTextColor(ContextCompat.getColor(getActivity(), R.color.legendary));
                }
                else
                {
                    mName.setTextColor(ContextCompat.getColor(getActivity(), R.color.exotic));
                }
                mPerks.setText((String) jsonObject.get("perks"));
                mAcquire.setText((String) jsonObject.get("acquire"));
                if(!jsonObject.isNull("details"))
                {
                    mDetails.setText((String) jsonObject.get("details"));
                }
                if(!jsonObject.isNull("gatedfor"))
                {
                    mExclusive.setText("!" + jsonObject.get("gatedfor") + " exclusive!");
                }

                String type = mType.getText().toString();

                if(type.equalsIgnoreCase("fusion rifle"))
                {
                    mStatOne.setText("Charge Rate: " + jsonObject.get("chargerate"));
                    mStatTwo.setText("Impact: " + jsonObject.get("impact"));
                    mStatThree.setText("Range: " + jsonObject.get("range"));
                    mStatFour.setText("Stability: " + jsonObject.get("stability"));
                    mStatFive.setText("Reload: " + jsonObject.get("reload"));
                    mStatSix.setText("Magazine: " + jsonObject.get("magazine"));
                }
                else if(type.equalsIgnoreCase("rocket launcher"))
                {
                    mStatOne.setText("Rate of Fire: " + jsonObject.get("rateoffire"));
                    mStatTwo.setText("Blast Radius: " + jsonObject.get("blastradius"));
                    mStatThree.setText("Velocity: " + jsonObject.get("velocity"));
                    mStatFour.setText("Stability: " + jsonObject.get("stability"));
                    mStatFive.setText("Reload: " + jsonObject.get("reload"));
                    mStatSix.setText("Magazine: " + jsonObject.get("magazine"));
                }
                else if(type.equalsIgnoreCase("sword"))
                {
                    mStatOne.setText("Speed: " + jsonObject.get("speed"));
                    mStatTwo.setText("Impact: " + jsonObject.get("impact"));
                    mStatThree.setText("Range: " + jsonObject.get("range"));
                    mStatFour.setText("Efficiency: " + jsonObject.get("efficiency"));
                    mStatFive.setText("Defense: " + jsonObject.get("defence"));
                    mStatSix.setText("Energy: " + jsonObject.get("energy"));
                    mStatSeven.setText("Magazine: " + jsonObject.get("magazine"));
                }
                else
                {
                    mStatOne.setText("Rate of Fire: " + jsonObject.get("rateoffire"));
                    mStatTwo.setText("Impact: " + jsonObject.get("impact"));
                    mStatThree.setText("Range: " + jsonObject.get("range"));
                    mStatFour.setText("Stability: " + jsonObject.get("stability"));
                    mStatFive.setText("Reload: " + jsonObject.get("reload"));
                    mStatSix.setText("Magazine: " + jsonObject.get("magazine"));
                }
            }
            catch(Exception e)
            {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }
    }
}
