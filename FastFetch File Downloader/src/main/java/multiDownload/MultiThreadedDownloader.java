package multiDownload;


/*import java.awt.Desktop;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadedDownloader {
    private static final int BUFFER_SIZE = 1024;

    public static void downloadFile(String fileUrl, int numSegments, String outputFileName) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        int contentLength = conn.getContentLength();
        conn.disconnect();

        if (contentLength == -1) {
            throw new IOException("Failed to retrieve file size. Server did not return content length.");
        }

        System.out.println("Total File Size: " + contentLength + " bytes");

        ExecutorService executor = Executors.newFixedThreadPool(numSegments);
        int segmentSize = contentLength / numSegments;

        for (int i = 0; i < numSegments; i++) {
            int start = i * segmentSize;
            int end = (i == numSegments - 1) ? contentLength - 1 : start + segmentSize - 1;
            int segmentIndex = i;
            executor.execute(() -> downloadSegment(fileUrl, start, end, segmentIndex));
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // Ensure all segment files exist before merging
        for (int i = 0; i < numSegments; i++) {
            File segmentFile = new File("segment" + i + ".part");
            if (!segmentFile.exists()) {
                throw new IOException("Segment file missing: " + segmentFile.getName());
            }
        }

        mergeSegments(numSegments, outputFileName);
        System.out.println("File saved at: " + new File(outputFileName).getAbsolutePath());
    }

    private static void downloadSegment(String fileUrl, int start, int end, int segmentIndex) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
            conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
            InputStream input = conn.getInputStream();
            File segmentFile = new File("segment" + segmentIndex + ".part");
            FileOutputStream output = new FileOutputStream(segmentFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }

            output.close();
            input.close();
            conn.disconnect();

            System.out.println("Segment " + segmentIndex + " downloaded. Size: " + segmentFile.length() + " bytes");
        } catch (Exception e) {
            System.err.println("Error downloading segment " + segmentIndex);
            e.printStackTrace();
        }
    }

    private static void mergeSegments(int numSegments, String outputFileName) throws IOException {
        try (FileOutputStream output = new FileOutputStream(outputFileName)) {
            for (int i = 0; i < numSegments; i++) {
                File file = new File("segment" + i + ".part");
                try (FileInputStream input = new FileInputStream(file)) {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                }
                file.delete();
                System.out.println("Merged segment: " + file.getName());
            }
        }
        System.out.println("File download completed successfully.");
    }

    private static void openFileLocation(String outputFileName) {
        try {
            File file = new File(outputFileName);
            File parentDir = file.getParentFile();
            if (parentDir == null) {
                parentDir = new File(System.getProperty("user.dir")); // Default to current directory
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(parentDir);
            } else {
                System.out.println("Desktop operations not supported on this OS.");
            }
        } catch (Exception e) {
            System.out.println("Failed to open file location: " + e.getMessage());
        }
    }
}
*/

import java.awt.Desktop;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;

public class MultiThreadedDownloader {
    private static final int BUFFER_SIZE = 1024;
   public static int contentLength;

    public static void downloadFile(String fileUrl, int numSegments, String outputFileName) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        contentLength = conn.getContentLength();
       
        conn.disconnect();
        if (contentLength == -1) {
            throw new IOException("Failed to retrieve file size. Server did not return content length.");
        }

        ExecutorService executor = Executors.newFixedThreadPool(numSegments);
        int segmentSize = contentLength / numSegments;

        for (int i = 0; i < numSegments; i++) {
            int start = i * segmentSize;
            int end = (i == numSegments - 1) ? contentLength - 1 : start + segmentSize - 1;
            int segmentIndex = i;
            
            
            executor.execute(() -> downloadSegment(fileUrl, start, end, segmentIndex));
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        mergeSegments(numSegments, outputFileName);
        String outputFilepath=new File(outputFileName).getAbsolutePath();
        System.out.println("File saved at: " + new File(outputFileName).getAbsolutePath());
        openFileLocation(outputFilepath);
    }

    private static void downloadSegment(String fileUrl, int start, int end, int segmentIndex) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
            conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
            InputStream input =conn.getInputStream();
            FileOutputStream output = new FileOutputStream("segment" + segmentIndex + ".part");
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            int downloaded = 0;
            int segmentSize = end - start + 1;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.close();
            input.close();
            conn.disconnect();
            System.out.println("Segment " + segmentIndex + " downloaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mergeSegments(int numSegments, String outputFileName) throws IOException {
        try (FileOutputStream output = new FileOutputStream(outputFileName)) {
            for (int i = 0; i < numSegments; i++) {
                File file = new File("segment" + i + ".part");
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                input.close();
                file.delete();
            }
        }
        System.out.println("File download completed.");
    }

