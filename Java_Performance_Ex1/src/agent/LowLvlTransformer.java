package agent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javassist.ClassPool;
import javassist.CtMethod;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class LowLvlTransformer implements ClassFileTransformer {

	protected final List<String> includedPackages;
	
	public LowLvlTransformer(String[] includedPackages) {
		this.includedPackages = Arrays.asList(includedPackages);
	}
	
	
	@Override
	public byte[] transform(ClassLoader loader, String name, Class<?> ___, ProtectionDomain __, byte[] classfile) throws IllegalClassFormatException {
		for (int i = 0; i < includedPackages.size(); i++) {
			if (name.startsWith(includedPackages.get(i)))
				break;	//class included -> transform
			if (i == includedPackages.size() - 1) 
				return classfile;	//not included -> do not transform
		}

		try {
			ClassFile file = new ClassFile(new DataInputStream(new ByteArrayInputStream(classfile)));

			@SuppressWarnings("unchecked")
			List<MethodInfo> methods = file.getMethods();
			for(MethodInfo method : methods) {
				transform(method);
			}
			
			ByteArrayOutputStream transformedClassFile = new ByteArrayOutputStream();
			file.write(new DataOutputStream(transformedClassFile));
			return transformedClassFile.toByteArray();
		} catch (IOException | RuntimeException | BadBytecode e) {
			e.printStackTrace();
			return null;
		}
	}

	//Method invocation opcodes: 182-186
	private void transform(MethodInfo method) throws BadBytecode {
		CodeIterator iterator = method.getCodeAttribute().iterator();
		
		while (iterator.hasNext()) {
			int nextInstrIndex = iterator.next();
			int opcode = iterator.byteAt(nextInstrIndex);
			
			if (opcode >= 182 && opcode <= 186)
				System.err.printf("name: %s ; instr: %d ; opcode: %d%n", method.getName(), nextInstrIndex, opcode);
		}
	}

}
