package com.example.practice;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.*;
import com.ibm.watson.language_translator.v3.model.Languages;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;

public class LanguageTranslation {
    public static LanguageTranslator authentication(String apikey, String url, String version) {
        //Authenticates the connection with an apikey
        IamAuthenticator authenticator = new IamAuthenticator(apikey);
        LanguageTranslator languageTranslator = new LanguageTranslator(version, authenticator);
        languageTranslator.setServiceUrl(url);

        //Removes the SSL from the connection
        HttpConfigOptions configOptions = new HttpConfigOptions.Builder()
                .disableSslVerification(true)
                .build();
        languageTranslator.configureClient(configOptions);
        return languageTranslator;

    }
    public static Map<String, String> getLanguages(){
        //Authenticate to get LanguageTranslator
        LanguageTranslator lt = authentication("NKp_FQ3HAHhN6eC36fc7kMfWieKBxwAVHWpG-lsIzl6b", "https://api.us-east.language-translator.watson.cloud.ibm.com", "2018-05-01");

        //List the languages available through the IBM API
        Languages languages = lt.listLanguages()
                .execute().getResult();
        String languageString = languages.toString();

        //Create array to store these language
        Map<String, String> languageMap = new HashMap<>();

        //Create regex pattern to match the language code
        Pattern p1 = Pattern.compile("\"language\": \"\\p{Alpha}*\"");
        Matcher m1 = p1.matcher(languageString);

        //Create a regex pattern to match the language name
        Pattern p2 = Pattern.compile("\"language_name\": \"\\p{Alpha}*\"");
        Matcher m2 = p2.matcher(languageString);

        //Create a map of name code pairs
        while(m1.find() && m2.find()) {
            String code = m1.group();
            code = code.substring(13,code.length()-1);
            String name = m2.group();
            name = name.substring(18, name.length()-1);
            languageMap.put(name, code);
        }

        return languageMap;
    }
    public static String translate(String message, String from, String to, Map<String, String> languageMap, LanguageTranslator lt) {
        //Convert the from and to to recognized format
        String codeFrom = languageMap.get(from);
        String codeTo = languageMap.get(to);
        String translateCode = codeFrom + "-" + codeTo;

        //Translate
        TranslateOptions translateOptions = new TranslateOptions.Builder()
                .addText(message)
                .modelId(translateCode)
                .build();

        TranslationResult result = lt.translate(translateOptions)
                .execute().getResult();

        //Create regex pattern to match the translation result
        Pattern p = Pattern.compile("\"translation\": \".*\"");
        Matcher m = p.matcher(result.toString());
        String trueResult = "No Result";
        if (m.find()) {
            trueResult = m.group();
            trueResult = trueResult.substring(16,trueResult.length()-1);
        }

        return trueResult.toString();
    }
    public static String translate(String message, String from, String to) {
        LanguageTranslator languageTranslator  = authentication("NKp_FQ3HAHhN6eC36fc7kMfWieKBxwAVHWpG-lsIzl6b", "https://api.us-east.language-translator.watson.cloud.ibm.com", "2018-05-01");
        Map<String, String> languageMap = getLanguages();
        return translate(message, from, to, languageMap, languageTranslator);
    }
    public static void translateXML(InputStream is, String from, String to) throws IOException {
        //Read in the XML file from the file path
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));

        String line = buf.readLine();
        StringBuilder doc = new StringBuilder();

        while(line != null){
            doc.append(line).append("\n");
            line = buf.readLine();
        }

        //Create regex patterns for the string
        Pattern p2 = Pattern.compile(">(\\p{Alpha}+ )*\\p{Alpha}+");
        Matcher m2 = p2.matcher(doc.toString());

        //Parse the regex and translate the strings
        String text;
        Map<String, String> xmlMap = new HashMap<>();
        while (m2.find()) {
            text = m2.group();
            text = text.substring(1);
            xmlMap.put(text, text);
        }

        //Translate the text and Replace the text with the new translation
        String docString = doc.toString();
        String translated;
        for (Map.Entry<String, String> entry : xmlMap.entrySet()) {
            translated = translate(entry.getValue(), from, to);
            entry.setValue(translated);
            docString = docString.replaceFirst(entry.getKey(), entry.getValue());
        }

        //Writes this new text to the file
        java.io.FileWriter fw = new java.io.FileWriter("my-file.xml");
        fw.write(docString);
        fw.close();

    }
}

