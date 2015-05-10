package email.crappy.ssao.ruoka.pojo;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import email.crappy.ssao.ruoka.activity.MainActivity;

/**
 * @author Santeri 'iffa'
 */
public class PojoUtil {

    /**
     * Generates a POJO object from the source JSON file
     *
     * @param context Context
     * @return Generated POJO object (RuokaJsonObject
     * @throws FileNotFoundException
     */
    public static RuokaJsonObject generatePojoFromJson(Context context) throws FileNotFoundException {
        File dataFile = new File(context.getFilesDir(), MainActivity.FILE_NAME);
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        return gson.fromJson(br, RuokaJsonObject.class);
    }
}
