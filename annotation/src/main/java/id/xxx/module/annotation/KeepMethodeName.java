package id.xxx.module.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kotlin.annotation.MustBeDocumented;

@MustBeDocumented
@Target({ElementType.METHOD})
public @interface KeepMethodeName {
    /* */
}
