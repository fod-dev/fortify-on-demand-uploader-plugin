package org.jenkinsci.plugins.fodupload.models.response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class ApplicationAttributeExtended {
    private String name;
    private int id;
    private String value;

    @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @SuppressFBWarnings("UWF_UNWRITTEN_FIELD")
    public String getValue() {
        return value;
    }
}
