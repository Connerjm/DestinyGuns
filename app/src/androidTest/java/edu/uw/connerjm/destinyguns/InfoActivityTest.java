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

//VARIABLES

    /** Holds the solo which simulates a user using the app. */
    private Solo solo;

//CONSTRUCTOR

    /**
     * Creates a new instance of this test.
     */
    public InfoActivityTest()
    {
        super(InfoActivity.class);
    }

//OVERWRITTEN METHODS

    /**
     * Is called at the beginning to do all the things that need to be completed before the tests.
     *
     * @throws Exception
     */
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    /**
     * Called at the end to close everything and be done.
     *
     * @throws Exception
     */
    @Override
    public void tearDown() throws Exception
    {
        solo.finishOpenedActivities();
    }

//METHODS

    /**
     * Tests to see if the info panel shows up.
     */
    public void testInfoShowsUp()
    {
        boolean textFound = solo.searchText("Submit a bug report");
        assertTrue("Info Activity showed up.", textFound);
    }
}
