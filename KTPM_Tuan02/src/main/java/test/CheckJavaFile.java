package test;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class CheckJavaFile {
	private static void checkJavaFile(File javaFile, List<String> report) {
		
    }

    public static void main(String[] args) {
        File javaFile = new File("E:\\hk2_n2\\java\\HSK_QuanLyKhachSan");
        List<String> report = new ArrayList();
        checkJavaFile(javaFile, report);
        report.forEach(System.out::println);
        

    }}






