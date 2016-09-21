package in.gov.bpm.app.pojo

/**
 * Created by vaibhav on 31/7/16.
 */
class User implements Serializable {
    String username;
    String email;
    String firstName;
    String lastName;
    List<String> groups;
}
