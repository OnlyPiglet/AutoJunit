import file2class.GenClassLoader;
import generator.TestSourceGenertor;
import log.LoggerHolder;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @ProjectName: ajunit
 * @Package: PACKAGE_NAME
 * @ClassName: GjunitMoji
 * @Author: 吴成昊
 * @Description:
 * @Date: 2019/8/12 10:24
 * @Version: 0.1
 */
@Mojo(name="genjunit",requiresProject = true,defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES,threadSafe = true)
public class GjunitMojo extends AbstractMojo {

    private final static GenClassLoader gcl = new GenClassLoader();
    private String[] extension = {"class"};


    GjunitMojo(){
        super();
        if(LoggerHolder.log == null){
            synchronized (GjunitMojo.class){
                if(LoggerHolder.log==null){
                    LoggerHolder.log = this.getLog();
                }
            }
        }
    }

    @Parameter(required = true,property = "testSource",defaultValue = "${project.build.testSourceDirectory}")
    private File testSource;

    @Parameter(required = false,property = "sourceClassDir",defaultValue = "${project.build.outputDirectory}")
    private File sourceClassDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if(!testSource.exists()){
            try {
                FileUtils.forceMkdir(testSource);
            }catch (IOException ioe){
                ioe.printStackTrace();
                LoggerHolder.log.error(ioe.getMessage(),ioe);
            }
        }

        List<File> class_files = (List<File>)FileUtils.listFiles(sourceClassDir, extension, true);

        for(File classfile : class_files){
            try {
                String absClassfilePath = classfile.getAbsoluteFile().getAbsolutePath();
                LoggerHolder.log.info("need to expired" + absClassfilePath);
                Class clazz = gcl.LoadClass(absClassfilePath);
                assert(clazz != null);
                //generator testsource  by clazz
                LoggerHolder.log.info(testSource.getAbsolutePath());
                TestSourceGenertor tsg = new TestSourceGenertor(testSource);
                tsg.generatorTestSource(clazz);

            }catch (ClassNotFoundException cnfe){
                LoggerHolder.log.error(cnfe.getMessage(),cnfe);
            }
        }


    }
}
