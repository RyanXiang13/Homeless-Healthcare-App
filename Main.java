import java.io.*;
import java.util.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The Main class represents the main entry point of the application.
 */
class Main {

  private static String menuStr = "";

  // Declare constant int varible for each menu to avoid magic numbers
  private static final int MENU_HOMELESS = 1;
  private static final int MENU_HOMELESS_SEARCH_HOMELESS = 1;
  private static final int MENU_HOMELESS_ADD_HOMELESS = 2;
  private static final int MENU_HOMELESS_BACK_TO_MAIN = 3;

  private static final int MENU_DOCTORS = 2;
  private static final int MENU_DOCTORS_SEARCH_DOCTOR = 1;
  private static final int MENU_DOCTORS_ADD_DOCTOR = 2;
  private static final int MENU_DOCTORS_BACK_TO_MAIN = 3;

  private static final int MENU_SCHEDULE = 3;
  private static final int MENU_SCHEDULE_BOOK = 1;
  private static final int MENU_SCHEDULE_UPDATE = 2;
  private static final int MENU_SCHEDULE_CANCEL = 3;
  private static final int MENU_SCHEDULE_BACK_TO_MAIN = 4;

  private static final int MENU_SUMMARY = 4;
  private static final int MENU_SUMMARY_APPOINTMENT_BY_SPECILITY = 1;  
  

  private static final int MENU_EXIT_APP = 5;

