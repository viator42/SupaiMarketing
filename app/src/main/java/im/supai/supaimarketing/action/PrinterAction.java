package im.supai.supaimarketing.action;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Base64;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Vector;
import im.supai.supaimarketing.AppContext;
import im.supai.supaimarketing.model.Order;
import im.supai.supaimarketing.model.OrderDetail;
import im.supai.supaimarketing.model.User;
import im.supai.supaimarketing.util.CommonUtils;
import im.supai.supaimarketing.util.EnvValues;
import im.supai.supaimarketing.util.StaticValues;

/**
 * Created by viator42 on 15/8/5.
 */
public class PrinterAction extends BaseAction
{
    /*
    private AppContext appContext;
    private Context context;
    private User user;

    public PrinterAction(Context context, AppContext appContext){
        this.context = context;
        this.appContext = appContext;
        this.user = appContext.getUser();

    }
    public void printOrder(Order order)
    {
        int printCopy = user.getPrintCopy();

        //打印订单
        EscCommand esc = new EscCommand();

        for(int printCount=0; printCount<printCopy; printCount++)
        {
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);

            //店铺名称
            esc.addText(order.getStoreName());
            esc.addPrintAndLineFeed();
            esc.addPrintAndLineFeed();

            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);

            //下单时间
            esc.addText("下单时间: " + CommonUtils.timestampToDatetime(order.getCreateTime()));
            esc.addPrintAndLineFeed();

            esc.addText("订单编号: " + order.getSn());
            esc.addPrintAndLineFeed();

            //买家姓名 电话 地址
            esc.addText("买家姓名: " + order.getCustomerName());
            esc.addPrintAndLineFeed();
            esc.addText("联系电话: " + order.getCustomerTel());
            esc.addPrintAndLineFeed();

            esc.addText("送货地址: " + order.getCustomerAddress());
            esc.addPrintAndLineFeed();

            if(order.getAdditional() != null && !order.getAdditional().isEmpty() && !order.getAdditional().equals("null"))
            {
                esc.addText("订单备注: " + order.getAdditional());
                esc.addPrintAndLineFeed();
            }

            esc.addText("商品信息");
            esc.addPrintAndLineFeed();
            esc.addText("--------------------------------");
            esc.addPrintAndLineFeed();
            ArrayList<OrderDetail> orderDetails = order.getOrderDetails();
            for(int a=0; a<orderDetails.size(); a++)
            {
                OrderDetail orderDetail = orderDetails.get(a);
                esc.addText(Integer.toString(a+1) + ": ");

                esc.addText(orderDetail.getGoodsName());
                esc.addPrintAndLineFeed();

                esc.addText("单价:" + Double.toString(orderDetail.getPrice()));
                esc.addHorTab();
                esc.addText("数量:" + Integer.toString(orderDetail.getCount()));
                esc.addPrintAndLineFeed();

            }
            esc.addText("--------------------------------");
            esc.addPrintAndLineFeed();

            esc.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT);

            esc.addText("总价:" + Double.toString(order.getSummary()));
            esc.addPrintAndLineFeed();

            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);

            esc.addPrintAndLineFeed();
            esc.addPrintAndLineFeed();
            byte qrSize = 0x05;
            esc.addSelectSizeOfModuleForQRCode(qrSize);
            byte correctionLevel = 0x33;
            esc.addSelectErrorCorrectionLevelForQRCode(correctionLevel);
            esc.addStoreQRCodeData(EnvValues.serverPath);
            esc.addPrintQRCode();

            //公司信息
            esc.addPrintAndLineFeed();
            esc.addText("济南速派信息技术有限公司");

            //走纸
            byte emptyLines = 5;
            esc.addPrintAndFeedLines(emptyLines);

            esc.addCutPaper();
        }

        Vector<Byte> datas = esc.getCommand(); //发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = appContext.getmGpService().sendEscCommand(appContext.getmPrinterIndex(), str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(context, GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printImg(Bitmap bitmap, String text)
    {
        //打印订单
        EscCommand esc = new EscCommand();

        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);

        esc.addRastBitImage(bitmap,bitmap.getWidth(),0);

        esc.addPrintAndLineFeed();
        esc.addText(text);

        //走纸
        byte emptyLines = 5;
        esc.addPrintAndFeedLines(emptyLines);
        esc.addCutPaper();

        Vector<Byte> datas = esc.getCommand(); //发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = appContext.getmGpService().sendEscCommand(appContext.getmPrinterIndex(), str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(context, GpCom.getErrorText(r),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

     */

}
