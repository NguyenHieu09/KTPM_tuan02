package test;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;

import java.util.List;

public class CodeAnalyzer {

    public static boolean isValidPackageName(String packageName) {
        return packageName.startsWith("com.companyname.");
    }

    public static boolean isValidClassName(String className) {
        return Character.isUpperCase(className.charAt(0)) && className.matches("[a-zA-Z0-9]+");
    }

    public static boolean isValidMethodName(String methodName) {
        return Character.isLowerCase(methodName.charAt(0)) && methodName.matches("[a-zA-Z0-9]+");
    }

    public static boolean hasClassDescriptionComment(ClassOrInterfaceDeclaration n) {
        List<Comment> comments = n.getAllContainedComments();
        for (Comment comment : comments) {
            if (comment instanceof JavadocComment || comment instanceof LineComment) {
                String content = comment.getContent();
                if (content.contains("created-date") && content.contains("author")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasMethodDescriptionComment(MethodDeclaration n) {
        String methodName = n.getNameAsString();
        if (!methodName.equals("hashCode") && !methodName.equals("equals") && !methodName.equals("toString")) {
            List<Comment> comments = n.getAllContainedComments();
            for (Comment comment : comments) {
                if (comment instanceof JavadocComment || comment instanceof LineComment) {
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
