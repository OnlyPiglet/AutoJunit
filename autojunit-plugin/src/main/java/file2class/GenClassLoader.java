package file2class;

import log.LoggerHolder;

import java.io.*;

/**
 * @Author wuchenghao
 * @ClassName GenClassLoader
 * @Description load the maven target source class
 * @Date 2019/10/2 14:39
 */
public class GenClassLoader extends ClassLoader {

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

            String real_absClassPath = absClassPath.replace("\\.",System.lineSeparator());

            clazz = this.getParent().loadClass(real_absClassPath);

        }

        return clazz;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{

        try{

            byte [] classData = getData(name);

            if(classData==null || classData.length == 0){
                throw new IOException(name + "can't find in the source class target directory");
            }
            else{
                //defineClass方法将字节码转化为类
                return this.defineClass(classData,0,classData.length);
            }

        }catch (IOException e){

            return super.findClass(name);

        }
    }



    private byte[] getData(String classAbsName) throws IOException{
        InputStream in = null;
        ByteArrayOutputStream out = null;
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