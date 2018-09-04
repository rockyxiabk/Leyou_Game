package com.leyou.game.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.leyou.game.dao.Friend;
import com.leyou.game.dao.PhoneContact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description : 获取手机联系人
 *
 * @author : rocky
 * @Create Time : 2017/7/15 下午3:20
 * @Modified Time : 2017/7/15 下午3:20
 * 读取联系人
 * 分为以下步骤：
 * 1、先读取contacts表，获取ContactsID；
 * 2、再在raw_contacts表中根据ContactsID获取RawContactsID；
 * 3、然后就可以在data表中根据RawContactsID获取该联系人的各数据了。
 */
public class ContactsUtil {
    public static List<PhoneContact> getContractsList(Context context) {
        List<PhoneContact> list = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                PhoneContact contactBean = new PhoneContact();
                //注意 如果某个联系人有多个电话 这里会出现多条 rawId相同 但是phone不同的数据
                int rawId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
                String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String pinyin = PinyinUtil.getPingYin(name);
                String sortString = pinyin.substring(0, 1).toUpperCase();
                contactBean.setName(name);
                if (!TextUtils.isEmpty(phone)) {
                    if (phone.contains("+86")) {
                        phone = phone.replace("+86", "");
                    }
                }
                contactBean.setPhone(phone);
                contactBean.setStatus(Friend.NO_SYSTEM);
                if (sortString.matches("[A-Z]")) {
                    contactBean.setPhoneNameLetter(sortString.toUpperCase());
                } else if (sortString.matches("[0-9]")) {
                    contactBean.setPhoneNameLetter("*");
                } else {
                    contactBean.setPhoneNameLetter("#");
                }
                if (!TextUtils.isEmpty(phone)) {
                    DBUtil.getInstance(context).insertPhoneContact(contactBean);
                    list.add(contactBean);
                }
            }
            cursor.close();
        }
        if (null != list && list.size() > 0) {
            Collections.sort(list, new PhoneContactComparator());
        }
        return list;
    }
}
