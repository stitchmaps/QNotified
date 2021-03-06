/* QNotified - An Xposed module for QQ/TIM
 * Copyright (C) 2019-2021 xenonhydride@gmail.com
 * https://github.com/ferredoxin/QNotified
 *
 * This software is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see
 * <https://www.gnu.org/licenses/>.
 */
package nil.nadph.qnotified.hook;

import android.app.Application;
import android.graphics.Bitmap;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import nil.nadph.qnotified.util.LicenseStatus;

import static nil.nadph.qnotified.util.Initiator._TroopPicEffectsController;
import static nil.nadph.qnotified.util.Utils.*;

public class ShowPicGagHook extends CommonDelayableHook {

    private static final ShowPicGagHook self = new ShowPicGagHook();

    private ShowPicGagHook() {
        super("qn_gag_show_pic");
    }

    public static ShowPicGagHook get() {
        return self;
    }

    @Override
    public boolean initOnce() {
        try {
            Method showPicEffect = null;
            for (Method m : _TroopPicEffectsController().getDeclaredMethods()) {
                Class[] argt = m.getParameterTypes();
                if (argt.length > 2 && argt[1].equals(Bitmap.class)) {
                    showPicEffect = m;
                    break;
                }
            }
            XposedBridge.hookMethod(showPicEffect, new XC_MethodHook(49) {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (LicenseStatus.sDisableCommonHooks) return;
                    if (!isEnabled()) return;
                    param.setResult(null);
                }
            });
            return true;
        } catch (Throwable e) {
            log(e);
            return false;
        }
    }

    @Override
    public boolean isValid() {
        Application app = getApplication();
        return app == null || !IS_TIM;
    }
}