    private static void openFileLocation(String outputFilepath) {
    	 try {
             File file = new File(outputFilepath); 
             if (file.exists()) {
                 Desktop.getDesktop().open(file.getParentFile());
             } else {
                 System.out.println("File not found.");
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
}


/*
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

public class MultiThreadedDownloader {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        // Example usage
        String fileUrl = "https://example.com/samplefile.zip";
        int numSegments = 4;
        String outputFileName = "downloaded_file.zip";
        
        downloadFile(fileUrl, numSegments, outputFileName);
    }

    public static void downloadFile(String fileUrl, int numSegments, String outputFileName) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        int contentLength = conn.getContentLength();
        conn.disconnect();
        
        ExecutorService executor = Executors.newFixedThreadPool(numSegments);
        int segmentSize = contentLength / numSegments;

        for (int i = 0; i < numSegments; i++) {
            int start = i * segmentSize;
            int end;
            if (i == numSegments - 1) {
                end = contentLength - 1;
            } else {
                end = start + segmentSize - 1;
            }
            
            DownloadTask task = new DownloadTask(fileUrl, start, end, i);
            executor.execute(task);
        }
        
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        
        mergeSegments(numSegments, outputFileName);
        System.out.println("File saved at: " + new File(outputFileName).getAbsolutePath());
    }
}

class DownloadTask implements Runnable {
    private String fileUrl;
    private int start;
    private int end;
    private int segmentIndex;
    
    public DownloadTask(String fileUrl, int start, int end, int segmentIndex) {
        this.fileUrl = fileUrl;
        this.start = start;
        this.end = end;
        this.segmentIndex = segmentIndex;
    }
    
    @Override
    public void run() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
            conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
            InputStream input = conn.getInputStream();
            FileOutputStream output = new FileOutputStream("segment" + segmentIndex + ".part");
            
            byte[] buffer = new byte[MultiThreadedDownloader.BUFFER_SIZE];
            int bytesRead;
            
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            
            output.close();
            input.close();
            conn.disconnect();
            System.out.println("Segment " + segmentIndex + " downloaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class FileMerger {
    public static void mergeSegments(int numSegments, String outputFileName) throws IOException {
        FileOutputStream output = new FileOutputStream(outputFileName);
        
        for (int i = 0; i < numSegments; i++) {
            File file = new File("segment" + i + ".part");
            FileInputStream input = new FileInputStream(file);
            
            byte[] buffer = new byte[MultiThreadedDownloader.BUFFER_SIZE];
            int bytesRead;
            
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            
            input.close();
            file.delete();
        }
        
        output.close();
        System.out.println("File download completed.");
    }
}

*/

/*import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

public class MultiThreadedDownloader {
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        
    }

    public static void downloadFile(String fileUrl, int numSegments, String outputFileName) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("HEAD");
        int contentLength = conn.getContentLength();
        conn.disconnect();
        
        ExecutorService executor = Executors.newFixedThreadPool(numSegments);
        int segmentSize = contentLength / numSegments;

        for (int i = 0; i < numSegments; i++) {
            int start = i * segmentSize;
            int end = (i == numSegments - 1) ? contentLength - 1 : start + segmentSize - 1;
            int segmentIndex = i;
            executor.execute(() -> downloadSegment(fileUrl, start, end, segmentIndex));
        }
        
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        mergeSegments(numSegments, outputFileName);
    }

    private static void downloadSegment(String fileUrl, int start, int end, int segmentIndex) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
            conn.setRequestProperty("Range", "bytes=" + start + "-" + end);
            InputStream input = conn.getInputStream();
            FileOutputStream output = new FileOutputStream("segment" + segmentIndex + ".part");
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            output.close();
            input.close();
            conn.disconnect();
            System.out.println("Segment " + segmentIndex + " downloaded.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void mergeSegments(int numSegments, String outputFileName) throws IOException {
        try (FileOutputStream output = new FileOutputStream(outputFileName)) {
            for (int i = 0; i < numSegments; i++) {
                File file = new File("segment" + i + ".part");
                FileInputStream input = new FileInputStream(file);
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                input.close();
                file.delete();
            }
        }
        System.out.println("File download completed.");
    }
}*/
