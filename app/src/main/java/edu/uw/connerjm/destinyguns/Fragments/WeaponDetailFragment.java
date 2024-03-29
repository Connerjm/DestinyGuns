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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import edu.uw.connerjm.destinyguns.Data.Database;
import edu.uw.connerjm.destinyguns.R;

/**
 * A fragment that holds a list of all the weapons that fulfill the requirements passed in by the
 * home fragment.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 29/11/2015
 */
public class WeaponDetailFragment extends Fragment
{

//VARIABLES

    /** Holds the String url we need to get specific weapon info. */
    private static final String url =
            "http://cssgate.insttech.washington.edu/~connerjm/getWeaponInfo.php";

    /** The two urls that we use to add or remove weapons to the three lists. */
    private static final String addurl =
            "http://cssgate.insttech.washington.edu/~connerjm/addEntryToList.php";
    private static final String removeurl =
            "http://cssgate.insttech.washington.edu/~connerjm/removeEntryFromList.php";

    private static final String rankurl =
            "http://cssgate.insttech.washington.edu/~connerjm/updateRank.php";

    /** Holds a reference to all of our xml bits and bobs. */
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

    private ImageView mPicture;

    private String name;
    private String useremail;

    private int mRank;

    private ImageButton mFavourite;
    private ImageButton mOwned;
    private ImageButton mWishlist;

    private boolean favClicked;
    private boolean ownedClicked;
    private boolean wishClicked;

//CONSTRUCTOR

    public WeaponDetailFragment() {/* Required empty public constructor */}

//OVERWRITTEN METHODS

    /**
     * Get references from all the associated xml.
     *
     * @param inflater inflates the layout.
     * @param container is the parent activity.
     * @param savedInstanceState is the saved instance of info from before.
     * @return the view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_weapon_detail, container, false);

        SharedPreferences sharedPreferences =
                getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS), 0);
        useremail = sharedPreferences.getString(getString(R.string.USERNAME), null);

        //Gets reference to all of the xml things.
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

        mPicture = (ImageView) v.findViewById(R.id.detail_picture);
//        picture.loadUrl("http://cssgate.insttech.washington.edu/~connerjm/DestinyGunsImages/Icons/abyssdefianticon.png");

        createButtonBar(v);
        createrankBar(v);

        return v;
    }

    /**
     * Handles the call to the server to get the info for the weapon from the database and put
     * all that info in the correct xml places.
     */
    @Override
    public void onStart()
    {
        super.onStart();
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        Bundle args = getArguments();
        String name = args.getString("name"), myurl = url;
        //Encodes the name for the url because spaces and apostrophes cause issues.
        final String TAG = "Testing name encoding.";
        Log.d(TAG, name);
        name = cleanName(name);
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

        Database db = new Database(getContext());
        favClicked = db.isGunInList(name, "favourite");
        ownedClicked = db.isGunInList(name, "owned");
        wishClicked = db.isGunInList(name, "wishlist");
        db.closeDB();

        if(favClicked)
        {
            mFavourite.setImageResource(R.drawable.ic_favourite_filled);
        }
        else
        {
            mFavourite.setImageResource(R.drawable.ic_favourite_border);
        }
        if(ownedClicked)
        {
            mOwned.setImageResource(R.drawable.ic_check_double);
        }
        else
        {
            mOwned.setImageResource(R.drawable.ic_check_single);
        }
        if(wishClicked)
        {
            mWishlist.setImageResource(R.drawable.ic_wishlist_filled);
        }
        else
        {
            mWishlist.setImageResource(R.drawable.ic_wishlist_border);
        }
    }

//HELPER METHODS

    private String cleanName(String beforeName)
    {
        String returning = beforeName.replaceAll(" ", "%20");
        returning = returning.replaceAll("\'", "\\\\%27");
        return returning;
    }

