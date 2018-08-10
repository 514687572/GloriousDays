package com.stip.net.common.result;

import java.io.Serializable;

public class StipResult<T> implements Serializable {
    /** 执行结果 */
    private boolean success;

    /** 错误码 */
    private String errorCode;

    /** 错误原因 */
    private String message;

    /** 返回数据 */
    private T data;
    
    /**
     * 返回成功的结果
     * @param data 需返回的结果
     * @param <T>
     * @return
     */
    public static <T> StipResult<T> successResult(T data){
    	StipResult<T> result = new StipResult<>();
        result.success = true;
        result.data = data;
        return result;
    }

    /**
     * 返回成功的结果
     * @param <T>
     * @return
     */
    public static <T> StipResult<T> successResult(){
    	StipResult<T> result = new StipResult<>();
        result.success = true;
        result.errorCode="200";
        result.message="";
        result.data=null;
        
        return result;
    }
    
    /**
     * 返回失败结果
     * @param code
     * @param message
     * @return
     */
    public static <T> StipResult<T> failureResult(){
    	StipResult<T> result = new StipResult<>();
        result.success = false;
        result.errorCode="200";
        result.message="";
        result.data=null;
        
        return result;
    }

    /**
     * 返回包含错误信息结果
     * @param code
     * @param message
     * @return
     */
    public static <T> StipResult<T> failureResult(String code,String message){
    	StipResult<T> result = new StipResult<>();
        result.success = false;
        result.errorCode =code;
        result.message = message;
        
        return result;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
    
}
