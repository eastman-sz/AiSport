// ILocationServiceAIDL.aidl
package com.ai.locationservice;

// Declare any non-default types here with import statements

interface ILocationServiceAIDL {
    /** hook when other service has already binded on it */
    void onFinishBind();
}
