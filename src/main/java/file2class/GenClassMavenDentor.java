package file2class;

import log.LoggerHolder;

import java.io.*;

public class GenClassMavenDentor extends ClassLoader {



    public GenClassMavenDentor(ClassLoader parent){

        super(parent);

    }

    public Class<?> loadClass(String absClassPath) throws ClassNotFoundException{
        return findClass(absClassPath);
    }

    protected Class<?> findClass(String name) throws ClassNotFoundException{

        try{


            byte [] classData = getData("/Users/mac/.m2/repository/"+name);

            if(classData==null || classData.length == 0){

            }
            else{
                //defineClass方法将字节码转化为类
                return this.defineClass(classData,0,classData.length);
            }

        }catch (IOException e){
            LoggerHolder.log.error(e.getMessage(),e);
        }

        return super.findClass(name);

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
