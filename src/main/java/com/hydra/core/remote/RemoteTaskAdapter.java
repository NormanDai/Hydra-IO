package com.hydra.core.remote;


import com.hydra.core.schedule.EnvironmentParams;

import java.util.List;

public interface RemoteTaskAdapter {

    void process(String taskName , RemoteAddress address, List<RemoteAddress> addressList, EnvironmentParams params);
}
