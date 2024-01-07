package race;

import java.util.ArrayList;
import java.util.Random;

import bicycleBody.Bicycle;

/**
 * <h1>RaceWithStages</h1>
 * Class responsible for the representation of a simple race.
 *
 * @version 1.0
 * @since 2017-07-22
 */
public class RaceWithStages implements IRace
{


    private int                stagePart         = 0;
    private int                totalStageParts   = 4;
    /*We suppose for simplicity that all the parts
     *of the stage have the same length
     */
    private double             stagePartDistance = 2000;
    private ArrayList<Bicycle> contestants;


    public RaceWithStages()
    {
        contestants = new ArrayList<Bicycle>();
    }


    private void nextPart()
    {
        stagePart++;
    }


    private boolean hasNextStage()
    {
        if (stagePart < totalStageParts)
            return true;

        return false;
    }


    /**
     * Sets the rate of the pedaling to a random value in range [0,40].
     */
    private void changePedaling(Bicycle contestant)
    {
        Random randomDice = new Random();
        double rate       = 40 * randomDice.nextDouble();
        contestant.setPedaling(rate);
        System.out.println("Contestant " + contestant.getName() + " pedals with rate " + String.format("%.2f", rate));
    }


    /**
     * Sets the force which is applied to the breaks to a random value in range [0,30].
     */
    private void changeBreaks(Bicycle contestant)
    {
        Random randomDice = new Random();
        double force      = 30 * randomDice.nextDouble();
        contestant.setBraking(force);
        System.out.println("Contestant " + contestant.getName() + " breaks with force " + String.format("%.2f", force));
    }


    /**
     * Decides the winner of the race based on the time
     */
    public void decideWinner()
    {
        double  minTime = Double.MAX_VALUE;
        Bicycle winner  = null;

        if (contestants.size() == 0)
        {
            System.out.println("There is no winner for this race...");
            return;
        }

        for (Bicycle contestant : contestants)
        {
            if (contestant.getTimeRun() < minTime)
            {
                minTime = contestant.getTimeRun();
                winner = contestant;
            }
        }
        System.out.println("Winner Details");
        winner.reportDetails();
    }


    /**
     * Executes the race
     */
    @Override
    public void runRace()
    {
        while (hasNextStage())
        {
            System.out.println("====================\nPart: "
                               + stagePart + "\n");
            ArrayList<Bicycle> brokenContestants = new ArrayList<Bicycle>();
            for (Bicycle bm : contestants)
            {
                changePedaling(bm);
                changeBreaks(bm);
                bm.computeTime(stagePartDistance);
                //Mark broken contestant
                if (bm.reportIfDamageExists())
                    brokenContestants.add(bm);
                //report contestant's details
                bm.reportDetails();
            }
            //Remove the contestants that are broken
            for (Bicycle brokenContestant : brokenContestants)
            {
                contestants.remove(brokenContestant);
            }
            nextPart();
        }
        System.out.println("End of RaceWithStages\n\n");
        decideWinner();
    }


    /**
     * Creates the vehicles that are going to participate in the race.
     */
    @Override
    public void setupVehicle(String pedalType, String brakeType, String name, double velocity)
    {
        Bicycle bicycleConstructed = new Bicycle(pedalType, brakeType, name);
        bicycleConstructed.setOriginalVelocity(velocity);
        System.out.println(name + ": velocity at start is: " + bicycleConstructed.getVelocity());
        contestants.add(bicycleConstructed);
    }


    /**
     * Helper method, constructing 3 bicycles to quickly setup a race between the vehicles.
     */
    @Override
    public void setupVehicles()
    {
        Bicycle hsyxhRoda = new Bicycle("SimplePedal", "NiceBrakes", "Hsuxi Roda");
        hsyxhRoda.setOriginalVelocity(50);
        System.out.println("Hsyxh Roda: velocity at start is: " + hsyxhRoda.getVelocity());
        contestants.add(hsyxhRoda);

        Bicycle afoniRoda = new Bicycle("ZuperPedal", "NiceBrakes", "Afoni Roda");
        afoniRoda.setOriginalVelocity(40);
        System.out.println("Afoni Roda: velocity at start is: " + afoniRoda.getVelocity());
        contestants.add(afoniRoda);

        Bicycle plousiaRoda = new Bicycle("ZuperPedal", "DuperBrakes", "Plousia Roda");
        plousiaRoda.setOriginalVelocity(70);
        System.out.println("Plousia Roda: velocity at start is: " + plousiaRoda.getVelocity());
        contestants.add(plousiaRoda);
    }

}
