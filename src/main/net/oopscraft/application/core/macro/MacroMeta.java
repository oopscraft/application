package net.oopscraft.application.core.macro;

import java.lang.annotation.ElementType; 
import java.lang.annotation.Retention; 
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface MacroMeta {
	 String name() default ""; 
	 String syntax() default ""; 
	 String example() default ""; 
}
