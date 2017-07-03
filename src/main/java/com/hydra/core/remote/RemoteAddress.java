package com.hydra.core.remote;

import lombok.ToString;


public interface RemoteAddress {

    boolean isSamed(Object address);

    String getAddressByString();
}
