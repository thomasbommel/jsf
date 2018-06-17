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

import javassist.bytecode.BadBytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;

public class LowLvlTransformer implements ClassFileTransformer {

	protected final List<String> includedPackages;
	
	public LowLvlTransformer(String[] includedPackages) {
		this.includedPackages = Arrays.asList(includedPackages);
	}
	
	
	@Override
	public byte[] transform(ClassLoader loader, String name, Class<?> ___, ProtectionDomain __, byte[] classfile) throws IllegalClassFormatException {
		for (int i = 0; i < includedPackages.size(); i++) {
			if (name.startsWith(includedPackages.get(i)))
				break;
			if (i == includedPackages.size()) 
				return classfile;	//not included -> do not transform
		}
		System.err.println(name);
		return classfile;
//		try {
//			ClassFile file = new ClassFile(new DataInputStream(new ByteArrayInputStream(classfile)));
//			
//			List<MethodInfo> methods = file.getMethods();
//			for(MethodInfo method : methods) {
//				if(method.getName().equals(methodname)) {
//					transform(method);
//				}
//			}
//			
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			file.write(new DataOutputStream(out));
//			byte[] data = out.toByteArray();			
//			try (FileOutputStream o = new FileOutputStream(classname.replace('/', '.') + ".class")) {
//				o.write(data);
//			}
//			return data;
//		} catch (IOException | RuntimeException | BadBytecode e) {
//			e.printStackTrace();
//			return null;
//		}
	}

}
