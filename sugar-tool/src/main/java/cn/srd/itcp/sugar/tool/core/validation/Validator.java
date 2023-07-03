package cn.srd.itcp.sugar.tool.core.validation;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * the validator support chain of responsibility
 *
 * @author wjm
 * @since 2023-06-27 16:41:11
 */
public interface Validator<T> {

    /**
     * get the validated object
     *
     * @return the validated object
     */
    T get();

    /**
     * modify the validated object
     *
     * @param input the validated object
     */
    void set(T input);

    /**
     * validate success or not in current validator node
     *
     * @return validate success or not
     */
    boolean isSuccess();

    /**
     * validate failed or not in current validator node
     *
     * @return validate failed or not
     */
    default boolean isFailed() {
        return !isSuccess();
    }

    /**
     * get next validator node
     *
     * @return next validator node
     */
    Validator<T> getNextValidator();

    /**
     * the validate logic
     *
     * @return validate success or not
     */
    boolean doValidate();

    /**
     * construct the validator chain and do validate
     *
     * @return current validator
     */
    @CanIgnoreReturnValue
    default Validator<T> validate() {
        if (doValidate()) {
            return getNextValidator().validate();
        }
        return this;
    }

    /**
     * do something when validate success on all validator node
     *
     * @param successLogic the success logic
     * @return current validator
     */
    @CanIgnoreReturnValue
    default Validator<T> onSuccess(UnaryOperator<T> successLogic) {
        if (isSuccess()) {
            set(successLogic.apply(get()));
        }
        return this;
    }

    /**
     * see {@link #onSuccess(UnaryOperator)}
     *
     * @param object       the logic param
     * @param successLogic the success logic
     * @param <K>          the logic param type
     * @return current validator
     */
    @CanIgnoreReturnValue
    default <K> Validator<T> onSuccess(K object, Consumer<K> successLogic) {
        if (isSuccess()) {
            successLogic.accept(object);
        }
        return this;
    }

    /**
     * do something when validate failed on any validator node
     *
     * @param failLogic the fail logic
     * @return current validator
     */
    @CanIgnoreReturnValue
    default Validator<T> onFail(UnaryOperator<T> failLogic) {
        if (isFailed()) {
            set(failLogic.apply(get()));
        }
        return this;
    }

    /**
     * see {@link #onFail(UnaryOperator)}
     *
     * @param object    the logic param
     * @param failLogic the fail logic
     * @param <K>       the logic param type
     * @return current validator
     */
    @CanIgnoreReturnValue
    default <K> Validator<T> onFail(K object, Consumer<K> failLogic) {
        if (isFailed()) {
            failLogic.accept(object);
        }
        return this;
    }

    /**
     * do something whether validate success or fail
     *
     * @param logic the logic
     * @return current validator
     */
    @CanIgnoreReturnValue
    default Validator<T> onFinally(UnaryOperator<T> logic) {
        set(logic.apply(get()));
        return this;
    }

    /**
     * see {@link #onFinally(UnaryOperator)}
     *
     * @param object the logic param
     * @param logic  the  logic
     * @param <K>    the logic param type
     * @return current validator
     */
    @CanIgnoreReturnValue
    default <K> Validator<T> onFinally(K object, Consumer<K> logic) {
        logic.accept(object);
        return this;
    }

}
