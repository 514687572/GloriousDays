package com.stip.net.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.stip.net.common.entity.Users;
import com.stip.net.common.pojo.SampleMessage;
import com.stip.net.common.result.StipResult;
import com.stip.net.common.service.kafka.KafkaProducerService;
import com.stip.net.common.service.redis.RedisService;
import com.stip.net.common.service.transaction.TransactionService;
import com.stip.net.common.service.user.UserService;
import com.stip.net.common.service.utils.Constant;
import com.stip.net.common.service.utils.KeyGenerator;
import com.stip.net.log.StipLogger;

@RestController
public class UserController {
	private final StipLogger logger = new StipLogger(this.getClass(),"gloriousDayLogs");
	
	@Reference(version = "1.0.0")
    private UserService userService;
	
	@Reference(version = "1.0.0")
	private RedisService redisService;
	
	@Reference(version = "1.0.0")
	private TransactionService transactionService;
	
	@Reference(version = "1.0.0")
	private KafkaProducerService kafkaProducerService;
	
    /** Session有效时间 */
    @Value("${session.expireTime}")
    private long sessionExpireTime;

    /** HTTP Response中Session ID 的名字 */
    @Value("${session.SessionIdName}")
    private String sessionIdName;
	
	@GetMapping("/login")
	public StipResult<Map<Object,Object>> userLogin(HttpServletRequest request,HttpServletResponse response) {
		String userName=request.getParameter("userName").toString();
		String password=request.getParameter("password").toString();
		
		Users user=userService.login(userName, password);
		
		logger.info(userName+"用户登录成功");
		
		if(user!=null) {
			doLoginSuccess(user,response);
			return StipResult.successResult();
		}else {
			return StipResult.failureResult();
		}
	}
	
	@GetMapping("/updateUserStatus")
	public void updateUserStatus(HttpServletRequest request,HttpServletResponse response) {
		int count=userService.batchUpdaUser();
		
		logger.info("更新用户信息");
	}
	
	@GetMapping("/sendLog")
	public void sendLog(HttpServletRequest request,HttpServletResponse response) {
		SampleMessage mesage=new SampleMessage(12312,"test logging");
		kafkaProducerService.send(mesage);
		
		logger.info("测试发送日志");
	}
	
	@GetMapping("/changeUserStatus")
	public void changeUserStatus(HttpServletRequest request,HttpServletResponse response) {
		String userName=request.getParameter("userName").toString();
		String password=request.getParameter("password").toString();
		Users user=userService.login(userName, password);
		
		if(user.getUserAge()>0) {
			userService.updateUserAge(user.getUserAge());
		}
	}
	
	@GetMapping("/deleteUserStatus")
	public void deleteUserStatus(HttpServletRequest request,HttpServletResponse response) {
		int count=userService.deleteAllUser();
		
		System.out.println("已经删除用户状态"+count);
	}
	
	@GetMapping("/batchInsert")
	public void batchInsert(HttpServletRequest request,HttpServletResponse response) {
		List<Users> userList=new ArrayList<Users>();
		
		for(int i=500;i<510;i++) {
			Users user=new Users();
			user.setPassword("123"+i);
			user.setUserId(100+i);
			user.setUserAge(i);
			user.setUserName("l"+i);
			user.setUserStatus("1");
			userList.add(user);
		}
		
		int count=userService.batchInsert(userList);
		
		System.out.println("已经删除用户状态"+count);
	}
	
	@GetMapping("/testSendMessage")
	public void testSendMessage(HttpServletRequest request,HttpServletResponse response) throws Exception {
		transactionService.sendMessage("test1", "helloWorld");
	}
	
	@GetMapping("/txTest")
	public void txTest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String a=transactionService.sendTransactionMessage("Rabbit-Transaction-queue", "testtx001", "order transaction");
		
		System.out.println(a);
		
		List<Users> userList=new ArrayList<Users>();
		Users user=new Users();
		user.setPassword("123123aa");
		user.setUserId(8798798);
		user.setUserAge(8);
		user.setUserName("king");
		user.setUserStatus("1");
		userList.add(user);
		
		int count=userService.batchInsert(userList);
		
		redisService.set("20180604", "20180604");
	}
	
    /**
     * 处理登录成功
     * @param userEntity 用户信息
     * @param httpRsp HTTP响应参数
     */
    private void doLoginSuccess(Users userEntity, HttpServletResponse httpRsp) {
        // 生成SessionID
        String sessionID = Constant.SessionID_Prefix + KeyGenerator.getKey();

        // 将 SessionID-UserEntity 存入Redis
        redisService.set(sessionID, userEntity, sessionExpireTime);

        // 将SessionID存入HTTP响应头
        Cookie cookie = new Cookie(sessionIdName, sessionID);
        httpRsp.addCookie(cookie);
    }

    /**
     * 获取SessionID
     * @param request 当前的请求对象
     * @return SessionID的值
     */
    private String getSessionID(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        // 遍历所有cookie，找出SessionID
        String sessionID = null;
        if (cookies!=null && cookies.length>0) {
            for (Cookie cookie : cookies) {
                if (sessionIdName.equals(cookie.getName())) {
                    sessionID = cookie.getValue();
                    break;
                }
            }
        }

        return sessionID;
    }

    /**
     * 获取SessionID对应的用户信息
     * @param sessionID
     * @return
     */
    private Users getUserEntity(String sessionID) {
        // SessionID为空
        if (StringUtils.isEmpty(sessionID)) {
            return null;
        }

        // 获取UserEntity
        Object userEntity = redisService.get(sessionID);
        if (userEntity==null) {
            return null;
        }
        return (Users) userEntity;
    }
}
