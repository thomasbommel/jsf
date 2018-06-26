package agent;

import java.util.List;

import javassist.bytecode.Bytecode;
import javassist.bytecode.Opcode;

/**
 * Provides utility functions for constructing Bytecode.
 */
public class BytecodeHelper {

	private final Bytecode code;
	
	/**
	 * @param code - the Bytecode to modify by the helper methods.
	 */
	public BytecodeHelper(Bytecode code) {
		this.code = code;
	}
	
	/**
	 * Pushes an Object-array on the stack with the argument values from the method that is about to be called<br>
	 * Stack before:<br>
	 * [ &lt;args&gt;<br>
	 * Stack after:<br>
	 * [ &lt;args&gt;, argsObjectArray
	 */
	public void pushArgumentsArray(List<String> argTypes) {
		int argCount = argTypes.size();
		// [ <args>
		code.addIconst(argCount);
		// [ <args>, argument count
		code.addAnewarray("java.lang.Object");
		// [ <args>, array
		
		//store arguments from stack into Object array
		for (int i = argCount - 1; i >= 0; i--) {
			String type = argTypes.get(i);
			// arg, array
			if (isLongOrDouble(type)) {
				// [ long.1, long.2, array
				code.add(Opcode.DUP_X2);
				// [ array, long.1, long.2, array
				code.add(Opcode.DUP_X2);
				code.add(Opcode.POP);
				// [ array, array, long.1, long.2, 
			}
			else {
				code.add(Opcode.DUP_X1);
				code.add(Opcode.SWAP);
			}
			// [ array, array, arg (Primitive)
			wrapPrimitiveWithReference(type);
			// [ array, array, arg (Object)
			code.addIconst(i);
			// [ array, array, arg, index
			code.add(Opcode.SWAP);
			// [ array, array, index, arg
			code.add(Opcode.AASTORE);
			// [ array
		}
		// [ array
		
		//restore arguments back to stack from Object array
		for (int i = 0; i < argCount; i++) {
			String type = argTypes.get(i);
			// [ array
			code.add(Opcode.DUP);
			code.addIconst(i);
			// [ array, array, index
			code.add(Opcode.AALOAD);
			// [ array, arg (Object)
			unwrapPrimitiveFromReference(type);
			// [ array, arg (Primitive)
			if (isLongOrDouble(type)) {
				// [ array, long.1, long.2
				code.add(Opcode.DUP2_X1);
				// [ long.1, long.2, array, long.1, long.2
				code.add(Opcode.POP2);
			}
			else {
				code.add(Opcode.SWAP);
			}
			// [ arg, array
		}
		// [ <args>, array
	}
	
	/**
	 * Returns true if typeDescriptor is "J" or "D"
	 */
	public boolean isLongOrDouble(String typeDescriptor) {
		switch (typeDescriptor) {
		case "J":
		case "D":
			return true;
		default:	//any other type
			return false;
		}
	}
	
	/**
	 * Loads the local parameter value of parameter i into the stack.<br>
	 * Returns a displacement of 1 for types Long("J") and Double("D").
	 */
	public int loadLocal(List<String> arguments, int i, int displacement) {
		switch(arguments.get(i)) {
			case "B":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Byte", "valueOf", "(B)Ljava/lang/Byte;");
				return 0;
			case "S":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Short", "valueOf", "(S)Ljava/lang/Short;");
				return 0;
			case "I":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Integer", "valueOf", "(I)Ljava/lang/Integer;");
				return 0;
			case "J":
				code.addLload(i + displacement);
				code.addInvokestatic("java.lang.Long", "valueOf", "(J)Ljava/lang/Long;");
				return 1;
			case "F":
				code.addFload(i + displacement);
				code.addInvokestatic("java.lang.Float", "valueOf", "(F)Ljava/lang/Float;");
				return 0;
			case "D":
				code.addDload(i + displacement);
				code.addInvokestatic("java.lang.Double", "valueOf", "(D)Ljava/lang/Double;");
				return 1;
			case "Z":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
				return 0;
			case "C":
				code.addIload(i + displacement);
				code.addInvokestatic("java.lang.Character", "valueOf", "(C)Ljava/lang/Character;");
				return 0;
			default: // L, [
				code.addAload(i + displacement);
				return 0;
		}
	}
	
