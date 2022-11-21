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

            //TODO Why the hell does the below line even work?
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
     * Task to fetch images for individual ImageViews
     * @param <V>
     */
    public class FetchImage<V> extends Task<Image> {

        private String imageUrl;

        public FetchImage(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        protected Image call() throws Exception {
            Image image = new Image(imageUrl);
            return image;
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
     * Class containing currently logged in user data regarding his/hers CV education details.
     */
    protected static class CVEdu {
        //private int EducationID;
        private String university;
        private String fieldOfStudy;
        private String beginingDate; //Day of beginning of studies
        private String graduationDate; //Day of graduation
        //private int userID;

        public CVEdu() {}

        public CVEdu(String uni, String fos, String dob, String dog){
            //this.EducationID=eduID;
            this.university = uni;
            this.fieldOfStudy = fos;
            this.beginingDate = dob;
            this.graduationDate = dog;
            //this.userID = uID;
        }

        //public void setEducationID(int eduID) {this.EducationID = eduID;}
        public void setUniversity(String uni) { this.university = uni;}
        public void setFoS(String fos) { this.fieldOfStudy = fos;}
        public void setDoB(String dob) { this.beginingDate = dob;}
        public void setDoG(String dog) { this.graduationDate = dog;}
        //public void setUserID(int uID) { this.userID = uID;}

        //public int getEducationID() { return EducationID; }
        public String getUniversity() {
            return university;
        }
        public String getFoS() { return fieldOfStudy; }
        public String getDoB() { return beginingDate; }
        public String getDoG() { return graduationDate; }
        //public int getUserID() { return userID; }
    }

    /**
     * Class containing currently logged in user data regarding his/hers CV interests details.
     */
    protected static class CVInterest {
        private String interestName;

        public CVInterest() {}

        public void setInterestName(String IN) { this.interestName = IN;}

        public String getInterestName() { return interestName; }
    }

    /**
     * Class containing currently logged in user data regarding his/hers CV achievements details.
     */
    protected static class CVAchievement {
        private String achievementName;

        public CVAchievement() {}

        public void setAchievementName(String achi) { this.achievementName = achi;}

        public String getAchievementName() { return achievementName; }
    }

    /**
     * Class containing currently logged in user data regarding his/hers experience mentioned in CV.
     */
    protected static class CVExperience {
        private String company;
        private String startDate;
        private String endDate;
        private String position;

        public CVExperience() {}

        public void setCompany(String comp) { this.company = comp;}
        public void setStartDate(String sd) { this.startDate = sd;}
        public void setEndDate(String ed) { this.endDate = ed;}
        public void setPosition(String pos) { this.position = pos;}

        public String getCompany() { return company; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getPosition() { return position; }
    }

    /**
     * Class containing currently logged in user data regarding his/hers known languages mentioned in CV.
     */
    protected static class CVLanguages {

        private String languageName;
        private String languageAdvancement;

        public CVLanguages(){}

        public void setLanguageName(String lN) { this.languageName = lN;}
        public void setLanguageAdvancement(String lA) { this.languageAdvancement = lA;}

        public String getLanguageName() { return languageName; }
        public String getLanguageAdvancement() { return languageAdvancement; }
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

    /**
     * Class containing currently logged in employer data.
     */
    protected static class Employer {
        private int ID;
        private String CorrespondingNumber;
        private String Email;
        private String CompanyName;
        private String HR;
        private String CompanyAdress;
        private String FirmSpecification;

        public Employer(){ }

        public Employer(int idnumber, String correspondingNumber, String email, String companyName, String hr, String companyAdress, String firmSpecification){
            this.ID = idnumber;
            this.CorrespondingNumber=correspondingNumber;
            this.Email=email;
            this.CompanyName=companyName;
            this.HR=hr;
            this.CompanyAdress=companyAdress;
            this.FirmSpecification=firmSpecification;
        }

        public void setId(int id) { this.ID = id;}
        public void setCorrespondingNumber(String correspondingNumber) { this.CorrespondingNumber=correspondingNumber; }
        public void setEmail(String email) {
            this.Email=email;
        }
        public void setName(String companyName) {
            this.CompanyName=companyName;
        }
        public void setHR(String hr) { this.HR = hr; }
        public void setCompanyAdress(String companyAdress) { this.CompanyAdress = companyAdress; }
        public void setFirmSpecification(String firmSpecification) { this.FirmSpecification = firmSpecification; }

        public int getId() {
            return ID;
        }
        public String getCorrespondingNumber() {
            return CorrespondingNumber;
        }
        public String getEmail() {
            return Email;
        }
        public String getCompanyName() {
            return CompanyName;
        }
        public String getHR() {
            return HR;
        }
        public String getCompanyAdress() { return CompanyAdress; }
        public String getFirmSpecification() { return FirmSpecification; }
    }

    /**
     * Class containing data used for showing potential employees.
     */
    protected static class Offer {
        private int employerID;
        private String languageRequirements;
        private String position;
        private String requirements;
        private Float salary;

        public Offer() {}

        public Offer(int id, String lanReq, String position, String jobReq, Float salary){
            this.employerID = id;
            this.languageRequirements = lanReq;
            this.position = position;
            this.requirements = jobReq;
            this.salary = salary;
        }

        public void setId(int id) { this.employerID = id; }
        public void setLanReq(String language) { this.languageRequirements = language; }
        public void setPosition(String position) { this.position = position; }
        public void setJobReq(String jobReq) { this.requirements = jobReq; }
        public void setSalary(Float salary) { this.salary = salary; }

        public int getId() { return this.employerID; }
        public String getLanReq() { return this.languageRequirements; }
        public String getPosition() { return this.position; }
        public String getJobReq() { return this.requirements; }
        public Float getSalary() { return this.salary; }

        @Override
        public String toString() {
            return "Offer{" +
                    "employerID=" + employerID +
                    ", languageRequirements='" + languageRequirements + '\'' +
                    ", position='" + position + '\'' +
                    ", requirements='" + requirements + '\'' +
                    ", salary=" + salary +
                    '}';
        }
    }

    protected static class CVInfo {
        private int CVnumber;
        private int userID;
        private String PhotoLink;
        private String BirthDate;
        private String Adress;

        public CVInfo() {}

        public int getCVnumber() {
            return CVnumber;
        }

        public int getUserID() {
            return userID;
        }

        public String getPhotoLink() {
            return PhotoLink;
        }

        public String getBirthDate() {
            return BirthDate;
        }

        public String getAdress() {
            return Adress;
        }

        public void setCVnumber(int CVnumber) {
            this.CVnumber = CVnumber;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public void setPhotoLink(String photoLink) {
            PhotoLink = photoLink;
        }

        public void setBirthDate(String birthDate) {
            BirthDate = birthDate;
        }

        public void setAdress(String adress) {
            Adress = adress;
        }

        public CVInfo(int CVnumber, int userID, String PhotoLink, String BirthDate, String Adress) {
            this.Adress = Adress;
            this.BirthDate = BirthDate;
            this.CVnumber = CVnumber;
            this.PhotoLink = PhotoLink;
            this.userID = userID;
        }
    }


}
