package edu.uw.connerjm.destinyguns;


import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import edu.uw.connerjm.destinyguns.Activities.InfoActivity;

/**
 * Tests the info activity functionality.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 25/11/2015
 */
public class InfoActivityTest extends ActivityInstrumentationTestCase2<InfoActivity>
{

    private Solo solo;

    public InfoActivityTest()
    {
        super(InfoActivity.class);
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();
    }

    public void testInfoShowsUp()
    {
        boolean textFound = solo.searchText("Submit a bug report");
        assertTrue("Info Activity showed up.", textFound);
    }
}
