package race;

/**
 * <h1>RaceTypeFactory</h1>
 * Class responsible for creating different types of objects that represent a race. the Book items.
 *
 * @version 1.0
 * @since 2017-07-24
 */
public class RaceTypeFactory
{

    /**
     * @return IRace object responsible for modeling the conditions of a race.
     */
    public IRace createRaceWithStages()
    {
        return new RaceWithStages();
    }
}
