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
package nil.nadph.qnotified.script.params;

public class GroupMessageParam {
    /**
     * 发送者id
     */
    public String senderuin;
    /**
     * 群id
     */
    public String uin;
    /**
     * 消息内容
     */
    public String content;

    public GroupMessageParam setSenderUin(String uin) {
        this.senderuin = uin;
        return this;
    }

    public GroupMessageParam setSenderUin(long uin) {
        this.senderuin = uin + "";
        return this;
    }

    public GroupMessageParam setUin(String uin) {
        this.uin = uin;
        return this;
    }

    public GroupMessageParam setUin(long uin) {
        this.uin = uin + "";
        return this;
    }

    public GroupMessageParam setContent(String content) {
        this.content = content;
        return this;
    }

    public GroupMessageParam create() {
        return this;
    }
}
