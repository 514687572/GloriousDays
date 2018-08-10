package com.stip.net.user.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.stip.net.common.entity.Users;
import com.stip.net.common.service.user.UserService;
import com.stip.net.example.UsersExample;
import com.stip.net.user.dao.UsersDao;

@org.springframework.stereotype.Service
@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private UsersDao userDao;

	@Override
	public Users login(String userName, String password) {
		UsersExample example=new UsersExample();
		example.createCriteria().andUserNameEqualTo(userName).andPasswordEqualTo(password);
		
		List<Users> list=userDao.selectByExample(example);
		
		if(list!=null&&list.size()>0) {
			logger.info("just test log info");
			return list.get(0);
		}else {
			return null;
		}
	}
	
	@Override
	public int batchUpdaUser() {
		Users user=new Users();
		user.setUserStatus("1");
		UsersExample example=new UsersExample();
		example.createCriteria().andUserStatusIsNull();
		
		int updateCount=userDao.updateByExampleSelective(user, example);
		
		System.out.println("更新数据条数"+updateCount);
		
		int i=0/12;
		
		return updateCount;
	}
	
	@Override
	public int updateUserAge(int age) {
		Users user=new Users();
		user.setUserAge(age-1);
		UsersExample example=new UsersExample();
		example.createCriteria().andUserStatusIsNull();
		
		int updateCount=userDao.updateByExampleSelective(user, example);
		
		System.out.println("更新数据条数"+updateCount);
		
		return updateCount;
	}
	
	@Override
	public int batchInsert(List<Users> userList) {
		int updateCount=userDao.batchInsert(userList);
		
		System.out.println("批量插入数据"+updateCount);
		
		int i=0/12;
		
		return updateCount;
	}

	/* (non-Javadoc)
	 * @see com.stip.net.common.service.user.UserService#deleteAllUser()
	 */
	@Override
	public int deleteAllUser() {
		UsersExample example=new UsersExample();
		example.createCriteria().andUserStatusEqualTo("1");
		int delCount=userDao.deleteByExample(example);
		
		System.out.println("删除数据条数"+delCount);
		
		return delCount;
	}

}
