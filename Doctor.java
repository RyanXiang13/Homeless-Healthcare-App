import java.io.*;
import java.util.*;

/**
 * Represents a doctor.
 */
public class Doctor extends Person {

  protected String practicingFrom;
  protected String licenseNumber;
  private Address officeAdddress;
  protected String specialty;
  protected String phoneNumber;

  private static ArrayList<Doctor> doctors = new ArrayList<>();

  /**
   * Defines categories for doctors.
   */
  public final static Hashtable<String, String> doctor_category = new Hashtable<String, String>();
  static {
    doctor_category.put("1", "Family medicine");
    doctor_category.put("2", "Cardiologist");
    doctor_category.put("3", "Neurologist");
    doctor_category.put("4", "Pediatrician");
    doctor_category.put("5", "Dentist");
    doctor_category.put("6", "Dermatologist");
    doctor_category.put("7", "Psychiatry");
    doctor_category.put("8", "Infectious Disease Specialist");
  }

  /**
   * Constructs a Doctor object with the specified attributes.
   *
   * @param name           the name of the doctor
   * @param dob            the date of birth of the doctor
   * @param gender         the gender of the doctor
   * @param practicingFrom the year from which the doctor started practicing
   * @param phoneNumber    the phone number of the doctor
   * @param specialty      the specialty of the doctor
   * @param licenseNumber  the license number of the doctor
   */
  public Doctor(String name, String dob, String gender, String practicingFrom,
                String phoneNumber, String specialty, String licenseNumber) {

    super(name, dob, gender);
    this.practicingFrom = practicingFrom;
    this.licenseNumber = licenseNumber;
    this.phoneNumber = phoneNumber;
    this.specialty = specialty;
  }

  /**
   * Constructs a Doctor object with the specified attributes.
   *
   * @param name           the name of the doctor
   * @param dob            the date of birth of the doctor
   * @param gender         the gender of the doctor
   * @param practicingFrom the year from which the doctor started practicing
   * @param phoneNumber    the phone number of the doctor
   * @param specialty      the specialty of the doctor
   * @param licenseNumber  the license number of the doctor
   * @param addr           the office address of the doctor
   */
  public Doctor(String name, String dob, String gender, String practicingFrom,
                String phoneNumber, String specialty, String licenseNumber, Address addr) {

    super(name, dob, gender);
    this.practicingFrom = practicingFrom;
    this.licenseNumber = licenseNumber;
    this.phoneNumber = phoneNumber;
    this.specialty = specialty;
    this.officeAdddress = addr;
  }


  /**
   * Gets the person ID of the doctor.
   *
   * @return the person ID of the doctor
   */
  public String getPersonID() {
    return this.getName() + "-" + this.phoneNumber;
  }

  /**
   * Gets the phone number of the doctor.
   *
   * @return the phone number of the doctor
   */
  public String getPhoneNumber() {
    return this.phoneNumber;
  }

  /**
   * Gets the license number of the doctor.
   *
   * @return the license number of the doctor
   */
  public String getLicenseNumber() {
    return this.licenseNumber;
  }

  /**
   * Gets the list of doctors.
   *
   * @return the list of doctors
   */
  public ArrayList<Doctor> getDoctors() {
    return doctors;
  }

  /**
   * Gets the office address of the doctor.
   *
   * @return the office address of the doctor
   */
  public Address getOfficeAddress() {
    return this.officeAdddress;
  }

  /**
   * Sets the office address of the doctor.
   *
   * @param addr the office address to set
   */
  public void setOfficeAddress(Address addr) {
    this.officeAdddress = addr;
  }

  /**
   * Returns a string representation of the doctor.
   *
   * @return a string representation of the doctor
   */
  public String toString() {
    
    String myPresentation = "";

    // Construct the doctor snapshot based on the type of the doctor
    if (this instanceof FamilyDoctor) {
      FamilyDoctor fd = (FamilyDoctor) this;
      myPresentation += fd.name + "  Family physician  Practicing from: " + fd.practicingFrom + "\n";
      myPresentation += "Walk-in: " + fd.getIsWalkinAccepted() + "\n";
      myPresentation += "Office phone number: " + fd.phoneNumber;
      myPresentation += "Address:\n" +
              fd.getOfficeAddress().getStreetNumber() + " " + fd.getOfficeAddress().getStreetName() +
              "\n" + fd.getOfficeAddress().getCity() + fd.getOfficeAddress().getProvince() +
              fd.getOfficeAddress().getPostalCode() + "\n";
    } else if (this instanceof SpecialistDoctor) {
      SpecialistDoctor fd = (SpecialistDoctor) this;
      myPresentation += fd.name + "  Family physician  Practicing from: " + fd.practicingFrom + "\n";
      myPresentation += "Affiliate hospital: " + fd.getAffiliatedHospital() + "\n";
      myPresentation += "Office phone number: " + fd.phoneNumber;
      myPresentation += "Address:\n" +
              fd.getOfficeAddress().getStreetNumber() + " " + fd.getOfficeAddress().getStreetName() +
              "\n" + fd.getOfficeAddress().getCity() + fd.getOfficeAddress().getProvince() + "\n";
    }

    return myPresentation;
  }

