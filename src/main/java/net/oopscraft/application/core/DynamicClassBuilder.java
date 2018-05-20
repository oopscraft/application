package net.oopscraft.application.core;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynamicClassBuilder
 * @author chomookun@gmail.com
 */
public class DynamicClassBuilder {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicClassBuilder.class);
	private static final String TEMP_DIR = ".classes";
	
	private String packagePath = null;
	private String className = null;
	private String sourceCode = null;
	
	public DynamicClassBuilder setPackagePath(String packagePath) {
		this.packagePath = packagePath;
		return this;
	}
	
	public DynamicClassBuilder setClassName(String className) {
		this.className = className;
		return this;
	}
	
	public DynamicClassBuilder setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
		return this;
	}
	
	public Class<?> build() throws Exception {
		File packageDir = null;
		File sourceFile = null;
		FileWriter fileWriter = null;
		try {
			packageDir = new File(TEMP_DIR + File.separator + packagePath.replaceAll("\\.", "/"));
			packageDir.mkdirs();
			sourceFile = new File(packageDir.getAbsolutePath() + File.separator + className + ".java");
			fileWriter = new FileWriter(sourceFile);
			fileWriter.write(this.sourceCode);
			fileWriter.flush();
			fileWriter.close();
			
	        // compile source.
	        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	        compiler.run(null, System.out, System.out, sourceFile.getPath());
	        
	        // create class instance.
	        URL[] classUrls = new URL[]{ new File(TEMP_DIR).toURI().toURL() };
	        URLClassLoader classLoader = URLClassLoader.newInstance(classUrls);
	        return Class.forName(packagePath + "." + className, true, classLoader);

		}catch(Exception e){
			LOGGER.error(e.getMessage(), e);
			throw e;
		}finally{
			if(packageDir != null) {
				File[] files = packageDir.listFiles(new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.startsWith(className);
					}
			    });
				if(files != null) {
					for(File file : files) {
						file.delete();
					}
				}
			}
		}
	}
	
}
