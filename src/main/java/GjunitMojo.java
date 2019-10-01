import file2class.GenClassLoader;
import generator.TestSourceGenertor;
import log.LoggerHolder;
import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import sun.rmi.runtime.Log;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;


@Mojo(name="genjunit",requiresProject = true,defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES,threadSafe = true,requiresDependencyCollection = ResolutionScope.RUNTIME)
public class GjunitMojo extends AbstractMojo {



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
//
    @Parameter(required = false,property = "project",defaultValue = "${project}")
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {


        //---------set a new classload with the dependenies maven classpath -----

        List runtimeClasspathElements = null;
        try {
            runtimeClasspathElements = project.getRuntimeClasspathElements();


        } catch (DependencyResolutionRequiredException e) {
            e.printStackTrace();
        }
        URL[] runtimeUrls = new URL[runtimeClasspathElements.size()];
        for (int i = 0; i < runtimeClasspathElements.size(); i++) {
            String element = (String) runtimeClasspathElements.get(i);
            try {
                runtimeUrls[i] = new File(element).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                LoggerHolder.log.error(e.getMessage(),e);
            }
        }


        URLClassLoader newLoader = new URLClassLoader(runtimeUrls,
                Thread.currentThread().getContextClassLoader());

        Thread.currentThread().setContextClassLoader(newLoader);


        GenClassLoader gcl = new GenClassLoader(newLoader);
        //--------------


        if(!testSource.exists()){
            try {
                FileUtils.forceMkdir(testSource);
            }catch (IOException ioe){
                LoggerHolder.log.error(ioe.getMessage(),ioe);
            }
        }

        List<File> class_files = (List<File>)FileUtils.listFiles(sourceClassDir, extension, true);

        for(File classfile : class_files){
            try {

                LoggerHolder.log.info(classfile.getName());

                String absClassfilePath = classfile.getAbsoluteFile().getAbsolutePath();


                Class clazz = gcl.LoadClass(absClassfilePath);

                //generator testsource  by clazz
                TestSourceGenertor tsg = new TestSourceGenertor(testSource);
                tsg.generatorTestSource(clazz);

            }catch (ClassNotFoundException  cnfe){
                LoggerHolder.log.error(cnfe.getMessage(),cnfe);
            }
        }


    }
}