  /**
   * The main method is the entry point of the application.
   *
   * @param args The command-line arguments passed to the application.
   */
  public static void main(String[] args) {

    loadMenusContext();

    // Initizlize int variable 'curMenu' to the 1st menu after application load and
    // use it to track the menu navigation forward and back easily
    int curMenu = 0;

    // Initialize Scanner object to read in user input
    Scanner input = new Scanner(System.in);

    displayMenu("MENU_APPLICATION_LOAD");
    
    // While loop is used to all the users to repeatedly navigate through menus,
    // make selection and display the cost results, unless the selection to exit
    // the application execution is selected.
    while (true) {
      int menuSelection = checkMenuChoice(5);

      if (menuSelection == MENU_HOMELESS) {
        while (true) {

          // display Menu for "Homeless Profile Management" option
          displayMenu("MENU_HOMELESS");
          int choice = checkMenuChoice(3);

          System.out.println();
          if (choice == MENU_HOMELESS_SEARCH_HOMELESS) {
            // Menu option "Search for a homeless person" is selected
            
            while (true) {
              // Prompt for name and dob of the homeless to search for
              System.out.print("Enter the name of the homeless person: ");
              String name = input.nextLine();
              String dob = isValidDate(false);
              HomelessPerson homeless = HomelessPerson.searchHomeless(name, dob);
              if (homeless == null) {
                // Re-prompt the users to enter name and dob if their name + dob
                // are not stored in database
                System.out.println("Person not found. Please re-enter.\n");
                continue;
              } else {

                // User enterd valid name + dob that can be found from our database
                // Get address for the homeless
                Address addr = Address.searchAddress(homeless);
                homeless.setResidencAddress(addr);

                // Display the Homeless profile
                System.out.println();
                System.out.println(homeless.toString());
                System.out.println();
                break;
              }
            }

          } else if (choice == MENU_HOMELESS_ADD_HOMELESS) {

            // Menu option "Add a homeless person" is selected

            // Prompt the user to enter name, dob and gender and address of the homeless
            System.out.print("Enter your name (First Last): ");
            String name = input.nextLine();
            String dob = isValidDate(false);
            System.out.print("Enter your gender (Male or Female): ");
            String gender = input.nextLine();
            Address addr = getAddressFromUserInput(input);
            HomelessPerson myProfile = new HomelessPerson(name, dob, gender, addr);

            // Save homeless profile to homeless database and address database
            HomelessPerson.saveProfileToDatabase(myProfile);
            Address.saveAddressToDatabase(myProfile);

            System.out.println();
            System.out.println("The homeless profile has been successfully saved in the database.");
            continue;
          } else if (choice == MENU_HOMELESS_BACK_TO_MAIN) {
            // option "Back to main menu" is selected, clear screen and break to main menu
            clearScreen();
            break;
          }
        }


      } else if (menuSelection == MENU_DOCTORS) {
        while (true) {

          // Disply "Doctor Profiles" menu
          displayMenu("MENU_DOCTOR");

          int choice = checkMenuChoice(3);
          if (choice == MENU_DOCTORS_SEARCH_DOCTOR) {
            while (true) {

              // "Search for a doctor" option is selected

              // Prompt user for the name of the doctor to search
              System.out.print("Enter the name of the doctor: ");
              String name = input.nextLine();

              Doctor doctor = Doctor.searchDoctor(name);
              if (doctor == null) {
                // Doctor not found, prompt for re-enter the name of the doctor
                System.out.println("Doctor does not exist. Please try again.");
              } else {

                // Doctor is found in database, retrieve the address associated with
                // the doctor found. And the display the full profile of the doctor
                Address addr = Address.searchAddress(doctor);
                doctor.setOfficeAddress(addr);
                System.out.println();
                System.out.println(doctor.toString());
                break;
              }
            }
            continue;

          } else if (choice == MENU_DOCTORS_ADD_DOCTOR) {

            // Menu option "Add a doctor" is selected

            // Prompt the user for common doctor inforation such as name, gender, contac phone number, 
            // license number, specility
            System.out.println("Please enter the following information of the doctor: ");
            System.out.print("Name (Firstname Lastname): ");
            String name = input.nextLine();
            String dob = isValidDate(false);
            System.out.print("Gender (Male or Female): ");
            String gender = input.nextLine();
            System.out.print("Practicing from (mm/dd/yyyy): ");
            String practicingFrom = input.nextLine();

            System.out.print("Phone Number (ddd-ddd-dddd): ");
            String phoneNumber = input.nextLine();

            System.out.print("License number: ");
            String licenseNumber = input.nextLine();

            String specility = getDoctorSpecialtyFromUserInput(input);

            // Ask for special information depend on whether the doctor is a family doctor or
            // a specialist
            String isWalkinAccepted = "No";
            String affiliatedHospital = "None";
            boolean isFamilyDoctor = specility.equalsIgnoreCase("Family medicine");
            if (isFamilyDoctor) {
              System.out.println("A family doctor is selected. Is Walk-in accepted? (Yes or No): ");
              isWalkinAccepted = input.nextLine();
            } else {
              System.out.println("A specialist doctor is selected. What is the affiliated hospital: ");
              affiliatedHospital = input.nextLine();
            }

            // Ask user for doctor's address
            Address addr = getAddressFromUserInput(input);

            // Instaniate doctor object based on the type of doctor(family doctors or specialists)
            Doctor myProfile;
            if (isFamilyDoctor) {
              myProfile = new FamilyDoctor(name, dob, gender, practicingFrom, phoneNumber, specility,
                      licenseNumber, isWalkinAccepted, addr);
            } else {
              myProfile = new SpecialistDoctor(name, dob, gender, practicingFrom, phoneNumber, specility,
                      licenseNumber, affiliatedHospital, addr);
            }

            // Save the doctor profile 
            Doctor.saveProfileToDatabase(myProfile);
            Address.saveAddressToDatabase(myProfile);

            System.out.println();
            System.out.println("The homeless profile has been successfully saved in the database.");
            continue;

          } else if (choice == MENU_DOCTORS_BACK_TO_MAIN) {

            // "Back to main menu" is selected, clear screen and break into the main menu
            clearScreen();
            break;
          }
        }       

      } else if (menuSelection == MENU_SCHEDULE) {

        // Main menu option "Medical Appointment Management" is selected
        HomelessPerson homeless = null;

        while (true) {

          // Ask for the name and dob of the homeless. If the name and dob entered cannot be 
          // found in our database, repeatedly prompt the user to reenter
          System.out.print("Enter the name of the homeless: ");
          String homeLessName = input.nextLine();
          String homelessDob = isValidDate(false);

          homeless = HomelessPerson.searchHomeless(homeLessName, homelessDob);
          if (homeless==null) {
            System.out.println("Homeless person not found. Please try again.\n");
          }
          else {
            break;
          }
        }
        while (true) {

          // Display Menu "Scheduling Appointments" 
          displayMenu("MENU_SCHEDULE");

          int choice = checkMenuChoice(4);
          if (choice == MENU_SCHEDULE_BOOK) {

            // Menu option "Book an appointment" is selected
            String doctorName="";
            String doctorPhoneNum;
            Doctor myDoctor = null;
  
              while (true) {

                // Enter doctor inforation, including name and contact phone number
                System.out.print("Name of the doctor: ");
                doctorName =  input.nextLine();
                System.out.print("Phone number of the doctor(ddd-ddd-dddd): ");            
                doctorPhoneNum =  input.nextLine();

                // Try retrieving the dcotor from our database based on the name and 
                // phone number entered. If not found, continue to ask user for reentering
                // the name and phone number
                myDoctor = Doctor.searchDoctor(doctorName, doctorPhoneNum);
                if (myDoctor == null) {
                  System.out.println("Doctor does not exist. Please try again\n");
                }
                else {
                  break;
                }
              }

              // Ask the user to enter a valid appointment date
              String appointmentDate =  isValidDate(true);           

              // Calculated the timeslots available for the selected doctor and date
              System.out.println("Time available");
              ArrayList<String> bookedTimeSlots = Appointment.getScheduledTimeslot(doctorName, doctorPhoneNum, 
                                                                                   appointmentDate);            
              for (int i=1; i <= 8; i++) {

                String timeSlot = Appointment.timeslots.get(String.valueOf(i));
                if (bookedTimeSlots.contains(String.valueOf(i))) {
                  timeSlot += "(Booked)";
                }
                System.out.print(String.valueOf(i) + ". " + timeSlot + "   ");
              }

              // Ask the user to selecte a timeslot
              System.out.print("\nPlease select a timeslot: ");  
              int timeSlotChoice = Integer.parseInt(input.nextLine());          

              // Now we have all data needed to create an appointment
              Appointment appointment = new Appointment(homeless.getName(), homeless.getDob(), myDoctor.getName(),     
                                       myDoctor.getPhoneNumber(), appointmentDate, String.valueOf(timeSlotChoice));
              Appointment.bookAppointment(appointment);

              // Display the appointment booked to the user
              System.out.println("You appointment has been booked: ");
              System.out.println("Doctor: " + myDoctor.getName() + "  Phone #: " + myDoctor.getPhoneNumber());
              System.out.println("Time: " +  Appointment.timeslots.get(String.valueOf(timeSlotChoice)) + " " +   
                               appointmentDate);            
              
          } else if (choice == MENU_SCHEDULE_UPDATE) {
  
            // Menu option "Change an appointment" is selected

            // Load from the db all the appointment currently booked for the homeless
            ArrayList<Appointment> mySchedules = Appointment.findHomelessAppointment(homeless.getName(),   
                                                                               homeless.getDob());  
            if (mySchedules.size() == 0) {
              
              // Currently no appointments scheduled for this homeless
              System.out.println();            
              System.out.println("You don't have an appointment scheduled as of now.");
              System.out.println();                        
            }  else {

              // Some appointment(s) found for this homeless, display these appointments
              int appNum = 1;
              for(Appointment s : mySchedules) {
                System.out.println(appNum++ + ". " + s.toString());
              }
              System.out.println();

              // Ask the user whether he/she wants to change the existing appointment
              System.out.print("Please select the appointment to update or enter 0 to exit: ");
              int noAppToUpdate = Integer.parseInt(input.nextLine());
              if (noAppToUpdate==0) {
                continue;
              }
              Appointment currentApp = mySchedules.get(noAppToUpdate-1);

              // Ask the users to enter the date for the new appointment 
              String appointmentDate = isValidDate(true);        
  
              // Get booked timeslots for the selected date for the doctor
              ArrayList<String> bookedTimeSlots = Appointment.getScheduledTimeslot(currentApp.getDoctorName(), 
                                                                               currentApp.getDoctorPhone(),
                                                                               appointmentDate);
              System.out.println("Time available");
              for (int i=1; i <= 8; i++) {
                
                String timeSlot = Appointment.timeslots.get(String.valueOf(i));
                if (bookedTimeSlots.contains(String.valueOf(i))) {
                  timeSlot += "(Booked)";
                }
                System.out.print(String.valueOf(i) + ". " + timeSlot + "   ");
              }

              // Ask the user to select which appointment he/she wants to change 
              System.out.print("\nPlease select a timeslot: ");  
              int timeSlotChoice = Integer.parseInt(input.nextLine());          

              // Update the appointment with the date and timeslot selected, and save the change to 
              // database
              Appointment updatedApp = new Appointment(currentApp.getHomelessName(), currentApp.getHomelessDob(), 
                                                 currentApp.getDoctorName(), currentApp.getDoctorPhone(), 
                                                 appointmentDate, String.valueOf(timeSlotChoice));
              
              Appointment.UpdateAppointment(currentApp, updatedApp);            
            }
  
          } else if (choice == MENU_SCHEDULE_CANCEL) {

            // Menu option "Cancel an appointment" isa selected

            // Load from the db all the appointment currently booked for the homeless
            ArrayList<Appointment> mySchedules = Appointment.findHomelessAppointment(homeless.getName(),   
                                                                               homeless.getDob());  
            if (mySchedules.size() == 0) {
              // Currently no appointments scheduled for this homeless              
              System.out.println();            
              System.out.println("You don't have an appointment scheduled as for now.");
              System.out.println();                        
            }
            else {
              // Some appointment(s) found for this homeless, display these appointments              
              int appNum = 1;
              for(Appointment s : mySchedules) {
                System.out.println(appNum++ + ". " + s.toString());
              }
              System.out.println();
              // Ask the user to select appointment to cacnel
              System.out.print("Please select the appointment to cancel or enter 0 to exit: ");
              int noAppToUpdate = Integer.parseInt(input.nextLine()); 
              if (noAppToUpdate == 0) {
                continue;
              }

              // Cancel the selected appointment
              Appointment appToCancel = mySchedules.get(noAppToUpdate-1);
              Appointment.CancelAppointment(appToCancel);            
            }
            
            
          } else if (choice == MENU_SCHEDULE_BACK_TO_MAIN) {

            // "Back to main menu" is selected, clear screen and break into the main menu            
            clearScreen();
            break;
          }
        }

      } else if (menuSelection == MENU_SUMMARY) {

        // Menu option "Homeless Medical Service Report" is selected
        while (true) {

          // Display menu "Medical Service Report"
          displayMenu("MENU_SUMMARY");
          
          int choice = checkMenuChoice(2);
          if (choice == MENU_SUMMARY_APPOINTMENT_BY_SPECILITY) {

            // Menu option "Homeless medical appointments by category" is selected

            // Get the stats info for the homeless accessing medical service by doctor category
            Hashtable<String, Integer> appointmentsBySpecilty =Appointment.getDoctorAccessBySpecility();
            System.out.println();

            // Display the summary of homeless access to medical service by doctor category
            System.out.println("\033[94mSummary of appointments by doctor category: ");            
            System.out.println();                        
            System.out.println("Medical Searvice Catogory     \t\tNumber of Access\033[0m");
            for (Map.Entry<String, Integer> mapElement : appointmentsBySpecilty.entrySet()) {
              String key = mapElement.getKey();
              key = formatSpecialtyOutput(key);

              Integer value = mapElement.getValue();
              System.out.println(key + "\t\t" + value);            
            }
            System.out.println();
          }
          else {
            clearScreen();
            break;
          }
        }
        
      } else if (menuSelection == MENU_EXIT_APP) {

        // "Back to main menu" is selected, clear screen and break into the main menu     
        System.out.print("\nThank you for using the application!");
        break;
      }
    }
  }

  
  /**
 * Loads the context of menus from a file.
 */
public static void loadMenusContext() {
    try {
        // Initialize the Scanner object to read data from file 'menu.txt'
        Scanner fileInput = new Scanner(new File("menu.txt"));

        // Traverse the menu.txt line by line to image the content of the file
        // into String var 'menuStr', with all format of the context reserved.
        while (fileInput.hasNextLine()) {
            menuStr += fileInput.nextLine() + "\n";
        }

        // Close the scanner to release resource
        fileInput.close();
    } catch (IOException ex) {
        // Catch exception if file 'menu.txt' does not exist and print
        // out friendly message to the users
        System.out.println("The file 'menu.txt' is not found.");
    }
}

