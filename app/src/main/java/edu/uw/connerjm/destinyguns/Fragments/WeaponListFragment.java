package edu.uw.connerjm.destinyguns.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
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
 * Holds the list of weapons that fulfill what is chosen from the home activity.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 29/11/2015
 */
public class WeaponListFragment extends Fragment
{

//INTERFACE

    public interface weaponListListener
    {
        void switchToWeaponDetailFragment(String name);
    }

//VARIABLES

    /** The different urls that we need to talk to the server. */
    private static final String refineURL =
            "http://cssgate.insttech.washington.edu/~connerjm/refineWeaponList.php";
    private static final String refineRarityURL =
            "http://cssgate.insttech.washington.edu/~connerjm/refineWeaponListRarity.php";
    private static final String favouriteListURL =
            "http://cssgate.insttech.washington.edu/~connerjm/viewUserFavourites.php";
    private static final String ownedListURL =
            "http://cssgate.insttech.washington.edu/~connerjm/viewOwned.php";
    private static final String wishlistURL =
            "http://cssgate.insttech.washington.edu/~connerjm/viewWishlist.php";

    /** Holds a list of the weapon info we need for holding these weapons. */
    private List<WeaponInfo> mList = new ArrayList<>();
    private ListView mListView;
    private ArrayAdapter mAdapter;

    /** Holds that shared prefs file that holds the username. */
    private SharedPreferences mSharedPreferences;

//CONSTRUCTOR

    public WeaponListFragment() {/* Required empty public constructor */}

//OVERWRITTEN METHODS

    /**
     * Creates the view and inflates the correct xml layout.
     *
     * @param inflater inflates the layout.
     * @param container is the main activity.
     * @param savedInstanceState is the saved information.
     * @return the view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weapon_list, container, false);
    }

    /**
     * Is called every time this fragment starts, meaning it gets the parameters of the weapons
     * the user wants in the list this time.
     */
    @Override
    public void onStart()
    {
        super.onStart();
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //Gets the parameters to refine the list.
        Bundle args = this.getArguments();
        boolean wasList = args.getBoolean("waslist?");
        String myurl = "", thelist = "";
        String rarity = args.getString("rarity");
        String slot = args.getString("slot");
        String type = args.getString("type");

        if(wasList)//From a list.
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
            mSharedPreferences =
                    getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS), 0);
            myurl += "?email=" + mSharedPreferences.getString(getString(R.string.USERNAME), null);
        }
        else if(!(rarity == null) && (slot == null) && (type == null))//just rarity
        {
            myurl = refineRarityURL;
            myurl += "?rarity=" + rarity;
        }
        else if((rarity == null) && !(slot == null) && (type == null))//just slot
        {
        }
        else if((rarity == null) && (slot == null) && !(type == null))//just type
        {
        }
        else if(!(rarity == null) && !(slot == null) && (type == null))//rarity and slot
        {
        }
        else if((rarity == null) && !(slot == null))//slot and type
        {
        }
        else if((rarity == null))//none
        {
        }
        else//rarity and type or all three.
        {
            myurl = refineURL;
            myurl += "?rarity=" + rarity;
            try
            {
                myurl += "&type=" + URLEncoder.encode(type, "UTF-8");
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

        //Add a header to the list.
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

        //Set listener so that clicking a gun gets the detailed information.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ((weaponListListener)
                        getActivity()).switchToWeaponDetailFragment(mList.get(position - 1).mName);
            }
        });
    }

//INNER CLASS

    /**
     * A class that gets the json information from the database from a server call.
     */
    private class WeaponWebTask extends AsyncTask<String, Void, String>
    {

    //VARIABLES

        /** Holds the string tag for this web task. */
        private static final String TAG = "WeaponWebTask";

    //OVERWRITTEN METHODS

        /**
         * Call to the server.
         *
         * @param urls is the url of the server.
         * @return the yielded stream.
         */
        @Override
        protected String doInBackground(String...urls)
        {
            String response = "";
            HttpURLConnection urlConnection = null;
            for(String url : urls)
            {
                try
                {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new
                            InputStreamReader(content));
                    String s;
                    while((s = buffer.readLine()) != null)
                    {
                        response += s;
                    }
                }
                catch(Exception e)
                {
                    response = "Unable to download because " + e.getMessage();
                }
                finally
                {
                    if(urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }

        /**
         * Handles all of the json information that is returned from the server call.
         *
         * @param s is the string json returned.
         */
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

    //HELPER METHODS

        /**
         * Takes the stream and reads it into a json string.
         *
         * @param stream that is returned from the server.
         * @param len is how long we care about.
         * @return the json string.
         * @throws IOException
         */
        public String readIt(InputStream stream, int len) throws IOException
        {
            Reader reader  = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}
