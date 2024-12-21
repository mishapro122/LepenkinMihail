package org.example.transactionmaneger;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.util.function.Supplier;

public class JdbiTransactionManager implements TransactionManager{
    private final Jdbi jdbi;

    public JdbiTransactionManager(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public <R> R inTransaction(Supplier<R> supplier) {
        return jdbi.inTransaction((Handle handle) -> supplier.get());
    }

    @Override
    public void useTransaction(Runnable runnable) {
        jdbi.useTransaction((Handle handle) -> runnable.run());
    }
}