  /**
   * Saves the doctor's profile to the database.
   *
   * @param doctor the doctor to save
   */
  public static void saveProfileToDatabase(Doctor doctor) {
    try {
      
      // Open doctor data base to append the 'doctor' profile into doctor database
      File file = new File("Data/doctor.txt");
      FileWriter fw = new FileWriter(file, true);

      // Persist the doctor profile into database based on the type of the doctor
      if (doctor instanceof FamilyDoctor) {
        fw.write("\n" + doctor.name + "," + doctor.dob + "," + doctor.gender + "," + doctor.practicingFrom +
                "," + doctor.phoneNumber + "," + doctor.specialty + "," + doctor.licenseNumber + "," +
                ((FamilyDoctor) doctor).getIsWalkinAccepted());
      } else {
        fw.write("\n" + doctor.name + "," + doctor.dob + "," + doctor.gender + "," + doctor.practicingFrom +
                "," + doctor.phoneNumber + "," + doctor.specialty + "," + doctor.licenseNumber + "," +
                ((SpecialistDoctor) doctor).getAffiliatedHospital());
      }

      // Close the filewrite to release resource
      fw.close();
    } catch (IOException e) {
      System.out.println("File does not exist");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Searches for a doctor with the given name.
   *
   * @param name the name of the doctor to search for
   * @return the found doctor or null if not found
   */
  public static Doctor searchDoctor(String name) {
    try {
      loadDoctors();
      for (Doctor d : doctors) {
        if (d.getName().equalsIgnoreCase(name))
          return d;
      }
    } catch (Exception e) {
      System.out.println("Doctor does not exist");
    }
    return null;
  }

  /**
   * Searches for a doctor with the given name and phone number.
   *
   * @param name      the name of the doctor to search for
   * @param phoneNum  the phone number of the doctor to search for
   * @return the found doctor or null if not found
   */
  public static Doctor searchDoctor(String name, String phoneNum) {
    try {
      loadDoctors();
      // Load a fresh copy of db doctor into 'docotrs'      
      for (Doctor d : doctors) {
        if (d.getName().equalsIgnoreCase(name) && d.phoneNumber.equalsIgnoreCase(phoneNum)){
          // a doctor is found in matching 'name' and 'phoneNum'
          return d;
        }
      }
    } catch (Exception e) {
      System.out.println("Doctor does not exist");
    }
    
    // No doctor in our database is found to match 'name' and 'phoneNum' passed in
    return null;
  }

  /**
   * Searches for a doctor with the given specialty and location.
   *
   * @param specialty the specialty of the doctor to search for
   * @param area      the location area to search for
   * @return the found doctor or null if not found
   */
  public static Doctor searchDoctorBySpecialtyAndLocation(String specialty, String area) {
    
    try {
      // Load a fresh copy of db doctor into 'docotrs'
      loadDoctors();

      for (Doctor d : doctors) {
        if (d.specialty.equalsIgnoreCase(specialty) && (d.getOfficeAddress().getCity()).equalsIgnoreCase(area)) {
          // a doctor is found in matching 'specilty' and 'area'
          return d;
        }
      }
    } catch (Exception e) {
      System.out.println("Doctor does not exist");
    }

    // No doctor in our database is found to match 'specialty' and 'area' passed in
    return null;
  }

  /**
   * Loads the list of doctors from the database.
   */
  public static void loadDoctors() {
    
    String line;
    try {
      // Instantiate a BufferedReader to load doctor from database to 'doctors'
      BufferedReader br = new BufferedReader(new FileReader("Data/doctor.txt"));
      while ((line = br.readLine()) != null) {
        String[] arr = line.split(",");

        String name = arr[0];
        String dob = arr[1];
        String gender = arr[2];
        String practicingFrom = arr[3];
        String phoneNumber = arr[4];
        String specialty = arr[5];
        String licenseNumber = arr[6];
        String walkInOrAffiliateHospital = arr[7];
        Doctor d;

        // Instantiate doctor based on the type of doctor retrieved from database
        if (specialty.equalsIgnoreCase("Family medicine")) {
          String isWalkinAccepted = arr[7];
          d = new FamilyDoctor(name, dob, gender, practicingFrom, phoneNumber, specialty,
                  licenseNumber, isWalkinAccepted);
        } else {
          String affiliatedHospital = arr[7];
          d = new SpecialistDoctor(name, dob, gender, practicingFrom, phoneNumber, specialty,
                  licenseNumber, affiliatedHospital);
        }
        doctors.add(d);
      }

      br.close();
    } catch (IOException e) {
      System.out.println("File does not exist");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
   /**
   * Searches for a doctor's specility with the given name and phone number.
   *
   * @param doctorName the name of the doctor
   * @param doctorPhone the phone number of the doctor
   * @return the specility of doctor if found, or 'N/A' otherwise
   */
  public static String getSpecility(String doctorName, String doctorPhone)
  {
    loadDoctors();
    for (Doctor d : doctors) {
      if (d.name.equalsIgnoreCase(doctorName) && d.phoneNumber.equalsIgnoreCase(doctorPhone)) {
        return d.specialty;
      }
    }

    return "Others";
  }
}
/**
 * Represents a specialist doctor, extending the base Doctor class.
 */
class SpecialistDoctor extends Doctor {

  private String affiliatedHospital;

  /**
   * Constructs a SpecialistDoctor object with the specified details.
   *
   * @param name               the name of the specialist doctor
   * @param dob                the date of birth of the specialist doctor
   * @param gender             the gender of the specialist doctor
   * @param practicingFrom     the year the specialist doctor started practicing
   * @param phoneNumber        the phone number of the specialist doctor
   * @param specialty          the specialty of the specialist doctor
   * @param licenseNumber      the license number of the specialist doctor
   * @param affiliatedHospital the affiliated hospital of the specialist doctor
   */
  public SpecialistDoctor(String name, String dob, String gender, String practicingFrom,
                          String phoneNumber, String specialty, String licenseNumber, String affiliatedHospital) {

    super(name, dob, gender, practicingFrom, phoneNumber, specialty, licenseNumber);
    this.affiliatedHospital = affiliatedHospital;
  }

  /**
   * Constructs a SpecialistDoctor object with the specified details and address.
   *
   * @param name               the name of the specialist doctor
   * @param dob                the date of birth of the specialist doctor
   * @param gender             the gender of the specialist doctor
   * @param practicingFrom     the year the specialist doctor started practicing
   * @param phoneNumber        the phone number of the specialist doctor
   * @param specialty          the specialty of the specialist doctor
   * @param licenseNumber      the license number of the specialist doctor
   * @param affiliatedHospital the affiliated hospital of the specialist doctor
   * @param addr               the address of the specialist doctor
   */
  public SpecialistDoctor(String name, String dob, String gender, String practicingFrom,
                          String phoneNumber, String specialty, String licenseNumber, String affiliatedHospital, Address addr) {

    super(name, dob, gender, practicingFrom, phoneNumber, specialty, licenseNumber, addr);
    this.affiliatedHospital = affiliatedHospital;
  }

  /**
   * Gets the affiliated hospital of the specialist doctor.
   *
   * @return the affiliated hospital
   */
  public String getAffiliatedHospital() {
    return affiliatedHospital;
  }
}

/**
 * Represents a family doctor, extending the base Doctor class.
 */
class FamilyDoctor extends Doctor {

  private String isWalkinAccepted;

  /**
   * Constructs a FamilyDoctor object with the specified details.
   *
   * @param name               the name of the family doctor
   * @param dob                the date of birth of the family doctor
   * @param gender             the gender of the family doctor
   * @param practicingFrom     the year the family doctor started practicing
   * @param phoneNumber        the phone number of the family doctor
   * @param specialty          the specialty of the family doctor
   * @param licenseNumber      the license number of the family doctor
   * @param isWalkinAccepted   whether walk-ins are accepted by the family doctor
   */
  public FamilyDoctor(String name, String dob, String gender, String practicingFrom,
                      String phoneNumber, String specialty, String licenseNumber, String isWalkinAccepted) {

    super(name, dob, gender, practicingFrom, phoneNumber, specialty, licenseNumber);
    this.isWalkinAccepted = isWalkinAccepted;
  }

  /**
   * Constructs a FamilyDoctor object with the specified details and address.
   *
   * @param name               the name of the family doctor
   * @param dob                the date of birth of the family doctor
   * @param gender             the gender of the family doctor
   * @param practicingFrom     the year the family doctor started practicing
   * @param phoneNumber        the phone number of the family doctor
   * @param specialty          the specialty of the family doctor
   * @param licenseNumber      the license number of the family doctor
   * @param isWalkinAccepted   whether walk-ins are accepted by the family doctor
   * @param addr               the address of the family doctor
   */
  public FamilyDoctor(String name, String dob, String gender, String practicingFrom,
                      String phoneNumber, String specialty, String licenseNumber, String isWalkinAccepted, Address addr) {

    super(name, dob, gender, practicingFrom, phoneNumber, specialty, licenseNumber, addr);
    this.isWalkinAccepted = isWalkinAccepted;
  }

  /**
   * Gets whether walk-ins are accepted by the family doctor.
   *
   * @return "true" if walk-ins are accepted, "false" otherwise
   */
  public String getIsWalkinAccepted() {
    return this.isWalkinAccepted;
  }
}
