package com.tinyquest.agent.config.datasource;

import org.springframework.transaction.support.TransactionSynchronizationManager;

public class TransactionContext {
    public static boolean isReadOnly() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    }
}
