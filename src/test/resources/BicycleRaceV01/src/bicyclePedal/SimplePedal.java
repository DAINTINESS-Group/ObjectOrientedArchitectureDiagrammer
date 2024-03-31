package bicyclePedal;

import java.util.Random;

/**
 * <h1>SimplePedal</h1>
 * Class responsible for the representation of a type of pedals (SimplePedal).
 *
 * @version 1.0
 * @since 2017-07-22
 */
public class SimplePedal implements IPedal
{

    /**
     * Computes the speed by a factor based on the input parameter.
     *
     * @param pedalingRate the rate of the pedaling
     * @return the speed which is achieved
     */
    @Override
    public double getSpeed(double pedalingRate)
    {
        return 35 * pedalingRate;

    }


    /**
     * Picks a random number in range [0,1] and if this random number is larger than 0.5 then returns false. In any other case, return true.Reports if the pedals are broken.
     *
     * @return true if the pedals are broken or false otherwise
     */
    @Override
    public boolean reportIfBroken()
    {
        Random randomDice = new Random();
        if (randomDice.nextDouble() > 0.6)
            return false;
        else
            return true;
    }

}
