package io.github.panapeepo.api.plugin;

import net.dv8tion.jda.api.requests.GatewayIntent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Plugin {

    String id();

    String displayName();

    int version();

    String[] authors() default {};

    String description() default "";

    String website() default "";

    PluginDependency[] depends() default {};

    GatewayIntent[] intents() default {};

    String dataFolder() default "plugins/%PLUGIN_ID%/";

}
