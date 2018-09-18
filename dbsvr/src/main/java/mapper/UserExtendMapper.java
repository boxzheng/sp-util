package mapper;

import com.sit.entity.UserExtend;
import com.sit.entity.UserExtendExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserExtendMapper {
    int countByExample(UserExtendExample example);

    int deleteByExample(UserExtendExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserExtend record);

    int insertSelective(UserExtend record);

    List<UserExtend> selectByExample(UserExtendExample example);

    UserExtend selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserExtend record, @Param("example") UserExtendExample example);

    int updateByExample(@Param("record") UserExtend record, @Param("example") UserExtendExample example);

    int updateByPrimaryKeySelective(UserExtend record);

    int updateByPrimaryKey(UserExtend record);
}