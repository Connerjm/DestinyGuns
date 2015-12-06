package edu.uw.connerjm.destinyguns.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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
    private static final String refineSlotURL =
            "http://cssgate.insttech.washington.edu/~connerjm/refineWeaponListSlot.php";
    private static final String refineTypeURL =
            "http://cssgate.insttech.washington.edu/~connerjm/refineWeaponListType.php";
    private static final String refineRarityAndSlotURL =
            "http://cssgate.insttech.washington.edu/~connerjm/refineWeaponListRarityAndSlot.php";
    private static final String refineAllURL =
            "http://cssgate.insttech.washington.edu/~connerjm/refineWeaponListAll.php";
    private static final String refineListURL =
            "http://cssgate.insttech.washington.edu/~connerjm/viewList.php";

    /** Holds a list of the weapon info we need for holding these weapons. */
    private ArrayList<WeaponInfo> mList = new ArrayList<>();
    private ListView mListView;
    private ArrayAdapter mAdapter;

    private Bitmap mIcon;
    private Bitmap mDamageIcon;

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

        mListView = (ListView) getActivity().findViewById(R.id.weapon_list);
        TextView textView = new TextView(getActivity());

        mAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mList);

//        mAdapter = new MyAdapter(getActivity(), mList);

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //Gets the parameters to refine the list.
        Bundle args = this.getArguments();
        boolean wasList = args.getBoolean("waslist?");
        String myurl, thelist;
        String rarity = args.getString("rarity");
        String slot = args.getString("slot");
        String type = args.getString("type");

        if(wasList)//From a list.
        {
            thelist = args.getString("thelist");
            myurl = refineListURL;
            SharedPreferences sharedPreferences =
                    getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS), 0);
            myurl += "?email=" + sharedPreferences.getString(getString(R.string.USERNAME), null);
            if(thelist != null)
                myurl += "&list=" + thelist.toLowerCase();

            textView.setText(getString(R.string.your_blank_weapons, thelist));
        }
        else if(!(rarity == null) && (slot == null) && (type == null))//just rarity
        {
            myurl = refineRarityURL;
            myurl += "?rarity=" + rarity;
            textView.setText(getString(R.string.blank_weapons, rarity));
        }
        else if((rarity == null) && !(slot == null) && (type == null))//just slot
        {
            myurl = refineSlotURL;
            myurl += "?slot=" + slot.toLowerCase();
            textView.setText(getString(R.string.blank_weapons, slot));
        }
        else if((rarity == null) && (slot == null) && !(type == null)
                || ((rarity == null) && !(slot == null)))//just type or slot and type
        {
            myurl = refineTypeURL;
            try
            {
                myurl += "?type=" + URLEncoder.encode(type, "UTF-8");
            }
            catch(Exception e)
            {
                Log.d("WeaponListURLEncoding", "Error encoding " + e.getMessage());
            }
            textView.setText(getString(R.string.blanks, type));
        }
        else if(!(rarity == null) && !(slot == null) && (type == null))//rarity and slot
        {
            myurl = refineRarityAndSlotURL;
            myurl += "?rarity=" + rarity + "&slot=" + slot.toLowerCase();
            textView.setText(getString(R.string.blank_blank_weapons, rarity, slot));
        }
        else if((rarity == null))//none
        {
            myurl = refineAllURL;
            textView.setText(getString(R.string.all_weapons));
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
            textView.setText(getString(R.string.blank_blanks, rarity, type));
        }
        mListView.addHeaderView(textView);

        if(networkInfo != null && networkInfo.isConnected())
        {
            new WeaponWebTask().execute(myurl);
        }
        else
        {
            Toast.makeText(getActivity(), "No network connection available.",
                    Toast.LENGTH_LONG).show();
        }

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
         * @return the yielded string.
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

                if(s.length() == 0)
                {
                    Toast.makeText(getActivity(), "Your List is Empty!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONArray jsonArray = new JSONArray(s);
                    for(int x = 0; x < jsonArray.length(); x++)
                    {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(x);
                        String name = (String) jsonObject.get("name");
//                        String cleanname = name.replace(" ", "").replace("\'", "").replace("(", "")
//                                .replace(")", "").replace("-", "").toLowerCase();
//                        String damageType = (String) jsonObject.get("damagetype");
//                        damageType = damageType.toLowerCase();
//                        new LoadImageWebTask().execute(
//                                "http://cssgate.insttech.washington.edu/~connerjm/DestinyGuns" +
//                                        "Images/Icons/" + cleanname + "icon.png");
//                        new LoadDamageImageWebTask().execute("http://cssgate.insttech.washington.edu/~connerjm/DestinyGunsImages/damageicons/" + damageType + ".svg");
                        mList.add(new WeaponInfo(mIcon, name, mDamageIcon));
                    }
                    mListView.setAdapter(mAdapter);
                }
            }
            catch(Exception e)
            {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }
    }

    private class LoadImageWebTask extends AsyncTask<String, Void, Bitmap>
    {

        private static final String TAG = "LoadImageWebTask";

        @Override
        protected Bitmap doInBackground(String... args)
        {
            Bitmap bitmap = null;
            try
            {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                Log.d(TAG, "have received bitmap " + bitmap.toString());
            }
            catch(Exception e)
            {
                Log.d(TAG, e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if(bitmap != null)
            {
                mIcon = bitmap;
                Log.d(TAG, "have set image to " + bitmap.toString());
            }
            else
            {
                Log.d(TAG, "Bitmap problem");
            }
        }
    }

    private class LoadDamageImageWebTask extends AsyncTask<String, Void, Bitmap>
    {

        private static final String TAG = "LoadImageWebTask";

        @Override
        protected Bitmap doInBackground(String... args)
        {
            Bitmap bitmap = null;
            try
            {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                Log.d(TAG, "have received bitmap " + bitmap.toString());
            }
            catch(Exception e)
            {
                Log.d(TAG, e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if(bitmap != null)
            {
                mDamageIcon = bitmap;
                Log.d(TAG, "have set image to " + bitmap.toString());
            }
            else
            {
                Log.d(TAG, "Bitmap problem");
            }
        }
    }
}
