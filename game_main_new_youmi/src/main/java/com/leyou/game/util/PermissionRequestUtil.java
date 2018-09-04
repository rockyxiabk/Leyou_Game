/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.leyou.game.util;

import android.Manifest;
import android.content.Context;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

/**
 * Description : 申请全限
 *
 * @author : rocky
 * @Create Time : 2017/7/27 上午11:28
 * @Modified By: rocky
 * @Modified Time : 2017/7/27 上午11:28
 */
public class PermissionRequestUtil {

    private Context mContext;
    private PermissionCallback mCallback;

    public PermissionRequestUtil(Context context, PermissionCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    public void request() {
        AndPermission.with(mContext)
                .requestCode(110)
                .permission(
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_SMS
                )
                .callback(this)
                .start();
    }

    @PermissionYes(110)
    public void yes(List<String> permissions) {
        this.mCallback.onSuccessful();
    }

    @PermissionNo(110)
    public void no(List<String> permissions) {
        this.mCallback.onFailure();
    }

    public interface PermissionCallback {
        void onSuccessful();

        void onFailure();
    }

}
