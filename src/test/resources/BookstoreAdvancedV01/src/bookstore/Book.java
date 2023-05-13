package bookstore;

import java.util.Objects;

/**
 * <h1>Book</h1>
 * Class responsible from handling all 
 * the Book items.
 *
 * @version 1.0
 * @since   2017-07-17
 */
public final class Book extends Item {

	/**
	 * Small enum for Packaging Types
	 * 
	 * SIMPLE for 0 and HARD for 1
	 * Intentionally we have avoided updating the constructors
	 * to avoid making the rest of the project dependent upon a 
	 * very specific enum. 
	 * So the constructor takes an int as an argument for the package.
	 * 
	 * The problem: what if I change the order of the enum values later?
	 * Everything is gonna be wrong
	 * 
	 * Also: what happens if the constructor takes sth other than
	 * an available int?
	 * 
	 * @todo TODO For *you*: can you argue if this is the best possible setup?
	 * 
	 * Can *you* find a way to ensure that the rest of the system is intact
	 * and we avoid a dependency? Is it better this way?
	 * What price would you pay and what would you gain?
	 * (there is no silver bullet) 
	 * Is it better -in the end- to just use a PackageType in the constructor and let everyone know?
	 */
	public enum PackageType{
		SIMPLE, HARD;
	}

	private final String author;
	private final PackageType packaging;
	private final int yearPublished;

	/**
	 * Book constructor 
	 * 
	 * @param aTitle a String with the title; should be obligatory
	 * @param anAuthor a String with the author of the book
	 * @param aDate an int with the year
	 * @param aPrice a double with the original price of the book
	 * @param aPackage an int with the id of the respective PackageType value
	 * @param id a unique id for the book; should be unique
	 * 
	 * TODO: see class comments on whether this is a good setup or not.  
	 */
	public Book(String aTitle, String anAuthor, int aDate, double aPrice,
			int aPackage, int id) {
		super(aTitle, aPrice, id); //constructor of Item!!!
		this.author = anAuthor;
		this.yearPublished = aDate;

		int packagePos = 0;
		if (aPackage <= PackageType.values().length - 1) {
			packagePos = aPackage;
		}
		this.packaging = PackageType.values()[packagePos];
	}

	@Override
	public String getDescriptionInDetail() {
		String packString = this.packaging.name().toLowerCase() + " packaging";
		String result = "***Item id: " + id + "***\n" +
				title + "\t\t Price:" + price + "\n" +
				" by " + author + " at " + yearPublished +  "\n" + 
				"with " + packString + ".\n\n";
		return result;
	}


	@Override
	public String getDescriptionShort(){
		String packString = this.packaging.name().toLowerCase() + " packaging";
		return (title + "\nby " + author + " at " + yearPublished 
				+ "\nwith " + packString 	+ "\n");
	}

	@Override
	public double getFinalPrice() {
		return price;
	}


}//end Book
