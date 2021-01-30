package nil.nadph.qnotified.mvc.base.item;

import androidx.annotation.NonNull;

public interface TextItem extends BaseItem {

    @NonNull
    CharSequence getText();

    void setText(@NonNull CharSequence text);
}
