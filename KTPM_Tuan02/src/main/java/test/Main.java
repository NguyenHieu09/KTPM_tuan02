//package test;
//
//import java.io.File;
//
//import comon.DirExplorer;
//
//public class Main {
//	public static void main(String[] args) { 
//		 File projectDir = new File("E:\\HK1_n4\\www\\WWW_Week02"); 
//		     new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, 
//		file) -> { 
//		   System.out.println(path); 
//		     }).explore(projectDir); 
//		 }
//
//}

package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import comon.DirExplorer;

public class Main {
    public static void main(String[] args) {
//        File projectDir = new File("E:\\HK1_n4\\www\\WWW_Week02");
//        File outputFile = new File(System.getProperty("user.dir"), "output.txt");
//
//        try {
//            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
//
//            // Ghi đoạn văn bản vào đầu dòng trong tập tin output.txt
//            writer.println("Duyệt qua thư mục và con của nó (sử dụng đệ quy) rồi lọc các file có phần mở rộng được chỉ định");
//
//            new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
//                writer.println(path);
//            }).explore(projectDir);
//
//            writer.close();
//            System.out.println("Results have been written to " + outputFile.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    	 File projectDir = new File("E:\\hk2_n2\\java\\HSK_QuanLyKhachSan");
         
         // Tạo một đối tượng ProjectVisitor và gọi phương thức analyzeProject() để kiểm tra dự án
         ProjectVisitor visitor = new ProjectVisitor();
         visitor.analyzeProject(projectDir);

}}



