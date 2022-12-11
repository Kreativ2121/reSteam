package com.resteam;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Controller {

    /**
     * Read the URL and return the json data.
     * !!! Probably DEPRECATED; use readUrlConnection instead!
     */
    public static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);

            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];

            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            reader.close();

            return buffer.toString();

        } catch (IOException e) {
            System.err.println("Error occurred when connecting to the server.");
            return "null";

        } finally {
            if (reader != null)
                reader.close();
        }
    }

    /**
     * Read the URL and return the token for authorization and data fetching.
     * @param urlString
     * @return
     * @throws Exception
     */
    public static String readUrlConnection(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            String request = "";

            /* Handling a connection a proper way -> It will take longer but will await for server answer */
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", request);
            int responseCode = con.getResponseCode();
            InputStream error = con.getErrorStream();
            //System.err.println(error);

            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            /* Parsing data returned from server */
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            reader.close();
            return buffer.toString();

        } catch (IOException e) {
            System.err.println("Error occurred when connecting to the server.");
            return "null";

        } finally {
            if (reader != null)
                reader.close();
        }
    }

    /**
     * Class containing currently logged in user data.
     */
    protected static class User {
        private int ID;
        private String ContactNumber;
        private String Email;
        private String Name;

        public User(){ }

        public User(int idnumber, String contactNumber, String email, String name){
            this.ID = idnumber;
            this.ContactNumber=contactNumber;
            this.Email=email;
            this.Name=name;
        }

        public void setId(int id) { this.ID = id;}
        public void setContactNumber(String contactNumber) { this.ContactNumber=contactNumber; }
        public void setEmail(String email) {
            this.Email=email;
        }
        public void setName(String name) {
            this.Name=name;
        }

        public int getId() {
            return ID;
        }
        public String getContactNumber() {
            return ContactNumber;
        }
        public String getEmail() {
            return Email;
        }
        public String getName() {
            return Name;
        }
    }

    /**
     * Class containing only type of the user and it's corresponding ID. Compliant with GDPR.
     */
    protected static class UserType {
        private int ID;
        private String Type;

        public UserType(){ }

        public UserType(int idNumber, String typeOfUser){
            this.ID = idNumber;
            this.Type = typeOfUser;
        }

        public void setId(int idNumber) { this.ID = idNumber;}
        public void setType(String typeOfUser) { this.Type=typeOfUser; }

        public int getId() {
            return ID;
        }
        public String getType() {
            return Type;
        }
    }
}
