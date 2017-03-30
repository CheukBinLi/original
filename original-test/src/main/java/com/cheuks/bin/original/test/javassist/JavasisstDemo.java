package com.cheuks.bin.original.test.javassist;

public class JavasisstDemo {
//	{
//		try {
//			ClassPool pool = ClassPool.getDefault();
//			pool.insertClassPath(new ClassClassPath(this.getClass()));
//			CtClass s3_Class = pool.get(S3Manager.class.getName());
//			CtClass s3Class = pool.makeClass(S3Manager.class.getName() + "_proxy");
//			s3Class.setSuperclass(pool.get(S3Manager.class.getName()));
//			CtConstructor[] constructors = s3_Class.getDeclaredConstructors();
//
//			for (CtConstructor constructor : constructors) {
//				s3Class.addConstructor(new CtConstructor(constructor, s3Class, null));
//			}
//
//			s3Class.addField(CtField.make("public static int MAX_SIZE=5120000;", s3Class));
//			CtConstructor constructor = CtNewConstructor.make(
//					"public S3Manager(String accCountServer, String endPoint, String userId, String appId, String appKey,int maxSize){super(accCountServer, endPoint, userId, appId, appKey);if(1024>this.MAX_SIZE){this.MAX_SIZE=maxSize;}}",
//					s3Class);
//			s3Class.addConstructor(constructor);
//			// 字段
//			CtField[] ctFields = s3_Class.getDeclaredFields();
//			for (CtField tempField : ctFields) {
//				if (!"MAX_SIZE".equals(tempField.getName()))
//					s3Class.addField(new CtField(tempField, s3Class));
//			}
//			// 方法
//			CtMethod[] ctMethods = s3_Class.getDeclaredMethods();
//			for (CtMethod tempMethod : ctMethods) {
//				s3Class.addMethod(new CtMethod(tempMethod, s3Class, null));
//			}
//
//			Class<?> s3ManagerClazz = s3Class.toClass();
//
//			s3ManagerConstructor = s3ManagerClazz.getDeclaredConstructor(String.class, String.class, String.class,
//					String.class, String.class, int.class);
//		} catch (Exception e) {
//			LOG.error(null, e);
//			e.printStackTrace();
//		}
//	}
}
