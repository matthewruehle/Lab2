package com.mtruehle.photostreamer;

import java.util.ArrayList;

/**
 * Created by matt on 9/22/15.
 * Contains a method which handles JSON Objects, and returns the image links.
 */
public interface Callback {
    void callback(ArrayList<String> links, boolean success);
}
