package net.oopscraft.application.utility.asm;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMTest {
	
	public static void main(String[] args) throws Exception {
		try { 
			ClassPrinterVisitor cp = new ClassPrinterVisitor(); 
			ClassReader cr = new ClassReader("net.oopscraft.application.core.asm.Callee"); 
			cr.accept(cp, 0); 
			MethodVisitor mv = cp.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);

			
		} catch (Throwable t) { 
			t.printStackTrace(); 
			throw new RuntimeException(t); 
		}
		

	}


}