  /**
   * Displays a menu based on the selected menu parameter.
   *
   * @param selectedMenu the menu to be displayed
   */
  public static void displayMenu(String selectedMenu) {
      // From the all menuStr 'menuStr', find the start and end position of the menu
      // 'selectedMenu' to be displayed.
      int start = menuStr.indexOf("[" + selectedMenu + "_START]") + ("[" + selectedMenu + "_START]").length();
      int end = menuStr.indexOf("[" + selectedMenu + "_END]", start);
  
      // Using the subString method to carve out the string for 'selectedMenu' from
      // 'menuStr'
      String menuContent = menuStr.substring(start, end);
  
      // Plug in actual formatting code as per specified in the 'menuStr'. Ex. the
      // [BlueColor] specified in the 'menuStr' (from menu.txt file) will be replaced
      // by actual format code for blue font "\033[94m".
      menuContent = menuContent.replace("[BlueColor]", "\033[94m")
                               .replace("[ColorEnd]", "\033[0m")
                               .replace("[CyanColor]", "\033[96m")
                               .replace("[RedColor]", "\033[91m")
                               .replace("[Bold]", "\033[1m")
                               .replace("[Cyan_underscore]", "\033[96m\033[4m");
  
      // Display the menu 'selectedMenu'
      System.out.println(menuContent);
  }
  
