package test;

import java.io.File;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

import comon.DirExplorer;
//Duyệt qua các file java và in ra các lớp bên trong file
public class ListClassesExample {
	 public static void listClasses(File projectDir) { 
	        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, 
	file) -> { 
	            System.out.println(path); 
	            System.out.println(Strings.repeat("=", path.length())); 
	            try { 
	                new VoidVisitorAdapter<Object>() { 
	                    @Override 
	                    public void visit(ClassOrInterfaceDeclaration n, Object arg) { 
	                        super.visit(n, arg); 
	                        System.out.println(" * " + n.getName()); 
	                    } 
	                }.visit(StaticJavaParser.parse(file), null); 
	                System.out.println(); // empty line 
	            } catch (Exception e) { 
	                new RuntimeException(e); 
	            } 
	        }).explore(projectDir); 
	    } 
	    public static void main(String[] args) { 
	        File projectDir = new File("E:\\HK1_n4\\www\\WWW_Week02"); 
	        listClasses(projectDir); 
	    }
}
