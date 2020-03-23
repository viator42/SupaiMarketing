package im.supai.supaimarketing.action;

import android.content.Context;
import android.content.SharedPreferences;

import im.supai.supaimarketing.model.Store;
import im.supai.supaimarketing.model.User;

public class RefAction {
    public void setUser(Context context, User user)
    {
        //用户信息写入ref
        SharedPreferences ref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putLong("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("username", user.getUsername());
        editor.putString("tel", user.getTel());
        editor.putString("icon", user.getIconUrl());
        editor.putString("address", user.getAddress());
        editor.putString("area", user.getArea());
        editor.putString("sn", user.getSn());
        editor.putInt("passtype", user.getPasstype());
        editor.putLong("clerk_of", user.getClerkOf());
        editor.putInt("status", user.getStatus());
        editor.putInt("print_copy", user.getPrintCopy());

        editor.commit();

    }

    public void getUser()
    {

    }

    //店铺信息
    public void setStore(Context context, Store store)
    {
        SharedPreferences storeRef = context.getSharedPreferences("store", Context.MODE_PRIVATE);

        //店铺信息保存到ref
        SharedPreferences.Editor editor = storeRef.edit();
        editor.putLong("id", store.getId());
        editor.putString("name", store.getName());
        editor.putString("area", store.getArea());
        editor.putString("address", store.getAddress());
        editor.putString("logo", store.getLogoUrl());
        editor.putString("description", store.getDescription());
        editor.putFloat("longitude", (float) store.getLongitude());
        editor.putFloat("longitude", (float) store.getLatitude());
        editor.putInt("storage_warning", store.getStorageWarning());

        editor.commit();

    }

    public void getStore()
    {

    }

    //首次开启
    public void setFirstOpen(Context context, String key, int value)
    {
        SharedPreferences firstOpenRef = context.getSharedPreferences("firstOpen", Context.MODE_PRIVATE);
        if(firstOpenRef != null)
        {
            SharedPreferences.Editor editor = firstOpenRef.edit();
            editor.putLong(key, value);

            editor.commit();

        }

    }
}
