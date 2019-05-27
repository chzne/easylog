package com.easy.logging;

import java.util.List;

public interface LoggerSelector {

    public List<InvocationLogger> select(Invocation invocation);
}
