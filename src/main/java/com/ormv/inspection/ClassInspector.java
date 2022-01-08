package com.ormv.inspection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public class ClassInspector {

	public static void listPublicConstructors(Class<?> clazz) {
		System.out.println("Printing the public constructors of class " + clazz.getName());

		Constructor<?>[] constructors = clazz.getConstructors();

		for (Constructor<?> constructor : constructors) {

			System.out.println("\tConstructor name: " + constructor.getName());
			System.out.println("\tConstructor param types: " + Arrays.toString(constructor.getParameterTypes()) + "\n");
			System.out.println();
		}
	}

	public static void listNonPublicFields(Class<?> clazz) {
		System.out.println("Printing the non-public fields of class " + clazz.getName());

		Field[] fields = clazz.getDeclaredFields();

		if (fields.length == 0) {
			System.out.println("There are no non-public fields in " + clazz.getName());
		} else {

			for (Field field : fields) {

				if (field.getModifiers() == (Modifier.PUBLIC))
					continue;

				System.out.println("\tField name: " + field.getName());
				System.out.println("\tField type: " + field.getType());
				System.out.println("\tIs the field primitive? " + field.getType().isPrimitive());
				System.out.println("\tModifiers bit value: " + Integer.toBinaryString(field.getModifiers()));
				System.out.println();
			}
		}

	}
	
	public static Method[] listPublicMethods(Class<?> clazz) {
		System.out.println("Printing public methods of " + clazz.getName());
		Method[] methods = clazz.getMethods();
		
		for(Method method : methods) {
			if(method.getDeclaringClass() == Object.class)
				continue;
			if (method.getModifiers() != (Modifier.PUBLIC))
				continue;

			
			System.out.println("\tMethod name: " + method.getName());
			System.out.println("\tMethod param count: " + method.getParameterCount());
			System.out.println("\tMethod declared class: " + method.getDeclaringClass());
			System.out.println("\tMethod declared annotations " + Arrays.toString(method.getDeclaredAnnotations()));
			
			Parameter[] params = method.getParameters(); 
			for(Parameter param : params) {
				System.out.println("\n\tParameter name: " + param.getName());
				System.out.println("\n\tParameter type: " + param.getType());
			}
		}
		System.out.println();
		
		return methods;
	}
}
