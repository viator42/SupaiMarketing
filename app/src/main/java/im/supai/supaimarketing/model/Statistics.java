package im.supai.supaimarketing.model;

/**
 * Created by viator42 on 15/9/13.
 */
public class Statistics
{
    private int productCount;
    private double productValueSum;
    private double unpaidSum;
    private double turnoverToday;
    private double turnoverMonth;
    private double turnoverYear;

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public double getProductValueSum() {
        return productValueSum;
    }

    public void setProductValueSum(double productValueSum) {
        this.productValueSum = productValueSum;
    }

    public double getTurnoverToday() {
        return turnoverToday;
    }

    public void setTurnoverToday(double turnoverToday) {
        this.turnoverToday = turnoverToday;
    }

    public double getTurnoverMonth() {
        return turnoverMonth;
    }

    public void setTurnoverMonth(double turnoverMonth) {
        this.turnoverMonth = turnoverMonth;
    }

    public double getTurnoverYear() {
        return turnoverYear;
    }

    public void setTurnoverYear(double turnoverYear) {
        this.turnoverYear = turnoverYear;
    }

    public double getUnpaidSum() {
        return unpaidSum;
    }

    public void setUnpaidSum(double unpaidSum) {
        this.unpaidSum = unpaidSum;
    }
}
