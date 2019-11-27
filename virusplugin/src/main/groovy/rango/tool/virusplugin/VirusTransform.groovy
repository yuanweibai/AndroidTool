package rango.tool.virusplugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import com.android.build.gradle.internal.pipeline.TransformManager

public class VirusTransform extends Transform {
    private Project mProject;

    public VirusTransform(Project p) {
        this.mProject = p;
    }

    @Override
    public String getName() {
        return "VirusTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false
    }

    private void deleteFiles(File dir, FileFilter filter) {
        if (dir.file) {
            if (filter != null && filter.accept(dir)) {
                dir.delete()
            }
        } else {
            File[] files = dir.listFiles()
            for (File f : files) {
                deleteFiles(f, filter)
            }
        }
    }

    @Override
    public void transform(Context context,
                          Collection<TransformInput> inputs,
                          Collection<TransformInput> referencedInputs,
                          TransformOutputProvider outputProvider,
                          boolean isIncremental) throws IOException, TransformException, InterruptedException {

        inputs.each {
            TransformInput input ->
                input.directoryInputs.each {
                    DirectoryInput directoryInput ->

                        //BuryInject.injectDir(directoryInput.file.absolutePath,"com\\sasas\\dsdsd")
                        // 获取output目录</strong></span>
                        def dest = outputProvider.getContentLocation(directoryInput.name,
                                directoryInput.contentTypes, directoryInput.scopes,
                                Format.DIRECTORY);

                        deleteFiles(directoryInput.file, new FileFilter() {
                            @Override
                            boolean accept(File pathname) {
                                String abspath = pathname.absolutePath.replaceAll("/", ".")
                                for (String s : JarUtil.blackList) {
                                    if (abspath.contains(s)) {
                                        println "ignore class: ${pathname}, for: ${s}"
                                        return true
                                    } else {
                                        println "${pathname} does not contains: ${s}"
                                    }
                                }
                                return false
                            }
                        })

                        println("" + directoryInput.file + " transform" + dest);
                        FileUtils.copyDirectory(directoryInput.file, dest)
                }

                input.jarInputs.each {
                    JarInput jarInput ->

                        def jarName = jarInput.name

                        injectJar(jarInput.file.absolutePath)


                        if (jarName.endsWith(".jar")) {
                            jarName = jarName.substring(0, jarName.length() - 4)
                        }
                        def dest = outputProvider.getContentLocation(jarName,
                                jarInput.contentTypes, jarInput.scopes, Format.JAR)

                        println("jar " + jarInput.file + " transform " + dest)
                        FileUtils.copyFile(jarInput.file, dest)
                }

        }

    }

    public static void injectJar(String path) {
        if (path.endsWith(".jar")) {
            File jarFile = new File(path)

            String jarZipDir = jarFile.getParent() + "/" + jarFile.getName().replace('.jar', '')
            List classNameList = JarUtil.unzipJar(path, jarZipDir)
            jarFile.delete()

            for (String className : classNameList) {
                if (className.endsWith(".class")
                        && !className.contains('R$')
                        && !className.contains('R.class')
                        && !className.contains("BuildConfig.class")) {
                    String tmp = className.substring(0, className.lastIndexOf('.'))
                    tmp = tmp.substring(0, tmp.lastIndexOf('.'))

                    // println "processing package : ${tmp}"
                }
            }
            JarUtil.zipJar(jarZipDir, path)
            FileUtils.deleteDirectory(new File(jarZipDir))
        }
    }
}