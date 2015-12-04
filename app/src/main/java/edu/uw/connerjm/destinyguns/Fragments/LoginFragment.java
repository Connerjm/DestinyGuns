package edu.uw.connerjm.destinyguns.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import edu.uw.connerjm.destinyguns.R;

/**
 * The login fragment that allows the user to enter their email
 * and password for the app. Can also start the register fragment from
 * here.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 14/11/2015
 */
public class LoginFragment extends Fragment
{

//INTERFACE

    /**
     * Interface that allows Activity to switch between Fragments and main activity.
     */
    public interface MyRegisterListener
    {
        void myStartRegister();
        void myStartRegisterFacebook(String firstName, String lastName, String email);
        void myStartMain();
    }

//VARIABLES

    /** the server url used to log the user in. */
    private String url = "http://cssgate.insttech.washington.edu/~connerjm/login.php";

    /** the layout components we interact with. */
    private EditText mEmail;
    private EditText mPassword;
    private LoginButton mFaceLogin;

    private CallbackManager mCallbackManager;

//CONSTRUCTOR

    public LoginFragment() {/* Required empty public constructor */}

//INHERITED METHODS

    /**
     * Inflates the UI of the login fragment.
     * Sets listeners to the buttons for logging in
     * and registering.
     *
     * @param inflater inflates the layout.
     * @param container is the activity this fragment is in.
     * @param savedInstanceState is info.
     * @return inflated UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FacebookSdk.sdkInitialize(getContext());
        mCallbackManager = CallbackManager.Factory.create();
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mEmail = (EditText) v.findViewById(R.id.email_login);
        mPassword = (EditText) v.findViewById(R.id.password_login);

        Button mLogin = (Button) v.findViewById(R.id.confirm_login_button);
        mLogin.setOnClickListener(new View.OnClickListener()
        {
            /**
             * Goes through a process of receiving the email and text combination and attempting to
             * login from the database of users. If logging in is successful, the post execute will
             * take the user to the main application.
             *
             * @param v the parent view.
             */
            @Override
            public void onClick(View v)
            {
                if (mEmail.getText().length() != 0 && mPassword.getText().length() != 0)
                {
                    url += "?email=" + mEmail.getText().toString() + "&password=" +
                            mPassword.getText().toString();
                    new LoginWebTask().execute(url);
                }
            }
        });

        Button mRegister = (Button) v.findViewById(R.id.register_login_button);
        mRegister.setOnClickListener(new View.OnClickListener() {
            /**
             * When the register button is clicked, the login activity will switch to the register
             * fragment from this one.
             *
             * @param v the parent view.
             */
            @Override
            public void onClick(View v) {
                ((MyRegisterListener) getActivity()).myStartRegister();
            }
        });
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        mFaceLogin = (LoginButton) v.findViewById(R.id.login_button);
        mFaceLogin.setReadPermissions(Arrays.asList("email", "public_profile"));
        mFaceLogin.setFragment(this);
        mFaceLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFaceLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    /**
                     * Grants access to the User's Facebook
                     * information if login result is successful
                     */
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = loginResult.getAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                            /**
                             * Retrieves the User's Facebook information
                             * in a JSONObject and sends it to the LoginActivity.
                             * @param object User's Facebook information
                             * @param response Not used
                             */
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                ((MyRegisterListener) getActivity()).
                                        myStartRegisterFacebook(object.optString("first_name"), object.optString("last_name"), object.optString("email"));
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "first_name, last_name, email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //INNER CLASSES

    /**
     * Tries to authenticate the User's email and password that is stored in an online database.
     */
    private class LoginWebTask extends AsyncTask<String, Void, String>
    {

    //VARIABLES

        private static final String TAG = "LoginWebTask";

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
         * @param myurl is the server url to execute.
         * @return the characters from the page.
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
                if(is != null)
                {
                    is.close();
                }
            }
            return null;
        }

        /**
         * Reads an InputStream and converts it to a String.
         *
         * @param stream is the file reader.
         * @param len is the amount of characters we're limiting to.
         * @return the string of characters.
         * @throws IOException
         */
        public String readIt(InputStream stream, int len) throws IOException
        {
            Reader reader;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        /**
         * Handles the result of the attempt to log in, printing a toast to let the user know what
         * happened. If successful, sets the shared preferences and starts the main activity.
         *
         * @param s is the json string.
         */
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
                    Toast.makeText(getActivity(), "Login is Successful.", Toast.LENGTH_LONG).show();
                    SharedPreferences mSharedPreferences = getActivity().getSharedPreferences
                            (getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean(getString(R.string.LOGGED_IN), true);
                    editor.putString(getString(R.string.USERNAME), mEmail.getText().toString());
                    editor.commit();
                    ((MyRegisterListener) getActivity()).myStartMain();
                }
                else
                {
                    Toast.makeText(getActivity(), getString(R.string.login_failure),
                            Toast.LENGTH_LONG).show();
                }
                //Transition to MainActivity goes here.
            }
            catch(Exception e)
            {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }
    }
}
