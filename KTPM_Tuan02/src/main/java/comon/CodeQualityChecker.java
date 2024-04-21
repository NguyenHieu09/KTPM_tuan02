
package comon;

import java.util.List;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

public class CodeQualityChecker {
	 public static void main(String[] args) {
	        File projectDir = new File("E:\\hk2_n2\\java\\HSK_QuanLyKhachSan");
	        analyzeProject(projectDir);
	    }

	    public static void analyzeProject(File projectDir) {
	        DirExplorer dirExplorer = new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
	            try {
	                CompilationUnit cu = StaticJavaParser.parse(file);
	                cu.accept(new ProjectVisitor(), null);
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            }
	        });

	        try {
	            dirExplorer.explore(projectDir);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private static class ProjectVisitor extends VoidVisitorAdapter<Void> {
	        @Override
	        public void visit(PackageDeclaration n, Void arg) {
	            super.visit(n, arg);
	            String packageName = n.getNameAsString();
	            // Kiểm tra yêu cầu 1: Package phải theo mẫu com.companyname.*
	            if (!packageName.matches("com\\.companyname\\..*")) {
	                System.out.println("Package không đúng mẫu: " + packageName);
	            }
	        }

	        @Override
	        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
	            super.visit(n, arg);
	            String className = n.getNameAsString();
	            // Kiểm tra yêu cầu 2: Class phải có tên là một danh từ hoặc cụm danh ngữ và bắt đầu bằng chữ hoa
	            if (!Character.isUpperCase(className.charAt(0)) || !className.matches("[a-zA-Z0-9]+")) {
	                System.out.println("Class không đúng định dạng: " + className);
	            }

	            // Kiểm tra yêu cầu 3: Mỗi lớp phải có một comment mô tả cho lớp
	            if (!hasClassDescriptionComment(n)) {
	                System.out.println("Class " + className + " không có mô tả");
	            }
	        }

	        @Override
	        public void visit(FieldDeclaration n, Void arg) {
	            super.visit(n, arg);
	            // Kiểm tra yêu cầu 4: Fields phải là danh từ hoặc cụm danh ngữ và bắt đầu bằng một chữ thường
	            List<String> fieldNames = n.getVariables().stream().map(variable -> variable.getNameAsString()).toList();
	            for (String fieldName : fieldNames) {
	                if (!Character.isLowerCase(fieldName.charAt(0)) || !fieldName.matches("[a-zA-Z0-9]+")) {
	                    System.out.println("Field không đúng định dạng: " + fieldName);
	                }
	            }
	        }

	        @Override
	        public void visit(MethodDeclaration n, Void arg) {
	            super.visit(n, arg);
	            String methodName = n.getNameAsString();
	            // Kiểm tra yêu cầu 6: Tên method phải bắt đầu bằng một động từ và phải là chữ thường
	            if (!Character.isLowerCase(methodName.charAt(0)) || !methodName.matches("[a-zA-Z0-9]+")) {
	                System.out.println("Method không đúng định dạng: " + methodName);
	            }

	            // Kiểm tra yêu cầu 7: Mỗi method phải có một ghi chú mô tả công việc của method
	            if (!hasMethodDescriptionComment(n)) {
	                System.out.println("Method " + methodName + " không có mô tả");
	            }
	        }

	        private boolean hasClassDescriptionComment(ClassOrInterfaceDeclaration n) {
	            List<Comment> comments = n.getAllContainedComments();
	            for (Comment comment : comments) {
	                if (comment instanceof JavadocComment || comment instanceof BlockComment || comment instanceof LineComment) {
	                    String content = comment.getContent();
	                    if (content.contains("created-date") && content.contains("author")) {
	                        return true;
	                    }
	                }
	            }
	            return false;
	        }

//	        private boolean hasMethodDescriptionComment(MethodDeclaration n) {
//	            List<Comment> comments = n.getAllContainedComments();
//	            for (Comment comment : comments) {
//	                if (comment instanceof JavadocComment || comment instanceof BlockComment || comment instanceof LineComment) {
//	                    String content = comment.getContent();
//	                    // Excluding default constructor, accessors/mutators, hashCode, equals, toString methods
//	                    if (!n.isConstructorDeclaration() && !n.isGetter() && !n.isSetter() &&
//	                            !n.isHashCode() && !n.isEquals() && !n.isToString()) {
//	                        if (!content.isBlank()) {
//	                            return true;
//	                        }
//	                    }
//	                }
//	            }
//	            return false;
//	        }
//	    }
	        private boolean hasMethodDescriptionComment(MethodDeclaration n) {
	            String methodName = n.getNameAsString();
	            // Kiểm tra xem phương thức không phải là một trong các phương thức đặc biệt
	            if (!methodName.equals("hashCode") && !methodName.equals("equals") && !methodName.equals("toString")) {
	                List<Comment> comments = n.getAllContainedComments();
	                for (Comment comment : comments) {
	                    if (comment instanceof JavadocComment || comment instanceof BlockComment || comment instanceof LineComment) {
	                        String content = comment.getContent();
	                        if (!content.isBlank()) {
	                            return true;
	                        }
	                    }
	                }
	            }
	            return false;
	        }
	    }

}
