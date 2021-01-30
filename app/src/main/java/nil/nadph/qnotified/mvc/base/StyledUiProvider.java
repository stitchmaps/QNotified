package nil.nadph.qnotified.mvc.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import nil.nadph.qnotified.util.UiThread;

/**
 * An abstract user interface which you can control to interact with user.
 * Implementation of this interface may be a Fragment, (part of) an Activity, a Dialog or even a commandline interface.
 * One StyledUiProvider can only attach one AbsConfigSection at a time.
 */
public interface StyledUiProvider {

    /**
     * When in `inline` mode, title may get ignored
     *
     * @return the title of this config section, may be null
     */
    @Nullable
    String getTitle();

    /**
     * Get the current title for this config section.
     *
     * @param title the title of this config section, may be null if you want to hide the title
     */
    @UiThread
    void setTitle(@Nullable String title);

    /**
     * Notice: direct interaction with {@link android.view.View} is discouraged.
     *
     * @return may not be an activity
     */
    @NonNull
    Context getContext();

    @Nullable
    AbsConfigSection getCurrentSection();

}
