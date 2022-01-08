package com.ormv.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.ormv.inspection.ClassInspector;

public class Session<T extends InterfaceSession> {
	// meta model
	//MetaModel<T> metaModel;
	//Object obj;
	Class<?> annotatedClass;
	MetaModel<Class<?>> model;
	private static Logger log = Logger.getLogger(Session.class);
	
	
	

	public void persist(Class<?> annotatedClass){
		this.annotatedClass = annotatedClass;
		MetaModel<Class<?>> model = MetaModel.of(annotatedClass);
		
		
	}
	
	public void delete() {		
		
		
		
	}
	
	public int getIdValue() {
		
		return -1;
	}
		
		
//		for (Annotation annotation : obj.class.getAnnotations()) {
//            Class<? extends Annotation> type = annotation.annotationType();
//            System.out.println("Values of " + type.getName());
//
//            for (Method method : type.getDeclaredMethods()) {
//                Object value = method.invoke(annotation, (Object[])null);
//                System.out.println(" " + method.getName() + ": " + value);
//            }
//	}

	
	public Object runGetter(Field field)
	{
	    // MZ: Find the correct method
		Method[] methods = ClassInspector.listDeclaredMethods(this.annotatedClass);
		
	    for (Method method : methods)
	    {
	        if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
	        {
	            if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
	            {
	                // MZ: Method found, run it
	                try
	                {
	                    return method.invoke(this.annotatedClass);
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
