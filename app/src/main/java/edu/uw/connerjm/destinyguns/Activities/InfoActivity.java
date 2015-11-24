package edu.uw.connerjm.destinyguns.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.uw.connerjm.destinyguns.R;

/**
 * An activity that presents the user with information about us, the devs, and about the
 * application itself including how to file a bug report and copyright information.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 20/11/2015
 */
public class InfoActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
}
