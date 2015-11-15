package edu.uw.connerjm.destinyguns.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.connerjm.destinyguns.R;

/**
 * A fragment that allows the a new user to register into the database with a username and password.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 14/11/2015
 */
public class RegisterFragment extends Fragment
{

//VARIABLES

    /** the server url to add a user to the database. */
    private String url = "http://cssgate.insttech.washington.edu/~connerjm/addUser.php";

    /** the layout parts that we need access to. */
    private Button mRegister;
    private EditText mEmail;
    private EditText mPassword;

//CONSTRUCTOR

    public RegisterFragment()
    {
        // Required empty public constructor
    }

    /**
     * Inflates the fragment_register UI and sets an
     * on click listener for the register button.
     *
     * @param inflater inflates the layout.
     * @param container is the activity this fragment is in.
     * @param savedInstanceState is info.
     * @return the inflated UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mEmail = (EditText) v.findViewById(R.id.email_register);
        mPassword = (EditText) v.findViewById(R.id.password_register);

        mRegister = (Button) v.findViewById(R.id.confirm_register);
        mRegister.setOnClickListener(new View.OnClickListener()
        {
            /**
             * When the register button is clicked, if the fields are filled out accordingly, we
             * attempt to add the new user to the database using the given email and password.
             *
             * @param v is the parent view.
             */
            @Override
            public void onClick(View v)
            {
                if (mEmail.getText().length() != 0 && mPassword.getText().length() != 0)
                {
                    url += "?email=" + mEmail.getText().toString() + "&password=" + mPassword.getText().toString();
                    new AddUserWebTask().execute(url);
                }
            }
        });
        return v;
    }

//INNER CLASSES

    /**
     * Sends the User's email and password to be stored in an online database.
     */
    private class AddUserWebTask extends AsyncTask<String, Void, String>
    {

    //VARIABLES

        private static final String TAG = "AddUserWebTask";

    //METHODS

        @Override
        protected String doInBackground(String...urls)
        {
            // params comes from the execute() call: params[0] is the url.
            try
            {
                return downloadUrl(urls[0]);
            }
            catch (IOException e)
            {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        /**
         * Given a URL, establishes an HttpUrlConnection and retrieves
         * the web page content as a InputStream, which it returns as
         * a string.
         *
         * @param myurl is the url we are downloading.
         * @return the characters retrieved from the web page.
         * @throws IOException
         */
        private String downloadUrl(String myurl) throws IOException
        {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try
            {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /*milliseconds*/);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();
                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                Log.d(TAG, "The string is: " + contentAsString);
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            }
            catch(Exception e)
            {
                Log.d(TAG, "Something happened. " + e.getMessage());
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

        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream, int len) throws IOException
        {
            Reader reader;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            // Parse JSON
            try
            {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("result");
                if(status.equalsIgnoreCase("success"))
                {
                    Toast.makeText(getActivity(), "You have successfully registered", Toast.LENGTH_LONG).show();
                    getFragmentManager().beginTransaction().replace(R.id.lr_container, new LoginFragment()).addToBackStack(null).commit();
                }
                else
                {
                    Toast.makeText(getActivity(), "Unable to register. Please retype your email and password. Password must be at least 6 characters in length.", Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception e)
            {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }
    }
}
