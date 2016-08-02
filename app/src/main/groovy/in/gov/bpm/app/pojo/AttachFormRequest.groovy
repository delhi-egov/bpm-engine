package in.gov.bpm.app.pojo

/**
 * Created by vaibhav on 3/8/16.
 */
class AttachFormRequest implements Serializable {
    String processInstanceId;
    String type;
    Map<String, Object> data;
}
