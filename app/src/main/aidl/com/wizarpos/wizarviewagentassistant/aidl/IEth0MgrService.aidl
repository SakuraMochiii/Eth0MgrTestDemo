// Eth0MgrService.aidl
package com.wizarpos.wizarviewagentassistant.aidl;

// Declare any non-default types here with import statements
import com.wizarpos.wizarviewagentassistant.aidl.IpBean;

interface IEth0MgrService {
    void enableStaticIp(boolean isStaticIp);// true for static ip, else for dhcp.
    boolean isStaticIp();
    boolean setStaticIp(in IpBean ipBean);
    IpBean getStaticIp();
}