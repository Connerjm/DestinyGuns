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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.connerjm.destinyguns.Fragments.LoginFragment;
import edu.uw.connerjm.destinyguns.R;

/**
 *
 */
public class RegisterFragment extends Fragment {


    private String url = "http://cssgate.insttech.washington.edu/~connerjm/addUser.php";
    private Button mRegister;
    private EditText mEmail;
    private EditText mPassword;


    public RegisterFragment() {
        // Required empty public constructor
    }


    /**
     *
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
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mEmail = (EditText) v.findViewById(R.id.email_register);
        mPassword = (EditText) v.findViewById(R.id.password_register);
        mRegister = (Button) v.findViewById(R.id.confirm_register);
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.getText().length() != 0 && mPassword.getText().length() != 0)
                {
                    url += "?email=" + mEmail.getText().toString() + "&password=" + mPassword.getText().toString();
                    new AddUserWebTask().execute(url);
                }
            }
        });
        return v;
    }

    /**
     * Sends the User's email and password to be stored in an online database.
     */
    private class AddUserWebTask extends AsyncTask<String, Void, String>
    {
        private static final String TAG = "AddUserWebTask";

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

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
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
            } catch (Exception e)
            {
                Log.d(TAG, "Something happened. " + e.getMessage());
            } finally
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
                if (status.equalsIgnoreCase("success"))
                {
                    Toast.makeText(getActivity(), "User successfully inserted", Toast.LENGTH_LONG).show();
                }

                getFragmentManager().beginTransaction().replace(R.id.lr_container, new LoginFragment()).addToBackStack(null).commit();
            }
            catch(Exception e)
            {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }

    }

}
