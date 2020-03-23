package im.supai.supaimarketing.model;

/**
 * Created by viator42 on 15/7/23.
 */
public class Appeal
{
    private String oldTel;
    private String newTel;
    private String name;
    private String address;
    private String imie;
    private String verifyMsg;

    public String getOldTel() {
        return oldTel;
    }

    public void setOldTel(String oldTel) {
        this.oldTel = oldTel;
    }

    public String getNewTel() {
        return newTel;
    }

    public void setNewTel(String newTel) {
        this.newTel = newTel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getVerifyMsg() {
        return verifyMsg;
    }

    public void setVerifyMsg(String verifyMsg) {
        this.verifyMsg = verifyMsg;
    }

    public boolean uploadVerify()
    {
        boolean result = true;
        if(this.oldTel == null || this.oldTel.isEmpty())
        {
            this.verifyMsg = "旧手机号不能为空";
            return false;

        }
        if(this.newTel == null || this.newTel.isEmpty())
        {
            this.verifyMsg = "新手机号不能为空";
            return false;

        }
        if(this.name == null || this.name.isEmpty())
        {
            this.verifyMsg = "姓名不能为空";
            return false;

        }
        if(this.address == null || this.address.isEmpty())
        {
            this.verifyMsg = "地址不能为空";
            return false;

        }

        return result;
    }
}
