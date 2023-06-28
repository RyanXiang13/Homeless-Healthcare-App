import java.io.*;
import java.util.*;

/**
 * Represents an appointment for a homeless person with a doctor.
 */
public class Appointment {

  private String homelessName;
  private String homelessDob;
  private String doctorName;
  private String doctorPhone;
  private String appointmentDate;
  private String appointmentTiemslot;

  private static ArrayList<Appointment> allAppointments = new ArrayList<>();
  static {
    loadAllAppoitments();
  }

  /**
   * Represents the available timeslots for appointments.
   */
  public final static Hashtable<String, String> timeslots = new Hashtable<>();
  static {
    timeslots.put("1", "08:00AM");
    timeslots.put("2", "09:00AM");
    timeslots.put("3", "10:00AM");
    timeslots.put("4", "11:00AM");
    timeslots.put("5", "01:00PM");
    timeslots.put("6", "02:00PM");
    timeslots.put("7", "03:00PM");
    timeslots.put("8", "04:00PM");
  }

  /**
   * Constructs an Appointment object with the specified doctor name, doctor phone, and appointment date.
   *
   * @param doctorName       the name of the doctor
   * @param doctorPhone      the phone number of the doctor
   * @param appointmentDate  the date of the appointment
   */
  public Appointment(String doctorName, String doctorPhone, String appointmentDate) {
    this.doctorName = doctorName;
    this.doctorPhone = doctorPhone;
    this.appointmentDate = appointmentDate;
  }

  /**
   * Constructs an Appointment object with the specified details.
   *
   * @param homelessName        the name of the homeless person
   * @param homelessDob         the date of birth of the homeless person
   * @param doctorName          the name of the doctor
   * @param doctorPhone         the phone number of the doctor
   * @param appointmentDate     the date of the appointment
   * @param appointmentTiemslot the timeslot of the appointment
   */
  public Appointment(String homelessName, String homelessDob, String doctorName,
                     String doctorPhone, String appointmentDate, String appointmentTiemslot) {
    this.homelessName = homelessName;
    this.homelessDob = homelessDob;
    this.doctorName = doctorName;
    this.doctorPhone = doctorPhone;
    this.appointmentDate = appointmentDate;
    this.appointmentTiemslot = appointmentTiemslot;
  }

  /**
   * Gets the name of the homeless person.
   *
   * @return the name of the homeless person
   */
  public String getHomelessName() {
    return this.homelessName;
  }

  /**
   * Gets the date of birth of the homeless person.
   *
   * @return the date of birth of the homeless person
   */
  public String getHomelessDob() {
    return this.homelessDob;
  }

  /**
   * Gets the name of the doctor.
   *
   * @return the name of the doctor
   */
  public String getDoctorName() {
    return this.doctorName;
  }

  /**
   * Gets the phone number of the doctor.
   *
   * @return the phone number of the doctor
   */
  public String getDoctorPhone() {
    return this.doctorPhone;
  }

  /**
   * Gets the date of the appointment.
   *
   * @return the date of the appointment
   */
  public String getAppointmentDate() {
    return this.appointmentDate;
  }

  /**
   * Gets the timeslot of the appointment.
   *
   * @return the timeslot of the appointment
   */
  public String getAppointmentTiemslot() {
    return this.appointmentTiemslot;
  }

  /**
   * Returns a string representation of the Appointment object.
   *
   * @return a string representation of the Appointment object
   */
  public String toString() {
    return "Doctor: " + this.doctorName + "\tPhone: " + this.doctorPhone + "\tTime: " +
        timeslots.get(String.valueOf(this.appointmentTiemslot)) + " " + this.appointmentDate;
  }

  /**
   * Books an appointment by adding it to the list of appointments and saving it to a file.
   *
   * @param appointment the appointment to be booked
   */
  public static void bookAppointment(Appointment appointment) {
    try {
      File file = new File("Data/appointment.txt");
      FileWriter fw = new FileWriter(file, true);
      fw.write(appointment.homelessName + "," + appointment.homelessDob + "," + appointment.doctorName + "," +
          appointment.doctorPhone + "," + appointment.appointmentDate + "," + appointment.appointmentTiemslot +
          "\n");
      fw.close();
      allAppointments.add(appointment);
    } catch (IOException e) {
      System.out.println("File does not exist");
    }
  }

  /**
   * Updates an appointment by canceling the current appointment and booking the updated appointment.
   *
   * @param currentAppointment the current appointment to be updated
   * @param updatedAppointment the updated appointment
   */
  public static void UpdateAppointment(Appointment currentAppointment, Appointment updatedAppointment) {
    CancelAppointment(currentAppointment);
    bookAppointment(updatedAppointment);
  }

