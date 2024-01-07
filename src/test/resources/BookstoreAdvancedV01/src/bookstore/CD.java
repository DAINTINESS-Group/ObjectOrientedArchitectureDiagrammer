package bookstore;

/**
 * <h1>CD</h1>
 * Class responsible from handling all the CD items.
 *
 * @version 1.0
 * @since 2017-07-17
 */
public final class CD extends Item
{
    private final String artist;
    private final double discount;


    public CD(String aTitle, double aPrice, String anArtist, double aDiscount, int id)
    {
        super(aTitle, aPrice, id);
        artist = anArtist;
        discount = aDiscount;
    }


    @Override
    public double getFinalPrice()
    {
        return (price - discount);
    }


    @Override
    public String getDescriptionInDetail()
    {
        String result = "***Item id: " + id + "*** \n" +
                        title + "\t\t Price:" + price + "\n" +
                        "by " + artist + "\n" +
                        "final price: " + getFinalPrice() + "\n\n";
        return result;
    }


    @Override
    public String getDescriptionShort()
    {
        return (title + "\n by " + artist);
    }
}