	/**
	 * Duplicates the return value currently on top of the Stack.
	 */
	public void duplicateReturnValue(String returnType) {
		// [
		if (returnType.equals("V")) {
			code.add(Opcode.ACONST_NULL);
		}
		else if (returnType.equals("J") || returnType.equals("D")) {
			code.add(Opcode.DUP2);
		}
		else {
			code.add(Opcode.DUP);
		}
		// [ return-value (as primitive)
		wrapPrimitiveWithReference(returnType);
		// [ return-value (as Object)
	}
	
	/**
	 * Converts a primitive value on the stack to a Reference value.
	 */
	public void wrapPrimitiveWithReference(String type) {
		switch(type) {
			case "B":
				code.addInvokestatic("java.lang.Byte", "valueOf", "(B)Ljava/lang/Byte;");
				break;
			case "S":
				code.addInvokestatic("java.lang.Short", "valueOf", "(S)Ljava/lang/Short;");
				break;
			case "I":
				code.addInvokestatic("java.lang.Integer", "valueOf", "(I)Ljava/lang/Integer;");
				break;
			case "J":
				code.addInvokestatic("java.lang.Long", "valueOf", "(J)Ljava/lang/Long;");
				break;
			case "F":
				code.addInvokestatic("java.lang.Float", "valueOf", "(F)Ljava/lang/Float;");
				break;
			case "D":
				code.addInvokestatic("java.lang.Double", "valueOf", "(D)Ljava/lang/Double;");
				break;
			case "Z":
				code.addInvokestatic("java.lang.Boolean", "valueOf", "(Z)Ljava/lang/Boolean;");
				break;
			case "C":
				code.addInvokestatic("java.lang.Character", "valueOf", "(C)Ljava/lang/Character;");
				break;
			default: // L, [ --> don't wrap since already reference
				break;	
		}
	}
	
	
	/**
	 * Converts an Object reference on the stack to a primitive value.
	 */
	public void unwrapPrimitiveFromReference(String primitiveType) {
		//stack before invokevirtual: [ objRef, <args>
		switch(primitiveType) {
			case "B":
				//we stored value in java/lang/Object array, so we need to cast objRef back to actual type
				code.addCheckcast("Ljava/lang/Byte;");
				code.addInvokevirtual("java.lang.Byte", "byteValue", "()B");
				break;
			case "S":
				code.addCheckcast("Ljava/lang/Short;");
				code.addInvokevirtual("java.lang.Short", "shortValue", "()S");
				break;
			case "I":
				code.addCheckcast("Ljava/lang/Integer;");
				code.addInvokevirtual("java.lang.Integer", "intValue", "()I");
				break;
			case "J":
				code.addCheckcast("Ljava/lang/Long;");
				code.addInvokevirtual("java.lang.Long", "longValue", "()J");
				break;
			case "F":
				code.addCheckcast("Ljava/lang/Float;");
				code.addInvokevirtual("java.lang.Float", "floatValue", "()F");
				break;
			case "D":
				code.addCheckcast("Ljava/lang/Double;");
				code.addInvokevirtual("java.lang.Double", "doubleValue", "()D");
				break;
			case "Z":
				code.addCheckcast("Ljava/lang/Boolean;");
				code.addInvokevirtual("java.lang.Boolean", "booleanValue", "()Z");
				break;
			case "C":
				code.addCheckcast("Ljava/lang/Character;");
				code.addInvokevirtual("java.lang.Character", "charValue", "()C");
				break;
			default:	//non-primitive type
				code.addCheckcast(primitiveType);
				break;	
		}
	}
	
}
