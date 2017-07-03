package com.hydra.core.remote.jgroups;


import com.hydra.core.remote.RemoteAddress;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jgroups.Address;

@Setter
@Getter
@ToString
public class JGroupsAdress implements RemoteAddress{

    private Address address;

    public boolean isSamed(Object address) {
        if(address instanceof Address && null != address){
            if(address.toString().equals(address.toString())){
                return true;
            }
        }
        return false;
    }

    public String getAddressByString() {
        if(null != address){
            return address.toString();
        }
        return null;
    }
}
