package net.oopscraft.application.core.asm;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;

public class App {
    private String targetClass;
    private Method targetMethod;

    private AppClassVisitor cv;

    private ArrayList<Callee> callees = new ArrayList<Callee>();

    private static class Callee {
        String className;
        String methodName;
        String methodDesc;
        String source;
        int line;

        public Callee(String cName, String mName, String mDesc, String src, int ln) {
            className = cName; methodName = mName; methodDesc = mDesc; source = src; line = ln;
        }
    }

    private class AppMethodVisitor extends MethodVisitor {

        boolean callsTarget;
        int line;

        public AppMethodVisitor() { 
        	super(Opcodes.ASM5);  
        }

        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        	System.err.println("#owner:" +owner);
            if (owner.equals(targetClass)
                    && name.equals(targetMethod.getName())
                    && desc.equals(targetMethod.getDescriptor())) {
                callsTarget = true;
            }
        }

        public void visitCode() {
            callsTarget = false;
        }

        public void visitLineNumber(int line, Label start) {
            this.line = line;
        }

        public void visitEnd() {
            if (callsTarget)
                callees.add(new Callee(cv.className, cv.methodName, cv.methodDesc, 
                        cv.source, line));
        }
    }

    private class AppClassVisitor extends ClassVisitor {

        private AppMethodVisitor mv = new AppMethodVisitor();

        public String source;
        public String className;
        public String methodName;
        public String methodDesc;

        public AppClassVisitor() { 
        	super(Opcodes.ASM5); 
        }

        public void visit(int version, int access, String name,
                          String signature, String superName, String[] interfaces) {
            className = name;
        }

        public void visitSource(String source, String debug) {
            this.source = source;
        }

        public MethodVisitor visitMethod(int access, String name, 
                                         String desc, String signature,
                                         String[] exceptions) {
            methodName = name;
            methodDesc = desc;
        	System.err.println(name + "," + desc);
            return mv;
        }
    }


    public void findCallingMethodsInJar(String jarPath, String targetClass,
                                        String targetMethodDeclaration) throws Exception {

        this.targetClass = targetClass;
        this.targetMethod = Method.getMethod(targetMethodDeclaration);

        this.cv = new AppClassVisitor();

        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            if (entry.getName().endsWith(".class")) {

                InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024);
                ClassReader reader = new ClassReader(stream);

                reader.accept(cv, 0);

                stream.close();
            }
        }
    }


    public static void main( String[] args ) {
    	args = new String[] {
    		 "C:\\Program Files\\ojdkbuild\\java-1.8.0-openjdk-1.8.0.212-1\\jre\\lib\\rt.jar"
    		,"java/io/PrintStream"
    		,"void println(String)"
    	};
        try {
            App app = new App();

            app.findCallingMethodsInJar(args[0], args[1], args[2]);

            for (Callee c : app.callees) {
                System.out.println(c.source+":"+c.line+" "+c.className+" "+c.methodName+" "+c.methodDesc);
            }

            System.out.println("--\n"+app.callees.size()+" methods invoke "+
                    app.targetClass+" "+
                    app.targetMethod.getName()+" "+app.targetMethod.getDescriptor());
        } catch(Exception x) {
            x.printStackTrace();
        }
    }

}