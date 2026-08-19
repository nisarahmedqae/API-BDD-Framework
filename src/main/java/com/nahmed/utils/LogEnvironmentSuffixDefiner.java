package com.nahmed.utils;

import ch.qos.logback.core.PropertyDefinerBase;

public class LogEnvironmentSuffixDefiner extends PropertyDefinerBase {

    @Override
    public String getPropertyValue() {
        return RuntimeConfigResolver.resolveEnvironmentSuffix();
    }

}
