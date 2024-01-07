package bicycleBreaks;

/**
 * <h1>IBrake</h1>
 * Interface which provides the basic functionality regarding the breaks of a bicycle.
 *
 * @version 1.0
 * @since 2017-07-22
 */
public interface IBrakes
{

    /**
     * Reduces the speed by a factor based on the input parameter.
     *
     * @param excertedForce the value of the force which is applied to the break.
     * @return the actual speed reduction
     */
    public abstract double getSpeedReduction(Double excertedForce);

    /**
     * Reports if the brakes are broken.
     *
     * @return true if the brakes are broken or false otherwise
     */
    public abstract boolean reportIfBroken(double threshold);
}
