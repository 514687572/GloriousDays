package com.stip.net.common.service.user;

import java.util.List;

import com.stip.net.common.entity.Users;

public interface UserService {
	/**
	 * 用户登录
	 * @param userName
	 * @param password
	 * @return
	 */
    public Users login(final String userName, final String password);
    
    /**
     * 批量更新用户状态信息
     * @param userName
     * @param password
     */
    public int batchUpdaUser();
    
    /**
     * 删除所有用户
     * @param userName
     * @param password
     */
    public int deleteAllUser();

	/**
	 * @param userList
	 * @return
	 */
	int batchInsert(List<Users> userList);
	
	public int updateUserAge(int age);
}
