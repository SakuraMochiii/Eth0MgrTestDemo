## Overview

`Eth0MgrTestDemo`
This system provides the AIDL interface to set the static Ethernet parameters. When connected to the service, the package name is com.wizarpos.wizarviewagentassistant, and the class name is com.wizarpos.wizarviewagentassistant.Eth0MgrService.
Permissions: 
The application declares the following permissions in the manifest：android.permission.CONNECTIVITY_INTERNAL

## Features

- **enableStaticIp**: Enable static IP, true for static ip, else for dhcp.
- **isStaticIp**: true for static ip, else for dhcp.
- **setStaticIp**: Set static IP.
- **getStaticIp**: Get static IP parameters.
