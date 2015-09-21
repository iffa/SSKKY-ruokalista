package email.crappy.ssao.ruoka.util;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import email.crappy.ssao.ruoka.model.RuokaJsonObject;

/**
 * @author Santeri 'iffa'
 */
public class PojoUtil {
    public static RuokaJsonObject generatePojoFromJson(Context context) throws FileNotFoundException {
        File dataFile = new File(context.getFilesDir(), "Data.json");
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        return gson.fromJson(br, RuokaJsonObject.class);
    }
}
