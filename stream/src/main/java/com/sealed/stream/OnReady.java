package com.sealed.stream;

import java.util.ArrayList;
import java.util.List;

public interface OnReady {
    void run(String signalingURL, ArrayList<StunServer> stunServers);
}
