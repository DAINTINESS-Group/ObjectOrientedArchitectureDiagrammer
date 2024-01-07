package model;

/**
 * <h1>OrderItem</h1>
 * Class responsible for handling the information about an order.
 *
 * @version 1.0
 * @since 2017-07-22
 */
public class OrderItem
{

    //Id	DishName	ChefName	TimeToMake	Stars
    private int      id;
    private Dish     orderDish;
    private Employee orderEmp;
    private String   dishName;
    private String   empName;
    private double   timeToMake;
    private int      stars;

//	public OrderItem() {
//		// TODO Auto-generated constructor stub
//	}


    public OrderItem(int i, String d, String e, double t, int s)
    {
        i = id;
        dishName = d;
        empName = e;
        timeToMake = t;
        stars = s;

        orderEmp = null;
        orderDish = null;
    }


    public Dish getOrderDish()
    {
        return orderDish;
    }


    public void setOrderDish(Dish orderDish)
    {
        this.orderDish = orderDish;
    }


    public Employee getOrderEmp()
    {
        return orderEmp;
    }


    public void setOrderEmp(Employee orderEmp)
    {
        this.orderEmp = orderEmp;
    }


    public String getDishName()
    {
        return dishName;
    }


    public void setDishName(String dishName)
    {
        this.dishName = dishName;
    }


    public String getEmpName()
    {
        return empName;
    }


    public void setEmpName(String empName)
    {
        this.empName = empName;
    }


    public double getTimeToMake()
    {
        return timeToMake;
    }


    public int getStars()
    {
        return stars;
    }


}
