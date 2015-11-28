package edu.uw.connerjm.destinyguns;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import edu.uw.connerjm.destinyguns.Activities.MainActivity;

/**
 * Tests the main activity functionality.
 *
 * @author Conner Martin
 * @author Robert Gillis
 * @version 0.0.01
 * @since 25/11/2015
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>
{

    private Solo solo;

    public MainActivityTest()
    {
        super(MainActivity.class);
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

    public void testHomeShowsUp()
    {
        boolean textFound = solo.searchText("View Selected List");
        textFound &= solo.searchText("Refine the List");
        assertTrue("Home fragment showed up.", textFound);
    }

    public void testSelectingList()
    {
        solo.clickOnText("Select a List");
        solo.clickOnText("Wishlist");
        solo.clickOnButton(0);
        boolean textFound = solo.searchText("Going to the Wishlist");
        assertTrue("Could select a list and press the go to list button.", textFound);
    }

    public void testRefiningList()
    {
        solo.clickOnText("Select a Rarity");
        solo.clickOnText("Exotic");
        solo.clickOnText("Select an Equip Slot");
        solo.clickOnText("Heavy");
        solo.clickOnText("Select a Weapon Type");
        solo.clickOnText("Sword");
        solo.clickOnButton(1);
        boolean textFound1 = solo.searchText("Retrieving all Exotic, Heavy Sword Weapons");
        boolean textFound2 = solo.searchText("Bolt-Caster");
        assertTrue("Could select all properties of a weapon and " +
                "press refine button and get results.", textFound1 & textFound2);
    }

    public void testMoveToInfo()
    {
        solo.clickOnMenuItem("App Info");
        boolean textFound = solo.searchText("Submit a bug report");
        assertTrue("Can switch from main to info activity.", textFound);
    }

    public void testLogInAndOut()
    {
        solo.clickOnMenuItem("Logout");
        boolean textFound1 = solo.searchText("Welcome Back!");
        assertTrue("Could logout and switch to login activity.", textFound1);
        solo.enterText(0, "connerjm@gmail.com");
        solo.enterText(1, "testing");
        solo.clickOnButton(0);
        boolean textFound2 = solo.searchText("Login is Successful.");
        assertTrue("Able to log back in.", textFound2);
    }

    public void testWeaponRefineValidation()
    {
        solo.clickOnText("Select a Rarity");
        solo.clickOnText("Legendary");
        solo.clickOnText("Select an Equip Slot");
        solo.clickOnText("Primary");
        solo.clickOnText("Select a Weapon Type");
        solo.clickOnText("Rocket Launcher");
        solo.clickOnButton(1);
        boolean textFound = solo.searchText("Not a valid");
        assertTrue("Weapon validation works", textFound);
    }
}
