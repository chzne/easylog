package com.easy.logging;

public interface SessionSwitcher {

    public boolean tryStop(Invocation invocation);
}
