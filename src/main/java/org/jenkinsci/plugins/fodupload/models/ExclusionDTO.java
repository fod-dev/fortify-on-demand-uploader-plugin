package org.jenkinsci.plugins.fodupload.models;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serializable;

@SuppressFBWarnings("SE_NO_SERIALVERSIONID")
public class ExclusionDTO implements Serializable {
    @SuppressWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD")
    public String value;
}
