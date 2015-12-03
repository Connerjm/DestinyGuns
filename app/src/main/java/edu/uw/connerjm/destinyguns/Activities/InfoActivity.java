package edu.uw.connerjm.destinyguns.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.uw.connerjm.destinyguns.R;

/**
 * An activity that presents the user with information about us, the devs, and about the
 * application itself including how to file a bug report and copyright information.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.02
 * @since 20/11/2015
 */
public class InfoActivity extends AppCompatActivity
{

//OVERWRITTEN METHODS

    /**
     * Creates and shows the activity that has all of our text views in it.
     *
     * @param savedInstanceState the saved information from before.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView bugReportText = (TextView) findViewById(R.id.bug_report_text);
        bugReportText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, "connerjm@uw.edu");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Destiny Guns Bug Report");
                try
                {
                    startActivity(Intent.createChooser(intent, "Submit Bug Report"));
                }
                catch(android.content.ActivityNotFoundException e)
                {
                    Toast.makeText(v.getContext(),
                            "No mail clients installed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
