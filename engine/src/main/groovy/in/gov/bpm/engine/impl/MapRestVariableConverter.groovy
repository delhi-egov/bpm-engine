package in.gov.bpm.engine.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.activiti.rest.service.api.engine.variable.RestVariable
import org.activiti.rest.service.api.engine.variable.RestVariableConverter
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by user-1 on 29/6/16.
 */
class MapRestVariableConverter implements RestVariableConverter {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    String getRestTypeName() {
        return 'map'
    }

    @Override
    Class<?> getVariableType() {
        return Map
    }

    @Override
    Object getVariableValue(RestVariable result) {
        if(result.getValue() != null) {
            return objectMapper.readValue((String)result.getValue(), new TypeReference<Map<String, Object>>(){});
        }
        return null;
    }

    @Override
    void convertVariableValue(Object variableValue, RestVariable result) {
        if(variableValue != null) {
            result.setValue(objectMapper.writeValueAsString(variableValue));
        }
        else {
            result.setValue(null);
        }
    }
}
