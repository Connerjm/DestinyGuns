package edu.uw.connerjm.destinyguns;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment
{
    private String url = "http://cssgate.insttech.washington.edu/~connerjm/addUser.php";

    private EditText mEmail;
    private EditText mPassword;
    private Button mRegister;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,container,false);

        mEmail = (EditText) v.findViewById(R.id.email_login_text);
        mPassword = (EditText) v.findViewById(R.id.password_login_text);

        mRegister = (Button) v.findViewById(R.id.confirm_login_button);
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

    private class AddUserWebTask extends AsyncTask<String, Void, String>
    {
        private static final String TAG = "RegisterWebTask";

        @Override
        protected String doInBackground(String...urls)
        {
            try
            {
                return downloadUrl(urls[0]);
            }
            catch (IOException e)
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
                is = conn.getInputStream();
                String contentAsString = readIt(is, len);
                Log.d(TAG, "The string is: " + contentAsString);
                return contentAsString;
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
            try
            {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("result");
                if (status.equalsIgnoreCase("success"))
                {
                    Toast.makeText(getActivity(), "User successfully inserted", Toast.LENGTH_LONG).show();
                }
                getFragmentManager().popBackStackImmediate();
            }
            catch(Exception e)
            {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }

    }


}
