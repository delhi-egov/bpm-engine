package in.gov.bpm.db.entity

import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

/**
 * Created by vaibhav on 23/7/16.
 */
@Entity
@Table(name = 'application_types')
@ToString
class ApplicationType extends Auditable implements Serializable {

    @Column(unique = true)
    String name;

    @Column(name = "requires_payment")
    Boolean requiresPayment;

    @Column(name = "payment_amount")
    BigDecimal paymentAmount;

    @Column(name = "bpm_process")
    String bpmProcess;
}
