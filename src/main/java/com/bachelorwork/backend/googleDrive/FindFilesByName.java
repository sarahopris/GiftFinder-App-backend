package com.bachelorwork.backend.googleDrive;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class FindFilesByName {

    // com.google.api.services.drive.model.File
    public static URL getGoogleFilesByName(String fileNameLike) throws IOException {

        Drive driveService = GoogleDriveUtils.getDriveService();
        String rootFolderIdParent = "1rquk_JMsb1zsaPJIowF2C-6Oes8-hnhu";
        String pageToken = null;
        List<File> list = new ArrayList<File>();

        String query = null;
        if (rootFolderIdParent == null) {
            query = " name = '" + fileNameLike + "' " //
                    + " and mimeType != 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " name = '" + fileNameLike + "' " //
                    + " and mimeType != 'application/vnd.google-apps.folder' " //
                    + " and '" + rootFolderIdParent + "' in parents";
        }

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime, mimeType
                    .setFields("nextPageToken, files(id, name, createdTime, mimeType)")//
                    .setPageToken(pageToken).execute();
            list.addAll(result.getFiles());
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        URL baseUrl = new URL("https://drive.google.com/");
        return new URL(baseUrl,"uc?id=" + list.get(0).getId());
    }



    public static void main(String[] args) throws IOException {

        URL imgUrl = getGoogleFilesByName("makeup_table.jpg");

//            System.out.println("Mime Type: " + folder.getMimeType() + " --- Name: " + folder.getName());


        System.out.println("Img url:" + imgUrl);
    }

}