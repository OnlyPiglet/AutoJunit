package generator;

import com.sun.codemodel.*;
import log.LoggerHolder;
import org.apache.commons.lang3.ClassUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @Author wuchenghao
 * @ClassName TestSourceGenertor
 * @Description generate the source of testjunit
 * @Date 2019/10/2 14:39
 */
public class TestSourceGenertor {

    private File testSourceDirFile;

    public TestSourceGenertor(File testSourceDirFile){
        this.testSourceDirFile =  testSourceDirFile;
    }

    public void generatorTestSource(Class<?> clazz){


        JCodeModel cm = new JCodeModel();

        JPackage p = cm._package(ClassUtils.getPackageName(clazz));

        try {

            JDefinedClass c =  p._class(ClassUtils.getShortClassName(clazz)+"Test");

            Method[] methods = clazz.getDeclaredMethods();


            for(Method m : methods){

                Class<?> [] clazzs = m.getExceptionTypes();

                for(Class<?> clazzd : clazzs){

                    JMethod jm = c.method(JMod.PUBLIC,void.class,m.getName()+"With"+clazzd.getSimpleName()+"Test");
                    JAnnotationUse jau = jm.annotate(Test .class);
                    jau.param("expected",clazzd);

                }


            }

            JMethod jmb = c.method(JMod.PUBLIC,void.class,"before");
            jmb.annotate(Before.class);

            JMethod jma = c.method(JMod.PUBLIC,void.class,"after");
            jma.annotate(After.class);

            cm.build(testSourceDirFile);

        }catch (JClassAlreadyExistsException jaee){
            //ingore
        }
        catch (IOException e){

            LoggerHolder.log.error(e.getMessage(),e);

        }



    }


}
