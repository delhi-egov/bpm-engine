package in.gov.bpm.db.entity

import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

/**
 * Created by vaibhav on 23/7/16.
 */
@Entity
@Table(name = 'otps', uniqueConstraints= @UniqueConstraint(columnNames= ["user_id"]))
@ToString
class Otp extends Auditable implements Serializable {
    Integer otp;
    @Column(name = "otp_count")
    Integer otpCount;
    @Column(name = "attempt_count")
    Integer attemptCount;
    @OneToOne
    @JoinColumn(name = 'user_id')
    User user;
}
