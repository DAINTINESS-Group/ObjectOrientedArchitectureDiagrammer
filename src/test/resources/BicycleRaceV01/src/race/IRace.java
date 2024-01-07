package race;

/**
 * <h1>IRace</h1>
 * Interface which provides the basic functionality for conducting the race.
 *
 * @version 1.0
 * @since 2017-07-24
 */
public interface IRace
{

    /**
     * Creates the vehicles that are going to participate in the race.
     */
    public void setupVehicles();

    public void setupVehicle(String pedalType, String brakeType, String name, double velocity);

    /**
     * Executes the race between the vehicles.
     */
    public void runRace();
}
