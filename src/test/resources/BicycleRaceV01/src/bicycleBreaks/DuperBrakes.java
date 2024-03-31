package bicycleBreaks;

/**
 * <h1>DuperBreaks</h1>
 * Class responsible for the representation of a simple race.
 *
 * @version 1.0
 * @since 2017-07-22
 */
public class DuperBrakes implements IBrakes
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
        return 45 * excertedForce;
    }


    /**
     * @return always false
     */
    @Override
    public boolean reportIfBroken(double threshold)
    {
        //Intentional issue, food for thought: parameter unused!
        return false;
    }

}
