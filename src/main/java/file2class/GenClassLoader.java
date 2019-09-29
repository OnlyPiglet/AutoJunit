package file2class;

import log.LoggerHolder;

import java.io.*;

/**
 * @ProjectName: ajunit
 * @Package: PACKAGE_NAME
 * @ClassName: file2class.GenClassLoader
 * @Author: 吴成昊
 * @Description:
 * @Date: 2019/8/12 11:05
 * @Version: 0.1
 */
public class GenClassLoader extends ClassLoader {

    private ClassLoader parent;


    public GenClassLoader(ClassLoader parent){

        super(parent);

    }


    public Class<?> LoadClass(String absClassPath) throws ClassNotFoundException{

        Class<?> clazz = null;

        try {

            clazz = findClass(absClassPath);

        }catch (ClassNotFoundException cnfe){

            //ingore
        }

        if(clazz == null){

            absClassPath = absClassPath.replace("\\.",System.lineSeparator());

            LoggerHolder.log.info(absClassPath);


            clazz = this.getParent().loadClass(absClassPath);

        }

        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{

        try{

            byte [] classData = getData(name);

            if(classData==null || classData.length == 0){

            }
            else{
                //defineClass方法将字节码转化为类
                return this.defineClass(classData,0,classData.length);
            }

        }catch (IOException e){

            return super.findClass(name);

        }
        return null;

    }



    private byte[] getData(String classAbsName) throws IOException{
        InputStream in = null;
        ByteArrayOutputStream out = null;
        LoggerHolder.log.info("classAbsName: "+classAbsName);
        try {
            String location = classAbsName;//.replace("\\",".");
            in=new FileInputStream(location);
            out=new ByteArrayOutputStream();
            byte[] buffer=new byte[2048];
            int len=0;
            while((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);
            }
            return out.toByteArray();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            in.close();
            out.close();
        }
        return null;
    }


}