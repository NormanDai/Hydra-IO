package com.hydra.core.invoke;

import com.google.common.util.concurrent.FutureCallback;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import java.util.concurrent.CountDownLatch;

@Getter
@Setter
public class TaskFutureCallback implements FutureCallback<Object>{

    private  Object result = null;

    private Throwable throwable;

    private CountDownLatch downLatch;

    public void onSuccess(@Nullable Object object) {
        result = object;
        downLatch.countDown();
    }

    public void onFailure(Throwable throwable) {
        this.throwable = throwable;
        downLatch.countDown();
    }
}
