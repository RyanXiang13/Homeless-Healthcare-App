import java.io.*;
import java.util.*;

/**
 * Represents an address.
 */
class Address {

  private String streetNumber;
  private String streetName;
  private String city;
  private String province;
  private String postalCode;

  /**
   * Constructs an Address object with the specified attributes.
   *
   * @param streetNumber the street number of the address
   * @param streetName   the street name of the address
   * @param city         the city of the address
   * @param province     the province of the address
   * @param postalCode   the postal code of the address
   */
  public Address(String streetNumber, String streetName, String city, String province, String postalCode) {
    this.streetNumber = streetNumber;
    this.streetName = streetName;
    this.city = city;
    this.province = province;
    this.postalCode = postalCode;
  }

  /**
   * Gets the street number of the address.
   *
   * @return the street number of the address
   */
  public String getStreetNumber() {
    return this.streetNumber;
  }

  /**
   * Gets the street name of the address.
   *
   * @return the street name of the address
   */
  public String getStreetName() {
    return this.streetName;
  }

  /**
   * Gets the city of the address.
   *
   * @return the city of the address
   */
  public String getCity() {
    return this.city;
  }

  /**
   * Gets the province of the address.
   *
   * @return the province of the address
   */
  public String getProvince() {
    return this.province;
  }

  /**
   * Gets the postal code of the address.
   *
   * @return the postal code of the address
   */
  public String getPostalCode() {
    return this.postalCode;
  }

  /**
   * Returns a string representation of the address.
   *
   * @return a string representation of the address
   */
  public String toString() {
    return this.streetNumber + " " + this.streetName + "\n" +
            this.city + " " + this.province + " " + this.postalCode;
  }

  /**
   * Gets the formatted address.
   *
   * @return the formatted address
   */
  public String getAddress() {
    return this.streetNumber + "," + this.streetName + "," +
            this.city + "," + this.province + "," + this.postalCode;
  }

  /**
   * Saves the address to the database.
   *
   * @param person the person associated with the address
   */
  public static void saveAddressToDatabase(Person person) {
    try {
      File file = new File("Data/addresses.txt");
      FileWriter fw = new FileWriter(file, true);

      if (person instanceof HomelessPerson) {
        Address addr = ((HomelessPerson) person).getResidenceAddress();
        fw.write(((HomelessPerson) person).getPersonID() + "," +
                addr.getAddress() + "\n");
      } else if (person instanceof Doctor) {
        Address addr = ((Doctor) person).getOfficeAddress();
        fw.write(((Doctor) person).getPersonID() + "," + addr.getAddress() + "\n");
      } else {
        System.out.println("Invalid address information. Nothing saved to the database");
      }

      fw.close();
    } catch (IOException e) {
      System.out.println("File does not exist");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  /**
   * Searches for the address associated with the person.
   *
   * @param person the person to search for
   * @return the address associated with the person if found, or null otherwise
   */
  public static Address searchAddress(Person person) {

    // Get person ID based on type of the Person object. The person ID will be used
    // as a unique identifier to retrieve the address associated with a person
    String myPersonID = "";
    if (person instanceof HomelessPerson) {
      myPersonID = person.getPersonID();
    } else if (person instanceof Doctor) {
      myPersonID = ((Doctor) person).getPersonID();
    }

    String line;
    try {
      
      BufferedReader br = new BufferedReader(new FileReader("Data/addresses.txt"));
      // Traverse the address database to find the address associated with the person ID
      // above
      while ((line = br.readLine()) != null) {
        String[] arr = line.split(",");
        String personID = arr[0];
        String streetNum = arr[1];
        String streetName = arr[2];
        String city = arr[3];
        String province = arr[4];
        String postalCode = arr[5];
        if (personID.equals(myPersonID)) {

          // An address matching the person ID is found in the database
          Address myAddr = new Address(streetNum, streetName, city, province, postalCode);
          return myAddr;
        }
      }
      // Close the BufferedReader to release resource
      br.close();
    } catch (IOException e) {
      System.out.println("File does not exist");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }

    // No address matching the person ID is found
    return null;
  }
}
