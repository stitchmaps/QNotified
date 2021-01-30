package nil.nadph.qnotified.mvc.base.item;

import androidx.annotation.Nullable;

public interface BaseItem {

    @Nullable
    String getId();

    void setId(@Nullable String id);

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
