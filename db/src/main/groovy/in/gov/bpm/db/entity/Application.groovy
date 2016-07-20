package in.gov.bpm.db.entity

import groovy.transform.ToString

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

/**
 * Created by vaibhav on 20/7/16.
 */
@Entity
@Table(name = 'applications',  uniqueConstraints= @UniqueConstraint(columnNames=["type", "user_id"]))
@ToString
class Application extends Auditable implements Serializable {
    @Column(name = "submission_stage")
    String submissionStage;
    @Column(name = "execution_status")
    String executionStatus;
    String type;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = 'user_id')
    User user;
}
