package bicyclePedal;

/**
 * <h1>IPedal</h1>
 * Interface which provides the basic functionality regarding of the pedals of a bicycle.
 *
 * @version 1.0
 * @since 2017-07-22
 */
public interface IPedal
{

    /**
     * Computes the speed which is provided given the pedaling rate. parameter.
     *
     * @param pedalingRate the rate of the pedaling
     * @return the actual speed which is achieved
     */

    public abstract double getSpeed(double pedalingRate);

    /**
     * Reports if the pedals are broken.
     *
     * @return true if the pedals are broken or false otherwise
     */
    public abstract boolean reportIfBroken();
}