  /**
   * Cancels an appointment by removing it from the list of appointments and saving the updated list to a file.
   *
   * @param appointment the appointment to be canceled
   */
  public static void CancelAppointment(Appointment appointment) {
    for (Appointment a : allAppointments) {
      if (a.homelessName.equalsIgnoreCase(appointment.homelessName) &&
          a.homelessDob.equalsIgnoreCase(appointment.homelessDob) &&
          a.doctorName.equalsIgnoreCase(appointment.doctorName) &&
          a.doctorPhone.equalsIgnoreCase(appointment.doctorPhone) &&
          a.appointmentDate.equalsIgnoreCase(appointment.appointmentDate) &&
          a.appointmentTiemslot.equalsIgnoreCase(appointment.appointmentTiemslot)) {
        allAppointments.remove(a);
        break;
      }
    }
    SaveAppointmentsToDB();
  }

  /**
   * Finds all appointments for a homeless person with the specified name and date of birth.
   *
   * @param name the name of the homeless person
   * @param dob  the date of birth of the homeless person
   * @return an ArrayList of appointments for the specified homeless person
   */
  public static ArrayList<Appointment> findHomelessAppointment(String name, String dob) {
    ArrayList<Appointment> myAppointments = new ArrayList<>();
    for (Appointment a : allAppointments) {
      if (a.homelessName.equalsIgnoreCase(name) && a.homelessDob.equalsIgnoreCase(dob)) {
        myAppointments.add(a);
      }
    }
    return myAppointments;
  }

  /**
   * Gets the scheduled timeslots for a doctor on a specific date.
   *
   * @param docName   the name of the doctor
   * @param docPhone  the phone number of the doctor
   * @param appDate   the date of the appointments
   * @return an ArrayList of scheduled timeslots for the specified doctor and date
   */
  public static ArrayList<String> getScheduledTimeslot(String docName, String docPhone, String appDate) {
    ArrayList<String> bookedTimeslots = new ArrayList<>();
    for (Appointment a : allAppointments) {
      if (a.doctorName.equalsIgnoreCase(docName) && a.doctorPhone.equalsIgnoreCase(docPhone)
          && a.appointmentDate.equalsIgnoreCase(appDate)) {
        bookedTimeslots.add(a.appointmentTiemslot);
      }
    }
    return bookedTimeslots;
  }

  /**
   * Loads all appointments from a file and populates the list of appointments.
   */
  public static void loadAllAppoitments() {
    String line;
    try {
      BufferedReader br = new BufferedReader(new FileReader("Data/appointment.txt"));
      while ((line = br.readLine()) != null) {
        String[] arr = line.split(",");
        String homelessName = arr[0];
        String homelessDob = arr[1];
        String doctorName = arr[2];
        String doctoPhoneNum = arr[3];
        String appointmentDate = arr[4];
        String appointmentTimeSlot = arr[5];
        Appointment app = new Appointment(homelessName, homelessDob, doctorName, doctoPhoneNum,
            appointmentDate, appointmentTimeSlot);
        allAppointments.add(app);
      }
      br.close();
    } catch (IOException e) {
      System.out.println("File does not exist");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Saves all appointments from the list to a file.
   */
  public static void SaveAppointmentsToDB() {
    try {
      File file = new File("Data/appointment.txt");
      FileWriter fw = new FileWriter(file);
      for (Appointment s : allAppointments) {
        fw.write(s.homelessName + "," + s.homelessDob + "," + s.doctorName + "," + s.doctorPhone + "," +
            s.appointmentDate + "," + s.appointmentTiemslot + "\n");
      }
      fw.close();
    } catch (Exception e) {
      System.out.println("File does not exist");
    }
  }

  /**
   * Gets the number of appointments for each doctor's specialty.
   *
   * @return a Hashtable containing the number of appointments for each specialty
   */
  public static Hashtable<String, Integer> getDoctorAccessBySpecility() {
    Hashtable<String, Integer> appointmentBySpecility = new Hashtable<>();
    appointmentBySpecility.put("Family medicine", 0);
    appointmentBySpecility.put("Cardiologist", 0);
    appointmentBySpecility.put("Neurologist", 0);
    appointmentBySpecility.put("Pediatrician", 0);
    appointmentBySpecility.put("Dentist", 0);
    appointmentBySpecility.put("Dermatologist", 0);
    appointmentBySpecility.put("Psychiatry", 0);
    appointmentBySpecility.put("Infectious Disease Specialist", 0);
    appointmentBySpecility.put("Others", 0);
    for (Appointment a : allAppointments) {
      String specility = Doctor.getSpecility(a.doctorName, a.doctorPhone);
      appointmentBySpecility.put(specility, appointmentBySpecility.get(specility) + 1);
    }
    return appointmentBySpecility;
  }
}
