package com.ormv.inspection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.ormv.util.Session;

public class ClassInspector {
	
	private static Logger log = Logger.getLogger(Session.class);

	public static void inspectClass(Class<?> clazz) {
		listPublicConstructors(clazz);
		listNonPublicConstructors(clazz);
		listPublicFields(clazz);
		listNonPublicFields(clazz);
		listPublicMethods(clazz);
		listDeclaredMethods(clazz);
	}

	public static void listPublicConstructors(Class<?> clazz) {
		System.out.println("Printing the public constructors of the " + clazz.getName());
		Constructor<?>[] constructors = clazz.getConstructors();
		for (Constructor<?> constructor : constructors) {
			System.out.println("\tConstructor name: " + constructor.getName());
			System.out.println("\tConstructor param types: " + Arrays.toString(constructor.getParameterTypes()) + "\n");
			System.out.println();
		}
	}

	public static void listPublicFields(Class<?> clazz) {
		System.out.println("Printing public fields of the " + clazz.getName());
		Field[] fields = clazz.getFields();
		if (fields.length == 0) {
			System.out.println("There are no public fields in " + clazz.getName());
		}
		for (Field field : fields) {
			System.out.println("\tField name: " + field.getName());
			System.out.println("\tField type: " + field.getType());
			System.out.println("\tIs field primitive? :: " + field.getType().isPrimitive());
			System.out.println("\tModifiers bit value: " + Integer.toBinaryString(field.getModifiers()) + "\n");
		}
	}

	public static void listNonPublicFields(Class<?> clazz) {
		System.out.println("Printing non-public fields of the " + clazz.getName());
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length == 0) {
			System.out.println("\nThere are no non-public fields in " + clazz.getName());
		}
		for (Field field : fields) {
			if ((field.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC)
				continue;
			System.out.println("\tField name: " + field.getName());
			System.out.println("\tField type: " + field.getType());
			System.out.println("\tIs field primitive?: " + field.getType().isPrimitive());
			System.out.println("\tModifiers bit value: " + Integer.toBinaryString(field.getModifiers()));
			System.out.println();
		}
	}

	public static Method[] listPublicMethods(Class<?> clazz) {
		System.out.println("Printing public fields of the " + clazz.getName());
		Method[] methods = clazz.getMethods();
		if (methods.length == 0) {
			System.out.println("There are no public fields in " + clazz.getName());
		}
		for (Method method : methods) {
			if (method.getDeclaringClass() == Object.class)
				continue;
			System.out.println("\nMethod name: " + method.getName());
			System.out.println("\tMethod param count: " + method.getParameterCount());
			System.out.println("\tMethod declared class: " + method.getDeclaringClass());
			System.out.println("\tMethod declared annotations: " + Arrays.toString(method.getDeclaredAnnotations()));

			Parameter[] params = method.getParameters();
			for (Parameter param : params) {
				System.out.println("\t\tParameter name: " + param.getName());
				System.out.println("\t\tParameter type: " + param.getType());
				System.out.println("\t\tParameter annotations: " + Arrays.toString(param.getDeclaredAnnotations()));
			}
			System.out.println();
		
		}
		
		return methods;
	}

	public static void listNonPublicConstructors(Class<?> clazz) {
		System.out.println("Printing non-visible constructors of the " + clazz.getName());
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			// Parse modifier out
			if ((constructor.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) {
				continue;
			}
			System.out.println("\tConstructor name: " + constructor.getName());
			System.out.println("\tConstructor param types: " + Arrays.toString(constructor.getParameterTypes()) + "\n");
			System.out.println();
		}
	}

	public static Method[] listDeclaredMethods(Class<?> clazz) {

		System.out.println("Listing all of the declared methods of the class: " + clazz.getName());
		Method[] methods = clazz.getDeclaredMethods();

		if (methods.length == 0) {
			System.out.println("\tThere are no non-public methods in the class: " + clazz.getName());
		}

		for (Method method : methods) {

			if ((method.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) {
				continue;
			}

			System.out.println("\tName: " + method.getName());
			Class<?>[] parameterTypes = method.getParameterTypes();
			System.out.println("\tModifiers bit value: " + Integer.toBinaryString(method.getModifiers()));
			System.out.println("\tDeclaring class: " + method.getDeclaringClass().getName());
			System.out.println("\tDeclared annotations: " + Arrays.toString(method.getDeclaredAnnotations()));

			System.out.println("\tParameter count: " + parameterTypes.length);
			Parameter[] params = method.getParameters();
			for (Parameter param : params) {
				System.out.println("\t\tParameter name: " + param.getName());
				System.out.println("\t\tParameter type: " + param.getType());
				System.out.println("\t\tParameter annotations: " + Arrays.toString(param.getAnnotations()));
			}
			System.out.println();
		}
		return methods;
	}
	
	public Object getFieldValue(Field field, Class<?> clazz)
	{
	    // MZ: Find the correct method
		Method[] methods = ClassInspector.listDeclaredMethods(clazz);
		
	    for (Method method : methods)
	    {
	        if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
	        {
	            if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
	            {
	                // MZ: Method found, run it
	                try
	                {
	                	method.setAccessible(true);
	                    return method.invoke(clazz);
	                }
	                catch (IllegalAccessException e)
	                {
	                    log.warn("Could not determine method: " + method.getName());
	                }
	                catch (InvocationTargetException e)
	                {
	                    log.warn("Could not determine method: " + method.getName());
	                }

	            }
	        }
	    }

	    return null;
	}
}
