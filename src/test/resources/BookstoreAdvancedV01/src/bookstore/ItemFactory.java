package bookstore;

/**
 * <h1>ItemFactory</h1>
 * Factory class responsible for creating the different types of items.
 *
 * @version 1.0
 * @since 2017-07-17
 */
public class ItemFactory
{
    /*
     * Always bad to do sth like the following.
     *
     * Why? Think what happens if subclasses of Item start evolving. What happens?
     *
     * *DO NOT USE FACTORIES LIKE THIS!* Try two different methods, instead!
     */
    @Deprecated
    public Item createItem(int aType, String aTitle, double aPrice, String anArtist, double aDiscount, String anAuthor, int aDate, int aPackage, int id)
    {

        switch (aType)
        {
            case 1:
                Book newBook = new Book(aTitle, anAuthor, aDate, aPrice, aPackage, id);
                return newBook;
            case 2:
                CD newCD = new CD(aTitle, aPrice, anArtist, aDiscount, id);
                return newCD;
            default:
                System.out.println("Wrong type of item for createItem() -- nothing created");
                return null;
        }
    }


    /**
     * Creates a Book item
     *
     * @param aTitle   book title
     * @param aPrice   price of the book
     * @param anAuthor author of the book
     * @param aPackage 0 for SIMPLE 1 for HARD
     * @param aDate    publication date
     * @param id       book id
     * @return an object of type Book
     */
    public Item createBook(String aTitle, double aPrice, String anAuthor, int aDate, int aPackage, int id)
    {
        Book newBook = new Book(aTitle, anAuthor, aDate, aPrice, aPackage, id);
        return newBook;
    }


    /**
     * Creates a CD item
     *
     * @param aTitle    cd title
     * @param aPrice    price of the cd
     * @param anArtist  author of the cd
     * @param aDiscount discount
     * @param id        cd id
     * @return an object of type CD
     */
    public Item createCD(String aTitle, double aPrice, String anArtist, double aDiscount, int id)
    {
        CD newCD = new CD(aTitle, aPrice, anArtist, aDiscount, id);
        return newCD;
    }
}