  /**
   * Clears the console contents and displays the application description block.
   */
  private static void clearScreen() {
      // Clear console contents
      System.out.print("\033[H\033[2J");
      System.out.flush();
  
      // Redisplay application description block
      displayMenu("MENU_APPLICATION_LOAD");
  }
  
  /**
   * Retrieves the address information from user input.
   *
   * @param input the Scanner object used for input
   * @return the Address object containing the user's address information
   */
  private static Address getAddressFromUserInput(Scanner input) {

    // Collect address related info from the user
    System.out.println("Enter your Address: ");
    System.out.print("  - Street number: ");
    String streetNum = input.nextLine();
    System.out.print("  - Street name: ");
    String streetName = input.nextLine();
    System.out.print("  - City: ");
    String city = input.nextLine();
    System.out.print("  - Province: ");
    String province = input.nextLine();
    System.out.print("  - Postal code: ");
    String postalCode = input.nextLine();
    // instantiate the Address object based on the user inputs
    Address addr = new Address(streetNum, streetName, city, province, postalCode);
  
    return addr;
  }
  
  /**
   * Retrieves the selected doctor specialty from user input.
   *
   * @param input the Scanner object used for input
   * @return the selected doctor specialty as a string
   */
  private static String getDoctorSpecialtyFromUserInput(Scanner input) {
    
    System.out.println();
    System.out.println("Please select the type of doctor you want to see: ");
    System.out.println("1. Family medicine");
    System.out.println("2. Cardiologist");
    System.out.println("3. Neurologist");
    System.out.println("4. Pediatrician");
    System.out.println("5. Dentist");
    System.out.println("6. Dermatologist");
    System.out.println("7. Psychiatry");
    System.out.println("8. Infectious Disease Specialist");
    int doctorCatgory = Integer.parseInt(input.nextLine());
  
    return Doctor.doctor_category.get(String.valueOf(doctorCatgory));
  }
  
