package email.crappy.ssao.ruoka.pojo;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * @author Santeri 'iffa'
 */
public class PojoUtil {
    /**
     * Generates a POJO object from the source JSON file
     *
     * @param dataFile Source JSON file
     * @return Generated POJO object (RuokaJsonObject
     * @throws FileNotFoundException
     */
    public static RuokaJsonObject generatePojoFromJson(File dataFile) throws FileNotFoundException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(dataFile));
        return gson.fromJson(br, RuokaJsonObject.class);
    }
}
