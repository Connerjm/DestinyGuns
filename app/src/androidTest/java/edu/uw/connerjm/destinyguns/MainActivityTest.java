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

//VARIABLES

    /** Holds the solo that simulates a user using the application. */
    private Solo solo;

//CONSTRUCTOR

    /**
     * Creates a new instance of this test.
     */
    public MainActivityTest()
    {
        super(MainActivity.class);
    }

//OVERWRITTEN METHODS

    /**
     * Called at the start to prepare for all of the tests.
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
     * Called at the end to finalize the ending of the tests.
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
     * Tests if the home fragment shows up.
     */
    public void testHomeShowsUp()
    {
        boolean textFound = solo.searchText("View Selected List");
        textFound &= solo.searchText("Refine the List");
        assertTrue("Home fragment showed up.", textFound);
    }

    /**
     * Tests if you can select one of the three lists and view the weapons in it.
     */
    public void testSelectingListWhenEmpty()
    {
        solo.clickOnText("Select a List");
        solo.clickOnText("Wishlist");
        solo.clickOnButton(0);
        boolean textFound = solo.searchText("Your List is Empty");
        assertTrue("Could select a list and press the go to list button.", textFound);
    }

    /**
     * Tests if you can click the image button and add a weapon to one of the lists.
     */
    public void testAddingWeaponToList()
    {
        solo.clickOnText("Select a Weapon Type");
        solo.clickOnText("Sword");
        solo.clickOnButton(1);
        solo.clickOnText("Dark-Drinker");
        solo.clickOnImageButton(0);
        solo.goBack();
        solo.goBack();
        solo.clickOnText("Select a List");
        solo.clickOnText("Favourites");
        solo.clickOnButton(0);
        boolean textFound = solo.searchText("Dark-Drinker");
        assertTrue("Could add weapon to list.", textFound);
    }

    /**
     * Tests if you can then view your list and remove the weapon from it.
     */
    public void testRemovingWeaponFromList()
    {
        solo.clickOnText("Select a List");
        solo.clickOnText("Favourites");
        solo.clickOnButton(0);
        solo.clickOnText("Dark-Drinker");
        solo.clickOnImageButton(0);
        solo.goBack();
        boolean textFound = solo.searchText("Dark-Drinker");
        assertFalse("Can remove weapon from list.", textFound);
    }

    /**
     * Tests if you can refine a list with the spinners and view the resulting list.
     */
    public void testRefiningList()
    {
        solo.clickOnText("Select a Rarity");
        solo.clickOnText("Exotic");
        solo.clickOnText("Select an Equip Slot");
        solo.clickOnText("Heavy");
        solo.clickOnText("Select a Weapon Type");
        solo.clickOnText("Sword");
        solo.clickOnButton(1);
        boolean textFound = solo.searchText("Bolt-Caster");
        assertTrue("Could select all properties of a weapon and " +
                "press refine button and get results.", textFound);
    }

    /**
     * Tests if you can click the button in the menu and go to the info fragment.
     */
    public void testMoveToInfo()
    {
        solo.clickOnMenuItem("App Info");
        boolean textFound = solo.searchText("Submit a bug report");
        assertTrue("Can switch from main to info activity.", textFound);
    }

    /**
     * Tests if you can log out and back in again.
     */
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

    /**
     * Tests for validation.
     */
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
