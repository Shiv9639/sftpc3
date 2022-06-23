package com.C3Collection.C3.C3ToSCH;
import com.C3Collection.C3.Model.C3File;
import com.C3Collection.C3.Model.LpvToken;
import com.C3Collection.C3.Service.LpvTokenServiceImpl;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import javax.swing.text.Document;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class C3ToSCH {
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    private final String CONTENT_TYPE = "application/csv";
        public void readDatFileToMongo ()
        {
            ArrayList<String> data = new ArrayList<String>();
            try {
                String[] lines = Files.readAllLines(new File("/home/shivang/Downloads/2022-05-30_12-00_Workflow_Reservations.dat").toPath()).toArray(new String[0]);
                for (String s : lines) {
                    data.add(s);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            writeCsvFile("/home/shivang/Downloads/2022-05-30_12-00_Workflow_Reservations.csv", data);
            C3File file = new C3File();
            Document doc = null;
            file.setFileName("/home/shivang/Downloads/2022-05-30_12-00_Workflow_Reservations.csv");

        }


    public static void writeCsvFile(String fileName, ArrayList<String> data) {
        String COMMA_DELIMITER = ",";
        String NEW_LINE_SEPARATOR = "\n";

        ArrayList<String> students = data;
        FileWriter fileWriter = null;
        try {
            int i = 0;
            fileWriter = new FileWriter(fileName);
            for (String student111 : students) {

                String datafinal[] = student111.split("\\|");
                for (String dat1 : datafinal) {
                    fileWriter.append(dat1);
                    fileWriter.append(COMMA_DELIMITER);
                }
                // String timeStamp=String.valueOf(LocalDateTime.now());
                // fileWriter.append(timeStamp);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }

    public void postCSVFile() throws Exception {
        try {
            String url = "";
            File dir = new File("/home/shivang/Documents/LCTFiles");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    // Do something with child
                    System.out.println("inside child--" + child.getName());
                    setFileName(child.getName());
                    url = "";
                    url += LPV_DELIVERY_END_POINT_VAIRABLE;
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpPost post = new HttpPost(url.trim().concat(getFileName().replace(".csv", "")));
                    post.setHeader(org.apache.http.HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
                    post.setHeader("interface","C3");
                    //post.setHeader(HttpHeaders.AUTHORIZATION, lpvToken.getToken_type() + " " + lpvToken.getAccess_token());
                    //post.setEntity(new UrlEncodedFormEntity(getLpvParams(entityName)));
                    //post.setEntity(EntityBuilder.create().setBinary(Files.readAllBytes(child.toPath()))
                      //      .setContentType(org.apache.http.entity.ContentType.create("application/csv")).build());
                    HttpResponse response = client.execute(post);
                    System.out.println("File posted successfully!!");
                 //   processLpvResponse(response, url.trim());
                }
            } else {
                System.out.println("Folder is empty. No Files found!");
            }

        } catch (Exception ex) {
            throw ex;
        }
    }

}