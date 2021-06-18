package org.jenkinsci.plugins.fodupload;

import java.util.Base64;

import com.google.gson.Gson;

public class BsiTokenParser {
    public org.jenkinsci.plugins.fodupload.models.BsiToken parseBsiToken(String encodedBsiString) {
        byte[] bsiBytes = Base64.getDecoder().decode(encodedBsiString);
        String decodedBsiString = Base64.getEncoder().encodeToString(bsiBytes);
        Gson gson = new Gson();
        String json = gson.toJson(decodedBsiString);
        System.out.println(json);
        return gson.fromJson(json, org.jenkinsci.plugins.fodupload.models.BsiToken.class);
    }
}