    private void createButtonBar(View v)
    {
        mFavourite = (ImageButton) v.findViewById(R.id.detail_favourite_button);
        mFavourite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Database db = new Database(getContext());
                if(favClicked)
                {
                    //Remove from list
                    db.deleteBookmark(name, "favourite");
                    new AddOrRemoveWebTask().execute(removeurl + "?list=favourites&email=" + useremail + "&name=" + cleanName(name));
                    mFavourite.setImageResource(R.drawable.ic_favourite_border);
                    favClicked = false;
                }
                else
                {
                    //add to list
                    db.insertBookmark(name, "favourite");
                    new AddOrRemoveWebTask().execute(addurl + "?list=favourites&email=" + useremail + "&name=" + cleanName(name));
                    mFavourite.setImageResource(R.drawable.ic_favourite_filled);
                    favClicked = true;
                }
                db.closeDB();
            }
        });

        mOwned = (ImageButton) v.findViewById(R.id.detail_owned_button);
        mOwned.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Database db = new Database(getContext());
                if(ownedClicked)
                {
                    //Remove from list
                    db.deleteBookmark(name, "owned");
                    new AddOrRemoveWebTask().execute(removeurl + "?list=owned&email=" + useremail + "&name=" + cleanName(name));
                    mOwned.setImageResource(R.drawable.ic_check_single);
                    ownedClicked = false;
                }
                else
                {
                    //add to list
                    db.insertBookmark(name, "owned");
                    new AddOrRemoveWebTask().execute(addurl + "?list=owned&email=" + useremail + "&name=" + cleanName(name));
                    mOwned.setImageResource(R.drawable.ic_check_double);
                    ownedClicked = true;
                }
                db.closeDB();
            }
        });

        mWishlist = (ImageButton) v.findViewById(R.id.detail_wishlist_button);
        mWishlist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Database db = new Database(getContext());
                if(wishClicked)
                {
                    //Remove from list
                    db.deleteBookmark(name, "wishlist");
                    new AddOrRemoveWebTask().execute(removeurl + "?list=wishlist&email=" + useremail + "&name=" + cleanName(name));
                    mWishlist.setImageResource(R.drawable.ic_wishlist_border);
                    wishClicked = false;
                }
                else
                {
                    //add to list
                    db.insertBookmark(name, "wishlist");
                    new AddOrRemoveWebTask().execute(addurl + "?list=wishlist&email=" + useremail + "&name=" + cleanName(name));
                    mWishlist.setImageResource(R.drawable.ic_wishlist_filled);
                    wishClicked = true;
                }
                db.closeDB();
            }
        });
    }

    private void createrankBar(View v)
    {
        Button mOne = (Button) v.findViewById(R.id.one_button);
        mOne.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 1);
            }
        });

        Button mTwo = (Button) v.findViewById(R.id.two_button);
        mTwo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 2);
            }
        });

        Button mThree = (Button) v.findViewById(R.id.three_button);
        mThree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 3);
            }
        });

        Button mFour = (Button) v.findViewById(R.id.four_button);
        mFour.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 4);
            }
        });

        Button mFive = (Button) v.findViewById(R.id.five_button);
        mFive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 5);
            }
        });

        Button mSix = (Button) v.findViewById(R.id.six_button);
        mSix.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 6);
            }
        });

        Button mSeven = (Button) v.findViewById(R.id.seven_button);
        mSeven.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 7);
            }
        });

        Button mEight = (Button) v.findViewById(R.id.eight_button);
        mEight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 8);
            }
        });

        Button mNine = (Button) v.findViewById(R.id.nine_button);
        mNine.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 9);
            }
        });

        Button mTen = (Button) v.findViewById(R.id.ten_button);
        mTen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new UpdateRankWebTask().execute(rankurl + "?name=" + cleanName(name) + "&userrank=" + 10);
            }
        });
    }

