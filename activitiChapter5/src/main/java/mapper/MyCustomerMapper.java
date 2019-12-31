package mapper;

import org.activiti.bpmn.model.Task;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MyCustomerMapper {

    @Select("SELECT * FROM ACT_RU_TASK")
    public List<Map<String, Object>> findAll();
}
