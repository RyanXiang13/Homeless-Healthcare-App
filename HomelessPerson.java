import java.io.*;
import java.util.*;

/**
 * Represents a homeless person, extending the Person class.
 */
public class HomelessPerson extends Person {

  private String medicalCondition;
  private ArrayList<Appointment> appointments;
  private static ArrayList<HomelessPerson> homeless = new ArrayList<>();
  private Address residenceAddress;

  /**
   * Constructs a HomelessPerson object with the specified name, date of birth, and gender.
   * @param name the name of the homeless person
   * @param dob the date of birth of the homeless person
   * @param gender the gender of the homeless person
   */
  public HomelessPerson(String name, String dob, String gender/* , String medicalCondition */) {
    super(name, dob, gender);
    // this.medicalCondition = medicalCondition; // Let's remove it for know to simplify the complexity ???
    this.appointments = new ArrayList<>(); // this can be moved when you declare appointments ???
  }

  /**
   * Constructs a HomelessPerson object with the specified name, date of birth, gender, and residence address.
   * @param name the name of the homeless person
   * @param dob the date of birth of the homeless person
   * @param gender the gender of the homeless person
   * @param residenceAddress the residence address of the homeless person
   */
  public HomelessPerson(String name, String dob, String gender, Address residenceAddress) {
    super(name, dob, gender);
    // this.medicalCondition = medicalCondition; // Let's remove it for know to simplify the complexity ???
    this.appointments = new ArrayList<>(); // this can be moved when you declare appointments ???
    this.residenceAddress = residenceAddress;
  }

  /**
   * Constructs a HomelessPerson object with an empty medical condition.
   */
  public HomelessPerson() {
    this.medicalCondition = "";
  }

  /**
   * Gets the medical condition of the homeless person.
   * @return the medical condition
   */
  public String getMedicalCondition() {
    return this.medicalCondition;
  }

  /**
   * Gets the appointments of the homeless person.
   * @return the list of appointments
   */
  public ArrayList<Appointment> getAppointments() {
    return this.appointments;
  }

  /**
   * Sets an appointment for the homeless person.
   * @param s the appointment to be set
   */
  public void setAppointment(Appointment s) {
    this.appointments.add(s);
  }

  /**
   * Changes an appointment for the homeless person at the specified index.
   * @param idx the index of the appointment to be changed
   * @param s the new appointment
   */
  public void changeAppointment(int idx, Appointment s) {
    this.appointments.set(idx, s);
  }

  /**
   * Gets the list of all homeless persons.
   * @return the list of homeless persons
   */
  public ArrayList<HomelessPerson> getHomeless() {
    return homeless;
  }

  /**
   * Gets the residence address of the homeless person.
   * @return the residence address
   */
  public Address getResidenceAddress() {
    return this.residenceAddress;
  }

  /**
   * Sets the residence address of the homeless person.
   * @param addr the new residence address
   */
  public void setResidencAddress(Address addr) {
    this.residenceAddress = addr;
  }

  /**
   * Returns a string representation of the homeless person.
   * @return a string representation of the homeless person
   */
  public String toString() {
    String mySnapshot = this.name + "\tDate of Birth: " + this.dob + "\tGender: " + this.gender + "\n";
    mySnapshot += this.residenceAddress.getStreetNumber() + " " + this.residenceAddress.getStreetName() 
                  + " " + this.residenceAddress.getCity() + " " + this.residenceAddress.getProvince() 
                  + " " + this.residenceAddress.getPostalCode();
    return  mySnapshot;
  }

  /**
   * Saves the profile of the homeless person to the database.
   * @param p the homeless person to be saved
   */
  public static void saveProfileToDatabase(HomelessPerson p) {
    homeless.add(p);
    try {
      File file = new File("Data/homeless.txt");
      FileWriter fw = new FileWriter(file, true);
      fw.write("\n" + p.getName() + "," + p.getDob() + "," + p.getGender());
      fw.close();
    } catch (IOException e) {
      System.out.println("File does not exist");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Searches for a homeless person in the database by name and birthdate.
   * @param name the name of the homeless person to search for
   * @param birthdate the birthdate of the homeless person to search for
   * @return the found homeless person, or null if not found
   */
  public static HomelessPerson searchHomeless(String name, String birthdate) {
    loadHomeless();
    try {
      for (HomelessPerson h : homeless) {
        if (name.equalsIgnoreCase(h.getName()) && birthdate.equals(h.getDob()))
          return h;
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
    return null;
  }

  /**
   * Loads the list of homeless persons from the database.
   */
  public static void loadHomeless() {
    homeless.clear();
    String line;
    try {
      BufferedReader br = new BufferedReader(new FileReader("Data/homeless.txt"));
      while ((line = br.readLine()) != null) {
        String[] arr = line.split(",");
        homeless.add(new HomelessPerson(arr[0], arr[1], arr[2]));
      }
      br.close();
    } catch (IOException e) {
      System.out.println("File does not exist");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Gets the total number of homeless persons in the database.
   * @return the total number of homeless persons
   */
  public static int GetTotalNumOfHomeless() {
    loadHomeless();
    return homeless.size();
  }
}
