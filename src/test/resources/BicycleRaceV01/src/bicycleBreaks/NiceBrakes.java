package bicycleBreaks;

import java.util.Random;

/**
 * <h1>NiceBrakes</h1>
 * Class responsible for the representation of a type of brakes (NiceBrakes).
 *
 * @version 1.0
 * @since 2017-07-22
 */
public class NiceBrakes implements IBrakes
{

    /**
     * Reduces the speed by a factor based on the input parameter.
     *
     * @param excertedForce the value of the force which is applied to the break.
     * @return the actual speed reduction
     */
    @Override
    public double getSpeedReduction(Double excertedForce)
    {
        return 35 * excertedForce;
    }


    /**
     * Picks a random number in range [0,1] and if this random number is larger than the input threshold then returns false. In any other case, return true.
     */
    @Override
    public boolean reportIfBroken(double threshold)
    {
        if ((threshold > 1.0) || (threshold > 1.0))
        {
            System.out.println("The threshold for breaks' health should be between 0 and 1");
            System.exit(-1);
        }

        Random randomDice = new Random();
        if (randomDice.nextDouble() > threshold)
            return false;
        else
            return true;
    }

}
