package jp.co.internous.ecsite.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import jp.co.internous.ecsite.model.domain.MstUser;
import jp.co.internous.ecsite.model.form.LoginForm;

@Mapper
public interface MstUserMapper {
	//⑥
	@Select(value="SELECT * FROM mst_user Where user_name = #{userName} AND password = #{password}")
	MstUser findByUserNameAndPassword(LoginForm form);

}
