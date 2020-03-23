package im.supai.supaimarketing.model;

import im.supai.supaimarketing.util.StaticValues;

/**
 * Created by viator42 on 15/10/29.
 * 应用各个模块的首次开启
 * // 首次开启  //0: 首次开启 1: 不是首次开启
 * // 首页 店铺 打印机 售货员
 */

public class FirstOpen {
    private int all = StaticValues.FIRST_OPEN_Y;
    private int mainPage = StaticValues.FIRST_OPEN_Y;
    private int store = StaticValues.FIRST_OPEN_Y;
    private int printer = StaticValues.FIRST_OPEN_Y;
    private int sales = StaticValues.FIRST_OPEN_Y;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    public int getMainPage() {
        return mainPage;
    }

    public void setMainPage(int mainPage) {
        this.mainPage = mainPage;
    }

    public int getPrinter() {
        return printer;
    }

    public void setPrinter(int printer) {
        this.printer = printer;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }
}
