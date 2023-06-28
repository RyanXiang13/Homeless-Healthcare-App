/**
 * The Person class represents a person with basic information such as name, date of birth, and gender.
 */
class Person {
  
  /**
   * The name of the person.
   */
  protected String name;
  
  /**
   * The date of birth of the person.
   */
  protected String dob;
  
  /**
   * The gender of the person.
   */
  protected String gender; 

  /**
   * Constructs a Person object with the specified name, date of birth, and gender.
   *
   * @param name   the name of the person
   * @param dob    the date of birth of the person
   * @param gender the gender of the person
   */
  public Person(String name, String dob, String gender) {
    this.name = name;
    this.dob = dob;
    this.gender = gender;
  }

  /**
   * Constructs a Person object with default values for name, date of birth, and gender.
   */
  public Person() {
    this.name = "";
    this.dob = "";
    this.gender = "";
  }

  /**
   * Returns a string representation of the person in the format "name: dob".
   *
   * @return a string representation of the person
   */
  public String toString() {
    return this.name + ": " + this.dob;
  }

  /**
   * Returns the name of the person.
   *
   * @return the name of the person
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the date of birth of the person.
   *
   * @return the date of birth of the person
   */
  public String getDob() {
    return this.dob;
  }

  /**
   * Returns the gender of the person.
   *
   * @return the gender of the person
   */
  public String getGender() {
    return this.gender;
  }

  /**
   * Returns a unique identifier for the person based on the name and date of birth.
   *
   * @return a unique identifier for the person
   */
  public String getPersonID() {
    return this.name + "-" + this.dob;
  }    
}
