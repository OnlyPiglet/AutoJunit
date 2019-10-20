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
import java.time.Duration;
import java.time.LocalDateTime;

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

            JFieldVar before = c.field(JMod.PRIVATE ,
                    cm.parseType(LocalDateTime.class.getName()), "before", JExpr._null());

            JFieldVar after = c.field(JMod.PRIVATE ,
                    cm.parseType(LocalDateTime.class.getName()), "after", JExpr._null());

            Method[] methods = clazz.getDeclaredMethods();

            for(Method m : methods){

                Class<?> [] clazzs = m.getExceptionTypes();

                if(clazzs.length > 0) {

                    for (Class<?> clazzd : clazzs) {

                        JMethod jm = c.method(JMod.PUBLIC, void.class, m.getName() + "With" + clazzd.getSimpleName() + "Test");
                        jm._throws(clazzd.asSubclass(Throwable.class));



                        if(clazzd.getConstructors()[0].getParameterCount()>=1){

                            jm.body()._throw(JExpr._new(cm.parseType(clazzd.getName())).arg("Test"));

                        }else{
                            jm.body()._throw(JExpr._new(cm.parseType(clazzd.getName())));

                        }


                        JAnnotationUse jau = jm.annotate(Test.class);
                        jau.param("expected", clazzd);

                    }

                }else{

                    JMethod jm = c.method(JMod.PUBLIC, void.class, m.getName() + "Test");
                    JAnnotationUse jau = jm.annotate(Test.class);

                }

            }

            JMethod jmb = c.method(JMod.PUBLIC,void.class,"beforeTest");
            jmb.annotate(Before.class);
            JBlock jbb = jmb.body();
            jbb.assign(before,cm.ref(java.time.LocalDateTime.class).staticInvoke("now"));

            JMethod jma = c.method(JMod.PUBLIC,void.class,"afterTest");
            jma.annotate(After.class);
            JBlock jba = jma.body();
            jba.assign(after,cm.ref(java.time.LocalDateTime.class).staticInvoke("now"));
            JVar duration_var = jba.decl(cm.ref(Duration.class),"duration",JExpr._null());
            JVar between_seconds = jba.decl(cm.LONG, "bwtween", JExpr.lit(0));
            jba.assign(duration_var,cm.ref(java.time.Duration.class).staticInvoke("between").arg(before).arg(after));
            jba.assign(between_seconds,duration_var.invoke("getSeconds"));
            JFieldRef jFieldRefSOut =  cm.ref(System.class).staticRef("out");
            jba.invoke(jFieldRefSOut, "print").arg("the method execute: ");
            jba.invoke(jFieldRefSOut, "print").arg(between_seconds);
            jba.invoke(jFieldRefSOut, "print").arg(" s");
            jba.invoke(jFieldRefSOut, "println");

            cm.build(testSourceDirFile);

        }catch (JClassAlreadyExistsException jaee){
            //ingore
        }
        catch (IOException | ClassNotFoundException e){

            LoggerHolder.log.error(e.getMessage(),e);

        }



    }


}
