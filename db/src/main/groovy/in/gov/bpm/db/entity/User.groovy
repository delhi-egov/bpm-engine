package in.gov.bpm.db.entity

import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Created by vaibhav on 20/7/16.
 */
@Entity
@Table(name = 'users')
@ToString
class User extends Auditable implements Serializable {

    @Column(name = "first_name")
    String firstName;
    @Column(name = "last_name")
    String lastName;
    @Column(unique = true)
    Long phone;
    String email;
    String password;
    String role;
}