  /**
   * Checks the user's menu choice and validates it.
   *
   * @param max the maximum valid menu option
   * @return the validated menu choice as an integer
   */
  public static int checkMenuChoice(int max) {
    
    Scanner input = new Scanner(System.in);
    int choice = 0;
    while (true) {
      System.out.print("Enter menu option: ");
      try {
        choice = Integer.parseInt(input.nextLine());
      } catch (Exception e) {
        System.out.println("Please enter an integer.\n");
        continue;
      }
      
      if (choice < 1 || choice > max) {
        System.out.println("Please enter an integer between 1 and " + max + ".\n");
      } else {
        return choice;
      }
    }
  }
  
  /**
   * Validates and retrieves a date from the user.
   *
   * @param apt indicates if the date is for an appointment (true) or date of birth (false)
   * @return the validated date as a string in the format "MM/dd/yyyy"
   */
  public static String isValidDate(boolean apt) {
    
      String formatString = "MM/dd/yyyy"; // define the format string
      Scanner input = new Scanner(System.in); // initialize scanner
      while (true) { // loop
          if (!apt) { // not an appointment
              System.out.print("Enter the date of birth (mm/dd/yyyy): ");
          } else { // an appointment
              System.out.print("Enter the date of the appointment (mm/dd/yyyy): ");
          }
          String date = input.nextLine(); // get date
          try {
              // format the day
              SimpleDateFormat format = new SimpleDateFormat(formatString);
              format.setLenient(false);
              format.parse(date); // parse the date and see if there is an error to be caught
              if (apt) {
                  Date currentDate = new Date(); // current date
                  Date enteredDate = format.parse(date); // date entered
                  if (currentDate.after(enteredDate)) { // use built-in method to compare the dates
                      System.out.println("Please enter a day after today.\n");
                      continue;
                  }
              }
              return date;
          } catch (ParseException e) {
              System.out.println("Invalid date. Try again.\n"); // error message
              continue;
          }
      }
  }
  
  /**
   * Formats the doctor specialty output for display.
   *
   * @param specialty the doctor specialty
   * @return the formatted doctor specialty string
   */
  public static String formatSpecialtyOutput(String specialty) {
    
    String formattedSpecialty = "";
    // Allign the length of each specilty in display by padding blank spaces
    switch (specialty) {
      case "Family medicine":
        formattedSpecialty = "Family medicine               ";
        break;
      case "Cardiologist":
        formattedSpecialty = "Cardiologist                  ";
        break;
      case "Neurologist":
        formattedSpecialty = "Neurologist                   ";
        break;
      case "Pediatrician":
        formattedSpecialty = "Pediatrician                  ";
        break;
      case "Dentist":
        formattedSpecialty = "Dentist                       ";
        break;
      case "Dermatologist":
        formattedSpecialty = "Dermatologist                 ";
        break;
      case "Psychiatry":
        formattedSpecialty = "Psychiatry                   ";
        break;
      case "Infectious Disease Specialist":
        formattedSpecialty = "Infectious Disease Specialist";
        break;
      default: //case "Others":
        formattedSpecialty = "Others                       ";
      }
      return formattedSpecialty;
    }
  }
