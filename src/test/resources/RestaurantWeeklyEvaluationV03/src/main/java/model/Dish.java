package model;

import java.util.ArrayList;

/**
 * <h1>Dish</h1>
 * Class responsible for handling the information about a dish.
 *
 * @version 1.0
 * @since 2017-07-22
 */
public class Dish
{
    private int                  id;
    private String               dishName;
    private double               gain;
    private double               avgTimeToMake;
    private double               evaluation;
    private ArrayList<OrderItem> orders;


    public Dish()
    {
        orders = new ArrayList<OrderItem>();
    }


    public Dish(int i, String n, double g)
    {
        id = i;
        dishName = n;
        gain = g;
        orders = new ArrayList<OrderItem>();
    }


    public int getId()
    {
        return id;
    }


    public void setId(int id)
    {
        this.id = id;
    }


    public String getDishName()
    {
        return dishName;
    }


    public void setDishName(String dishName)
    {
        this.dishName = dishName;
    }


    public double getGain()
    {
        return gain;
    }


    public void setGain(double gain)
    {
        this.gain = gain;
    }


    public double getAvgTimeToMake()
    {
        return avgTimeToMake;
    }


    public void setAvgTimeToMake(double avgTimeToMake)
    {
        this.avgTimeToMake = avgTimeToMake;
    }


    public double getEvaluation()
    {
        return evaluation;
    }


    public void setEvaluation(double evaluation)
    {
        this.evaluation = evaluation;
    }


    public ArrayList<OrderItem> getOrders()
    {
        return orders;
    }


    public void addOrder(OrderItem o)
    {
        orders.add(o);
    }


    public int computeDishStats()
    {
        double sumStars = 0.0;
        double sumTime  = 0.0;
        int    count    = 0;

        if (orders.isEmpty() == false)
        {
            for (OrderItem o : orders)
            {
                count++;
                sumStars += o.getStars();
                sumTime += o.getTimeToMake();
            }
        }
        avgTimeToMake = sumTime / (count);
        evaluation = sumStars / (count);
        return count;
    }


    public String getShortReport()
    {
        return dishName + "\t Gain: " + gain + "\t avgTime: " + avgTimeToMake + "\t stars:" + evaluation;
    }

}
