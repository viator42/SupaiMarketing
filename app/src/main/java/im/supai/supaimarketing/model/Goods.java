package im.supai.supaimarketing.model;

import java.io.Serializable;

/**
 * Created by viator42 on 15/3/24.
 * 商品 指某一种类的商品
 */
public class Goods
{
    int id;
    String category;
    int categoryid;
    String name;
    String rccode;
    double price;
    String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRccode() {
        return rccode;
    }

    public void setRccode(String rccode) {
        this.rccode = rccode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
