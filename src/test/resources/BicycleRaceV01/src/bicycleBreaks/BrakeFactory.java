package bicycleBreaks;

/**
 * <h1>BrakeFactory</h1>
 * Factory class responsible for creating the different types of brakes.
 *
 * @version 1.0
 * @since 2017-07-22
 */
public class BrakeFactory
{

    /**
     * Creates all the different types of objects which are responsible for modeling the different kinds of brakes.
     *
     * @return an IBrake object which is responsible for the representation of a brake type.
     */
    public IBrakes constructBrake(String concreteClassName)
    {
        if (concreteClassName.equals("NiceBrakes"))
            return new NiceBrakes();
        else if (concreteClassName.equals("DuperBrakes"))
            return new DuperBrakes();

        System.out.println("If the code got up to here, you passed a wrong argument to BreakFactory");
        return null;
    }
}
