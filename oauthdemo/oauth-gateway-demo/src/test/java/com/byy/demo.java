package com.byy;


import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class demo {
    @Test
    public void demo1() {
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UyIiwicmVzb3VyY2UxIl0sImV4cCI6MTU5MjA2Mzg3NiwidXNlcl9uYW1lIjoiYWRtaW4iLCJqdGkiOiIxNmVhODNkZi1hYzUwLTQwYzktYjZiYy1lMzRhMDc5YzA4OGQiLCJjbGllbnRfaWQiOiJjbGllbnQtYSIsInNjb3BlIjpbInJlYWRfdXNlcl9pbmZvIiwid3JpdGUiLCJhbGwiXX0.gkLiOZXfAAQBIFDK_1QYf5v_15PP7G-L4nXZ6IKDSQlArkgPqd7aeKRjszfEeEG0VnU60229qcXu9DTIXrGswDMDVlLTowh7zTX2cp5xohGcRC4VcfohvHFMu3qNfSQpKURWDxy65H0X2kdBmEfJIH2Xm9jOLYcabPiR7Gxn5BRFtbLew9bXMxRXMZwAUO4ubQfqRnLLp-YCsFSP-Kx99uBFIBvC7GnWB1fplLCKFSzQ6CCh19ZI4JdhhDkg2iFVS9mgOdUgsAg78wC7yrdn_TNs13k1NNjgWLbsnGxlj_07Lpr6bg7Py0uPu8fkBhaS6d3mmlngTYPso6-9RnFeHQ";
        String verifierKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm8Qo/azKU/65pewxypgE\n" +
                "mw6d7EP8X79JeHBjgpBm+yyYoD7FvSVyguSSvpyKiETmKrL66iEMrpIgnNjKOauM\n" +
                "gqQ7PiznDLdKlMQaVOcX5NAgHEKZzPwB/FYAjzLfCzcRzaeJdXFDba5ysyFP3onX\n" +
                "x8zqK1g4o/flcMweEGJA8s+ACRZtb+7e9zBhYP3dqVoL3ykSOVHr2qeA+e1RS4pq\n" +
                "dSBusGzQN9wCDlXG4b3XYf2o6zOjUeMo0TSLPwNY91DBMjmxobRMi27NDDFsviq7\n" +
                "6Xi3C+sHkaHLz4jlfiU9OYRsNV76eDWpwv0GWO5U64hcwJ7nVrQLP57dXUC9Yhv+\n" +
                "SQIDAQAB\n" +
                "-----END PUBLIC KEY-----";
        try {
            //
            SignatureVerifier verifier = new RsaVerifier(verifierKey);
            Jwt jwt = null;

            jwt = JwtHelper.decodeAndVerify(token, verifier);
            String claimsStr = jwt.getClaims();
            Map claims = JSON.parseObject(claimsStr, HashMap.class);
            Object user_name = claims.get("user_name");
            System.out.println(user_name);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }
}
