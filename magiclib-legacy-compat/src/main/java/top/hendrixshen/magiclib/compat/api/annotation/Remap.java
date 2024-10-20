package top.hendrixshen.magiclib.compat.api.annotation;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MagicLibMixinPlugin auxiliary annotation.
 *
 * <p>Used to manually obfuscate class/method/field names with unspecified values.
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Remap {
    /**
     * Obfuscation name.
     *
     * @return Obfuscated name.
     */
    String value();

    /**
     * Keep unmapped methods to ensure compatibility.
     *
     * @return True if unmapped methods are kept.
     */
    boolean dup() default false;
}