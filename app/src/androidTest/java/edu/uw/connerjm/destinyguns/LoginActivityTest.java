package edu.uw.connerjm.destinyguns;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.Random;

import edu.uw.connerjm.destinyguns.Activities.LoginActivity;

/**
 * Tests the login activity functionality.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 25/11/2015
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity>
{

//VARIABLES

    /** Holds the solo that simulates a user using the app. */
    private Solo solo;

//CONSTRUCTOR

    /**
     * Creates an instance of this test class.
     */
    public LoginActivityTest()
    {
        super(LoginActivity.class);
    }

//OVERWRITTEN METHODS

    /**
     * Called at the start to set up the test to run all of the tests.
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
     * Called at the end to finish all of the activities and be done.
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
     * Tests if the login fragment shows up.
     */
    public void testLoginShowsUp()
    {
        boolean textFound = solo.searchText("Welcome Back!");
        assertTrue("Login Activity showed up.", textFound);
    }

    /**
     * Tests to see if it is possible to register that same email again.
     */
    public void testRegisteringDuplicateUser()
    {
        solo.clickOnButton(1);
        boolean textFound1 = solo.searchText("Register New User");
        assertTrue("Could switch to register fragment", textFound1);
        solo.enterText(0, "connerjm@gmail.com");
        solo.enterText(1, "testing");
        solo.enterText(2, "Conner");
        solo.enterText(3, "J");
        solo.enterText(4, "Martin");
        solo.clickOnButton(0);
        boolean textFound2 = solo.searchText("Unable to register");
        assertTrue("Unable to make repeat user.", textFound2);
    }

    /**
     * Tests whether the register new user fragment comes up upon clicking register.
     */
    public void testRegisteringNewUser()
    {
        solo.clickOnButton(1);
        boolean textFound1 = solo.searchText("Register New User");
        assertTrue("Could switch to register fragment", textFound1);
        Random random = new Random();
        int number = random.nextInt(10000);
        solo.enterText(0, "test@test" + number + ".com");
        solo.enterText(1, "testingpassword");
        solo.enterText(2, "Test");
        solo.enterText(3, "T");
        solo.enterText(4, "Test");
        if(random.nextBoolean())
        {
            solo.clickOnText("Xbox One");
            solo.clickOnText("Playstation Four");
        }
        solo.clickOnButton(0);
        boolean textFound2 = solo.searchText("Thank you for registering!");
        assertTrue("Able to register new user.", textFound2);
    }
}
