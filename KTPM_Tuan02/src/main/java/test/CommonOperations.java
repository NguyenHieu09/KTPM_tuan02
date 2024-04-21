//package test;
//
//import java.io.File;
//import com.github.javaparser.StaticJavaParser; 
//import com.github.javaparser.ast.PackageDeclaration; 
//import com.github.javaparser.ast.body.FieldDeclaration; 
//import com.github.javaparser.ast.body.MethodDeclaration; 
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter; 
//import com.google.common.base.Strings;
//
//import comon.DirExplorer; 
//
//public class CommonOperations {
//	public static void listMethodCalls(File projectDir) { 
//		  new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, 
//		path, file) -> { 
//		   System.out.println(path); 
//		   System.out.println(Strings.repeat("=", path.length())); 
//		   try { 
//		    new VoidVisitorAdapter<Object>() { 
//		    	 @Override
//		    	    public void visit(PackageDeclaration n, Object arg) {
//		    	        super.visit(n, arg);
//		    	        String packageName = n.getNameAsString();
//		    	        if (!CodeAnalyzer.isValidPackageName(packageName)) {
//		    	            System.out.println("Package không tuân thủ mẫu: " + packageName);
//		    	        }
//		    	        currentPackage = packageName;
//		    	    }
//
//		    	    @Override
//		    	    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
//		    	        super.visit(n, arg);
//		    	        String className = n.getNameAsString();
//		    	        if (!CodeAnalyzer.isValidClassName(className)) {
//		    	            System.out.println("Class không đúng định dạng: " + className + " trong package " + currentPackage);
//		    	        }
//
//		    	        if (!CodeAnalyzer.hasClassDescriptionComment(n)) {
//		    	            System.out.println("Class " + className + " không có mô tả trong package " + currentPackage);
//		    	        }
//		    	    }
//
//		    	    @Override
//		    	    public void visit(MethodDeclaration n, Object arg) {
//		    	        super.visit(n, arg);
//		    	        String methodName = n.getNameAsString();
//		    	        if (!CodeAnalyzer.isValidMethodName(methodName)) {
//		    	            System.out.println("Method không đúng định dạng: " + methodName + " trong package " + currentPackage);
//		    	        }
//
//		    	        if (!CodeAnalyzer.hasMethodDescriptionComment(n)) {
//		    	            System.out.println("Method " + methodName + " không có mô tả trong package " + currentPackage);
//		    	        }
//		    	    }
//		 
//		    }.visit(StaticJavaParser.parse(file), null); 
//		   } catch (Exception e) { 
//		    new RuntimeException(e); 
//		   } 
//		  }).explore(projectDir); 
//		 } 
//		 
//		 public static void main(String[] args) { 
//		  File projectDir = new File("E:\\HK1_n4\\www\\WWW_Week02"); 
//		  listMethodCalls(projectDir); 
//		 }
//}
