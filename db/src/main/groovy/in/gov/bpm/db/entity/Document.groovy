package in.gov.bpm.db.entity

import groovy.transform.ToString

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

/**
 * Created by vaibhav on 20/7/16.
 */
@Entity
@Table(name = 'documents',  uniqueConstraints= @UniqueConstraint(columnNames=["type", "application_id"]))
@ToString
class Document extends Auditable implements Serializable {
    String type;
    String path;
    @OneToOne
    @JoinColumn(name = "application_id")
    Application application;
}
