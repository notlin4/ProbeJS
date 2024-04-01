package com.probejs;

import com.probejs.features.plugin.ProbeJSEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class ProbeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerEvents() {
        ProbeJSEvents.GROUP.register();
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        // lol
        filter.deny("com.probejs");
        filter.deny("org.jetbrains.java.decompiler");
        filter.deny("com.github.javaparser");
    }
}
