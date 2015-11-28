package edu.uw.connerjm.destinyguns.Fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import edu.uw.connerjm.destinyguns.HelperClasses.WeaponInfo;
import edu.uw.connerjm.destinyguns.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeaponListFragment extends Fragment
{

    private static final String
            refineURL = "http://cssgate.insttech.washington.edu/~connerjm/refineWeaponList.php";
    private static final String
            favouriteListURL = "http://cssgate.insttech.washington.edu/~connerjm/viewUserFavourites.php";
    private static final String
            ownedListURL = "http://cssgate.insttech.washington.edu/~connerjm/viewOwned.php";
    private static final String
            wishlistURL = "http://cssgate.insttech.washington.edu/~connerjm/viewWishlist.php";

    private List<WeaponInfo> mList = new ArrayList<>();
    private ListView mListView;
    private ArrayAdapter mAdapter;

    public WeaponListFragment() {/* Required empty public constructor */}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weapon_list, container, false);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Bundle args = this.getArguments();
        boolean wasList = args.getBoolean("waslist?");
        String myurl = "", thelist = "";

        if(wasList)
        {
            thelist = args.getString("thelist");
            switch(thelist)
            {
                case "Favourites":
                    myurl = favouriteListURL;
                    break;
                case "Owned":
                    myurl = ownedListURL;
                    break;
                case "Wishlist":
                    myurl = wishlistURL;
                    break;
            }
            myurl += "?email=" + "connerjm@gmail.com";//TODO change this email!!
        }
        else
        {
            myurl = refineURL;
            myurl += "?rarity=" + args.getString("rarity");
            try
            {
                myurl += "&type=" + URLEncoder.encode(args.getString("type"), "UTF-8");
            }
            catch(Exception e)
            {
                Log.d("WeaponListURLEncoding", "Error in encoding is " + e.getMessage());
            }
        }

        if(networkInfo != null && networkInfo.isConnected())
        {
            new WeaponWebTask().execute(myurl);
        }
        else
        {
            Toast.makeText(getActivity(), "No network connection available.",
                    Toast.LENGTH_LONG).show();
        }

        mListView = (ListView) getActivity().findViewById(R.id.weapon_list);

        if(wasList)
        {
            TextView textView = new TextView(getActivity());
            textView.setText("Your " + thelist + " weapon's");

            mListView.addHeaderView(textView);
        }
        else
        {
            TextView textView = new TextView(getActivity());
            textView.setText(args.getString("rarity") + " " + args.getString("type") + "'s");

            mListView.addHeaderView(textView);
        }

        mAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mList);
    }

    private class WeaponWebTask extends AsyncTask<String, Void, String>
    {

        private static final String TAG = "WeaponWebTask";

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
            int len = 500;

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

            try
            {
                mList.clear();

                JSONArray jsonArray = new JSONArray(s);
                for(int x = 0; x < jsonArray.length(); x++)
                {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(x);
                    String name = (String) jsonObject.get("name");
                    String damageType = (String) jsonObject.get("damagetype");
                    mList.add(new WeaponInfo(name, damageType));
                }
                mListView.setAdapter(mAdapter);
            }
            catch(Exception e)
            {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }
    }
}
