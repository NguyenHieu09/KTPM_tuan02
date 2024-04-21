package test;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.List;
//
//import com.github.javaparser.StaticJavaParser;
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.PackageDeclaration;
//import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
//import com.github.javaparser.ast.body.FieldDeclaration;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.comments.BlockComment;
//import com.github.javaparser.ast.comments.Comment;
//import com.github.javaparser.ast.comments.JavadocComment;
//import com.github.javaparser.ast.comments.LineComment;
//import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
//import com.google.common.base.Optional;
//import com.github.javaparser.ast.type.ClassOrInterfaceType;
////import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
//
//
//import comon.DirExplorer;
//
//public class ProjectVisitor extends VoidVisitorAdapter<Void> {
//    private String currentPackage = "";
//
//    public void analyzeProject(File projectDir) {
//        DirExplorer dirExplorer = new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
//            try {
//                CompilationUnit cu = StaticJavaParser.parse(file);
//                cu.accept(this, null); // Sử dụng "this" để gọi các phương thức visit của ProjectVisitor
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        });
//
//        try {
//            dirExplorer.explore(projectDir);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void visit(PackageDeclaration n, Void arg) {
//        super.visit(n, arg);
//        String packageName = n.getNameAsString();
//        if (!isValidPackageName(packageName)) {
//            System.out.println("Package không tuân thủ mẫu: " + packageName);
//        }
//        currentPackage = packageName;
//    }
//
//    
//    @Override
//    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
//        super.visit(n, arg);
//        String className = n.getNameAsString();
//        if (!Character.isUpperCase(className.charAt(0)) || !className.matches("[a-zA-Z0-9]+")) {
//            System.out.println("Class không đúng định dạng: " + className + " trong package " + currentPackage);
//        }
//
//        if (!hasClassDescriptionComment(n)) {
//            System.out.println("Class " + className + " không có mô tả trong package " + currentPackage);
//        }
//    }
//    
//    private boolean isValidPackageName(String packageName) {
//        // Kiểm tra xem gói có bắt đầu bằng "com.companyname." hay không
//        return packageName.startsWith("com.companyname.");
//    }
//    
//    
//    @Override
//    public void visit(MethodDeclaration n, Void arg) {
//        super.visit(n, arg);
//        String methodName = n.getNameAsString();
//        if (!Character.isLowerCase(methodName.charAt(0)) || !methodName.matches("[a-zA-Z0-9]+")) {
//            System.out.println("Method không đúng định dạng: " + methodName + " trong package " + currentPackage);
//        }
//
//        if (!hasMethodDescriptionComment(n)) {
//            System.out.println("Method " + methodName + " không có mô tả trong package " + currentPackage);
//        }
//    }
//    
//
//
//    
//    private boolean hasClassDescriptionComment(ClassOrInterfaceDeclaration n) {
//        List<Comment> comments = n.getAllContainedComments();
//        for (Comment comment : comments) {
//            if (comment instanceof JavadocComment || comment instanceof BlockComment || comment instanceof LineComment) {
//                String content = comment.getContent();
//                if (content.contains("created-date") && content.contains("author")) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private boolean hasMethodDescriptionComment(MethodDeclaration n) {
//        String methodName = n.getNameAsString();
//        if (!methodName.equals("hashCode") && !methodName.equals("equals") && !methodName.equals("toString")) {
//            List<Comment> comments = n.getAllContainedComments();
//            for (Comment comment : comments) {
//                if (comment instanceof JavadocComment || comment instanceof BlockComment || comment instanceof LineComment) {
//                    String content = comment.getContent();
//                    if (!content.isBlank()) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//    
//}
//
//

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import comon.DirExplorer;

public class ProjectVisitor extends VoidVisitorAdapter<Void> {
    private String currentPackage = "";
    private List<String> errors = new ArrayList<>();

    public void analyzeProject(File projectDir) {
        DirExplorer dirExplorer = new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            try {
                CompilationUnit cu = StaticJavaParser.parse(file);
                cu.accept(this, null); // Sử dụng "this" để gọi các phương thức visit của ProjectVisitor
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        try {
            dirExplorer.explore(projectDir);
            printErrorsToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(PackageDeclaration n, Void arg) {
        super.visit(n, arg);
        String packageName = n.getNameAsString();
        if (!isValidPackageName(packageName)) {
        	 errors.add("========================================================");
            errors.add("Package không tuân thủ mẫu com.companyname. : " + packageName);
        }
        currentPackage = packageName;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);
        String className = n.getNameAsString();
        if (!hasClassDescriptionComment(n)) {
            errors.add("Class " + className + " không có mô tả trong package " + currentPackage);
        }

        List<MethodDeclaration> methods = n.getMethods();
        for (MethodDeclaration method : methods) {
            if (!hasMethodDescriptionComment(method)) {
                errors.add("Method " + method.getNameAsString() + " không có mô tả trong package " + currentPackage);
            }
        }
    }

    private boolean isValidPackageName(String packageName) {
        // Kiểm tra xem gói có bắt đầu bằng "com.companyname." hay không
        return packageName.startsWith("com.companyname.");
    }

    private boolean hasClassDescriptionComment(ClassOrInterfaceDeclaration n) {
        List<Comment> comments = n.getAllContainedComments();
        for (Comment comment : comments) {
            if (comment instanceof JavadocComment || comment instanceof BlockComment) {
                String content = comment.getContent();
                if (content.contains("created-date") && content.contains("author")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasMethodDescriptionComment(MethodDeclaration n) {
        if (n.getNameAsString().equals("hashCode") || n.getNameAsString().equals("equals") || n.getNameAsString().equals("toString")) {
            return true; // Bỏ qua các phương thức hashCode, equals, và toString
        }

        List<Comment> comments = n.getAllContainedComments();
        for (Comment comment : comments) {
            if (comment instanceof JavadocComment || comment instanceof BlockComment) {
                String content = comment.getContent();
                if (!content.isBlank()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void printErrorsToFile() throws Exception {
        File outputFile = new File("report.txt");
        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));

        for (String error : errors) {
            writer.println(error);
        }

        writer.close();
    }
}