//INNER CLASS

    /**
     * A class that handles the talking to the server to get the json information from the database.
     */
    private class DetailWebTask extends AsyncTask<String, Void, String>
    {

    //VARIABLES

        /** Holds the tag for this given web task. */
        private static final String TAG = "DetailWebTask";

    //OVERWRITTEN METHODS

        /**
         * Gets the web page information at the given url.
         *
         * @param urls is the url we create with the gun name added to the end.
         * @return the json.
         */
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

        /**
         * Handles all of the information that is recieved from the json.
         * We take all of that information and put it into the page.
         *
         * @param s is the string json returned from the web page.
         */
        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            final String TAG = "Getting fields from JSON";

            try
            {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                name = (String) jsonObject.get("name");

                String iconname = name.replace(" ", "").replace("\'", "").replace("(", "")
                        .replace(")", "").replace("-", "").toLowerCase();
                new LoadImageWebTask().execute(
                        "http://cssgate.insttech.washington.edu/~connerjm/DestinyGunsImages/Icons/"
                                + iconname + "icon.png");

                String mRarity = (String) jsonObject.get("rarity");
                mType.setText((String) jsonObject.get("guntype"));
                mDamage.setText((String) jsonObject.get("damagetype"));
                mFlavour.setText((String) jsonObject.get("flavour"));
                mRank = Integer.parseInt((String) jsonObject.get("rank"));
                mName.setText(getString(R.string.full_name, name, mRank));
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
                    mExclusive.setText(getString(R.string.exclusive, jsonObject.get("gatedfor")));
                }

                String type = mType.getText().toString();

                if(type.equalsIgnoreCase("fusion rifle"))
                {
                    mStatOne.setText(getString(R.string.charge_rate, jsonObject.getInt("chargerate")));
                    mStatTwo.setText(getString(R.string.impact, jsonObject.getInt("impact")));
                    mStatThree.setText(getString(R.string.range, jsonObject.getInt("range")));
                    mStatFour.setText(getString(R.string.stability, jsonObject.getInt("stability")));
                    mStatFive.setText(getString(R.string.reload, jsonObject.getInt("reload")));
                    mStatSix.setText(getString(R.string.magazine, jsonObject.getInt("magazine")));
                }
                else if(type.equalsIgnoreCase("rocket launcher"))
                {
                    mStatOne.setText(getString(R.string.rateoffire, jsonObject.getInt("rateoffire")));
                    mStatTwo.setText(getString(R.string.blast, jsonObject.getInt("blastradius")));
                    mStatThree.setText(getString(R.string.velocity, jsonObject.getInt("velocity")));
                    mStatFour.setText(getString(R.string.stability, jsonObject.getInt("stability")));
                    mStatFive.setText(getString(R.string.reload, jsonObject.getInt("reload")));
                    mStatSix.setText(getString(R.string.magazine, jsonObject.getInt("magazine")));
                }
                else if(type.equalsIgnoreCase("sword"))
                {
                    mStatOne.setText(getString(R.string.speed, jsonObject.getInt("speed")));
                    mStatTwo.setText(getString(R.string.impact, jsonObject.getInt("impact")));
                    mStatThree.setText(getString(R.string.range, jsonObject.getInt("range")));
                    mStatFour.setText(getString(R.string.efficiency, jsonObject.getInt("efficiency")));
                    mStatFive.setText(getString(R.string.defense, jsonObject.getInt("defence")));
                    mStatSix.setText(getString(R.string.energy, jsonObject.getInt("energy")));
                    mStatSeven.setText(getString(R.string.magazine, jsonObject.getInt("magazine")));
                }
                else
                {
                    mStatOne.setText(getString(R.string.rateoffire, jsonObject.getInt("rateoffire")));
                    mStatTwo.setText(getString(R.string.impact, jsonObject.getInt("impact")));
                    mStatThree.setText(getString(R.string.range, jsonObject.getInt("range")));
                    mStatFour.setText(getString(R.string.stability, jsonObject.getInt("stability")));
                    mStatFive.setText(getString(R.string.reload, jsonObject.getInt("reload")));
                    mStatSix.setText(getString(R.string.magazine, jsonObject.getInt("magazine")));
                }
            }
            catch(Exception e)
            {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }

    //HELPER METHODS

        /**
         * downloads the info from the url.
         *
         * @param myurl the created url.
         * @return the json.
         * @throws IOException
         */
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

        /**
         * Reads the stream to a string.
         *
         * @param stream the stream revieved from the web page.
         * @param len is how many characters we care about.
         * @return teh json string.
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

    /**
     * A class that gets the json information from the database from a server call.
     */
    private class AddOrRemoveWebTask extends AsyncTask<String, Void, String>
    {

        //VARIABLES

        /** Holds the string tag for this web task. */
        private static final String TAG = "AddOrRemoveWebTask";

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
                JSONObject jsonObject = new JSONObject(s);
                String result = jsonObject.getString("result");
                Log.d(TAG, "result = " + result);
            }
            catch(Exception e)
            {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }
    }

    /**
     * A class that gets the json information from the database from a server call.
     */
    private class UpdateRankWebTask extends AsyncTask<String, Void, String>
    {

        //VARIABLES

        /** Holds the string tag for this web task. */
        private static final String TAG = "UpdateRankWebTask";

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
                JSONObject jsonObject = new JSONObject(s);
                mRank = jsonObject.getInt("newrank");
                Log.d(TAG, "result = " + mRank);
                mName.setText(getString(R.string.full_name, name, mRank));
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
                mPicture.setImageBitmap(bitmap);
                Log.d(TAG, "have set image to " + bitmap.toString());
            }
            else
            {
                Log.d(TAG, "Bitmap problem");
            }
        }
    }
}
