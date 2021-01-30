package nil.nadph.qnotified.mvc.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * typedef ValidStatus
 */
public class ValidStatus {

    enum State {
        /**
         * Value of item is valid and reasonable.
         */
        VALID,
        /**
         * Value of item is valid but discouraged.
         */
        WARNING,
        /**
         * Value of item is invalid.
         */
        INVALID
    }

    @NonNull
    public State state;
    @Nullable
    public String message;

    public ValidStatus() {
        this.state = State.INVALID;
    }

    public ValidStatus(@NonNull State state, @Nullable String message) {
        this.state = state;
        this.message = message;
    }
}
