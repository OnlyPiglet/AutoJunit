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

import org.apache.maven.plugin.logging.Log;

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

    private Log log;

    public GjunitMojo(){
        log = this.getLog();
    }

    @Parameter(required = true,property = "testSource",defaultValue = "${project.build.testSourceDirectory}")
    private File testSource;

    @Parameter(required = false,property = "sourceClassDir",defaultValue = "${project.build.outputDirectory}")
    private File sourceClassDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        log.info("testSource: " + testSource.getAbsolutePath());


        if(!testSource.exists()){
            try {
                FileUtils.forceMkdir(testSource);
            }catch (IOException ioe){
                ioe.printStackTrace();
                log.error(ioe.getMessage(),ioe);
            }
        }

        List<File> class_files = (List<File>)FileUtils.listFiles(sourceClassDir, extension, true);


        for(File classfile : class_files){
            try {
                log.info(classfile.getAbsolutePath().replace(".class",""));
                gcl.findClass(classfile.getAbsolutePath().replace(".class","")).getSimpleName();
//                log.info(gcl.findClass(classfile.getAbsolutePath()).getSimpleName());
            }catch (ClassNotFoundException cnfe){
                log.error(cnfe.getMessage(),cnfe);
            }
        }


    }
}
