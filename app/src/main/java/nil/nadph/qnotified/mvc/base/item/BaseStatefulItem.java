package nil.nadph.qnotified.mvc.base.item;

import nil.nadph.qnotified.mvc.base.ValidStatus;

public interface BaseStatefulItem extends BaseItem {

    ValidStatus getValidState();

    void setValidState(ValidStatus status);
}
