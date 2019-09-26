package conversion;

import com.esotericsoftware.reflectasm.FieldAccess;
import org.apache.maven.plugin.logging.Log;

public class DefaultConversion implements Conversion{


    @Override
    public void conversion(Class<?> clazz) {

        FieldAccess.get(clazz);

    }


}
