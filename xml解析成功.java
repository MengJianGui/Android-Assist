package cs;
import java.io.IOException;
import java.io.InputStream;

import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.codec.binary.Base64;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;

import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


import javax.xml.parsers.DocumentBuilderFactory;

public class Htt {
    private static DocumentBuilderFactory factory = DocumentBuilderFactory
            .newInstance();

    public static void main(String[] args)  throws Exception {

        String developerServer = "http://10.20.4.225:8080/client/api";
        String ApiKey = "8R_RGeSrsD-W5g24KdAVz-dheM2piH-cBwy72e1xHOJ1w-n4vHFCNlM0AkAVHtDoSUttYF_PTOlll3L6jqtcTQ";
        String s_secretKey = "W4lv2e4szrZSKPc5wTLioW0tWC0lSbFIEVQqJR5XM8x8m_u1VmJ2GCTh_k9ObQ1SpyqARUIwaePDhrdYrWW33A";

        String encodedApiKey = URLEncoder.encode(ApiKey, "UTF-8");
        String urlold = "apikey=" + encodedApiKey + "&command=getTimeUser";
        urlold = urlold.toLowerCase();
        String signature = signRequest(urlold, s_secretKey);
        String encodedSignature = URLEncoder.encode(signature, "UTF-8");
        System.out.println(encodedSignature);

        String url = developerServer + "?command=getTimeUser&apikey="
                + encodedApiKey + "&signature=" + encodedSignature;
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        System.out.println(url);
        int responseCode = client.executeMethod(method);

        if (responseCode == 200) {
           InputStream is = method.getResponseBodyAsStream();
           Map<String, String> success = getSingleValueFromXML(is,
                  new String[] { "food" });
            System.out.print(success.get("food"));
        } else {
            System.out.print("food1");
        }
    }

    public static String signRequest(String request, String secretkey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec keySpec = new SecretKeySpec(secretkey.getBytes(),  "HmacSHA1");
        mac.init(keySpec);
        mac.update(request.getBytes());
        byte[] encryptedBytes = mac.doFinal();
        // System.out.println("HmacSHA1 hash: " + encryptedBytes);
        return new String(Base64.encodeBase64(encryptedBytes));
    }
    public static Map<String, String> getSingleValueFromXML(InputStream is,
            String[] tagNames) {
        Map<String, String> returnValues = new HashMap<String, String>();
        try {
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(is);
            Element rootElement = doc.getDocumentElement();
            for (int i = 0; i < tagNames.length; i++) {
                NodeList targetNodes = rootElement
                        .getElementsByTagName(tagNames[i]);
                if (targetNodes.getLength() <= 0) {
                    // s_logger.error("no " + tagNames[i] +
                    // " tag in XML response...returning null");
                } else {
                    returnValues.put(tagNames[i], targetNodes.item(0)
                            .getTextContent());
                }
            }
        } catch (Exception ex) {
            // s_logger.error("error processing XML", ex);
        }
        return returnValues;
    }
}
