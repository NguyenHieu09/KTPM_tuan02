package test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import comon.DirExplorer;


public class ProjectVisitor extends VoidVisitorAdapter<Void> {
    private String currentPackage = "";
    private List<String> errors = new ArrayList<>();
    private Set<String> constantNames = new HashSet<>();
    private boolean isInInterface = false;

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

    private boolean isValidPackageName(String packageName) {
        return packageName.startsWith("com.companyname.");
    }
    
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        super.visit(n, arg);
        String className = n.getNameAsString();
        if (!isValidClassName(className)) {
            errors.add("Class " + className + " không bắt đầu bằng chữ hoa trong package " + currentPackage);
        }
        if (!hasClassDescriptionComment(n)) {
            errors.add("Class " + className + " không có mô tả trong package " + currentPackage);
        }

        List<MethodDeclaration> methods = n.getMethods();
        for (MethodDeclaration method : methods) {
            if (!hasMethodDescriptionComment(method)) {
                errors.add("Method " + method.getNameAsString() + " không có mô tả trong package " + currentPackage);
            }
        }

        if (!constantNames.isEmpty() && !n.isInterface()) {
            errors.add("Các hằng số " + String.join(", ", constantNames) + " không nằm trong một interface trong package " + currentPackage);
        }
        constantNames.clear();
    }

    @Override
    public void visit(FieldDeclaration n, Void arg) {
        super.visit(n, arg);
        if (n.isFinal()) {
            for (VariableDeclarator variable : n.getVariables()) {
                String fieldName = variable.getNameAsString();
                if (!isValidFieldName(fieldName)) {
                    errors.add("Field " + fieldName + " không bắt đầu bằng chữ thường trong package " + currentPackage);
                }
            }
        }

        if (n.isFinal()) {
            for (VariableDeclarator variable : n.getVariables()) {
                String constantName = variable.getNameAsString();
                // Kiểm tra tên hằng số có viết hoa không
                if (!Character.isUpperCase(constantName.charAt(0))) {
                    errors.add("Hằng số " + constantName + " không viết hoa trong package " + currentPackage);
                }
                // Thêm tên hằng số vào danh sách constantNames
                constantNames.add(constantName);
            }
        }
    }



    private boolean isValidClassName(String className) {
        return className.matches("[A-Z][a-zA-Z]*");
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
            return true;
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

    

    private boolean isValidFieldName(String fieldName) {
        return Character.isLowerCase(fieldName.charAt(0)) && fieldName.matches("[a-zA-Z]+");
    }

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        super.visit(n, arg);
        String methodName = n.getNameAsString();
        if (!isValidMethodName(methodName)) {
            errors.add("Method " + methodName + " không bắt đầu bằng một động từ và phải là chữ thường trong package " + currentPackage);
        }
    }

    private boolean isValidMethodName(String methodName) {
        return Character.isLowerCase(methodName.charAt(0));
    }

    @Override
    public void visit(CompilationUnit n, Void arg) {
        super.visit(n, arg);
        isInInterface = false;
    }

    @Override
    public void visit(BlockComment n, Void arg) {
        super.visit(n, arg);
        if (n.getContent().contains("interface")) {
            isInInterface = true;
        }
    }

    @Override
    public void visit(JavadocComment n, Void arg) {
        super.visit(n, arg);
        if (n.getContent().contains("interface")) {
            isInInterface = true;
        }
    }
    
    

    private void printErrorsToFile() {
        try {
            File outputFile = new File("report.txt");
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));

            for (String error : errors) {
                writer.println(error);
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
