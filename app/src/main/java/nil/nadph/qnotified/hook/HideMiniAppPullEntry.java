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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import nil.nadph.qnotified.config.ConfigItems;
import nil.nadph.qnotified.config.ConfigManager;
import nil.nadph.qnotified.util.Initiator;
import nil.nadph.qnotified.util.Utils;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static me.singleneuron.util.QQVersion.QQ_8_2_6;
import static nil.nadph.qnotified.util.Initiator.load;
import static nil.nadph.qnotified.util.Utils.*;

public class HideMiniAppPullEntry extends CommonDelayableHook {
    public static final HideMiniAppPullEntry INSTANCE = new HideMiniAppPullEntry();

    protected HideMiniAppPullEntry() {
        super(ConfigItems.qn_hide_msg_list_miniapp);
    }

    @Override
    protected boolean initOnce() {
        try {
            if (IS_TIM) return false;
            ConfigManager cache = ConfigManager.getCache();
            if (isEnabled()) {
                int lastVersion = cache.getIntOrDefault("qn_hide_msg_list_miniapp_version_code", 0);
                if (getHostVersionCode32() == lastVersion) {
                    String methodName = cache.getString("qn_hide_msg_list_miniapp_method_name");
                    findAndHookMethod(Initiator._Conversation(), methodName, XC_MethodReplacement.returnConstant(null));
                } else {
                    Class<?> con = Initiator._Conversation();
                    for (Method m : con.getDeclaredMethods()) {
                        Class<?>[] ps = m.getParameterTypes();
                        if (ps != null && ps.length > 0) continue;
                        if (!m.getReturnType().equals(void.class)) continue;
                        String name = m.getName();
                        if (name.length() > 1) continue;
                        char c = name.charAt(0);
                        if ('F' <= c && c < 'a')
                            XposedBridge.hookMethod(m, new XC_MethodReplacement(30) {
                                @Override
                                protected Object replaceHookedMethod(MethodHookParam param) {
                                    try {
                                        Method m = (Method) param.method;
                                        m.setAccessible(true);
                                        XposedBridge.invokeOriginalMethod(m, param.thisObject, param.args);
                                    } catch (InvocationTargetException e) {
                                        if (!(e.getCause() instanceof UnsupportedOperationException)) {
                                            log(e);
                                        }
                                    } catch (Throwable t) {
                                        log(t);
                                    }
                                    return null;
                                }
                            });
                    }

                    Class<?> tmp;
                    Class<?> miniapp = null;
                    if (Utils.getHostVersionCode32() >= QQ_8_2_6) {
                        //for 8.2.6
                        miniapp = load("com/tencent/mobileqq/mini/entry/MiniAppDesktop");
                        if (miniapp == null) {
                            tmp = load("com/tencent/mobileqq/mini/entry/MiniAppDesktop$1");
                            if (tmp != null) miniapp = tmp.getDeclaredField("this$0").getType();
                        }
                    } else {
                        //for 818
                        try {
                            miniapp = load("com.tencent.mobileqq.mini.entry.desktop.MiniAppDesktopLayout");
                            if (miniapp == null) {
                                tmp = load("com.tencent.mobileqq.mini.entry.desktop.MiniAppDesktopLayout$1");
                                if (tmp != null) miniapp = tmp.getDeclaredField("this$0").getType();
                            }
                            if (miniapp == null) {
                                tmp = load("com.tencent.mobileqq.mini.entry.desktop.MiniAppDesktopLayout$2");
                                if (tmp != null) miniapp = tmp.getDeclaredField("this$0").getType();
                            }
                        } catch (Exception ignored) {
                        }
                        //for older
                        if (miniapp == null)
                            miniapp = load("com/tencent/mobileqq/mini/entry/MiniAppEntryAdapter");
                        if (miniapp == null)
                            miniapp = load("com/tencent/mobileqq/mini/entry/MiniAppEntryAdapter$1").getDeclaredField("this$0").getType();
                    }
                    XposedBridge.hookAllConstructors(miniapp, new XC_MethodHook(60) {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            String methodName = null;
                            StackTraceElement[] stacks = new Throwable().getStackTrace();
                            for (StackTraceElement stack : stacks) {
                                if (stack.getClassName().contains("Conversation")) {
                                    methodName = stack.getMethodName();
                                    break;
                                }
                            }
                            if (methodName == null)
                                throw new NullPointerException("Failed to get Conversation.?() to hide MiniApp!");
                            ConfigManager cache = ConfigManager.getCache();
                            cache.putString("qn_hide_msg_list_miniapp_method_name", methodName);
                            cache.getAllConfig().put("qn_hide_msg_list_miniapp_version_code", getHostVersionCode32());
                            cache.save();
                            param.setThrowable(new UnsupportedOperationException("MiniAppEntry disabled"));
                        }
                    });
                }
            }
            return true;
        } catch (Exception e) {
            log(e);
        }
        return false;
    }
}
