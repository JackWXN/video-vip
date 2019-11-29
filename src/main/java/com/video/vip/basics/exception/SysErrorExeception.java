package com.video.vip.basics.exception;

import com.video.vip.basics.exception.base.VideoRuntimeException;
import com.video.vip.basics.util.enums.ResultEnum;

/**
 * 系统异常（严重异常）
 */
public class SysErrorExeception extends VideoRuntimeException {

	private static final long serialVersionUID = -7483284273236565805L;
	public SysErrorExeception() {
		super();
		setExptCode(ResultEnum.EXCEPTION_SYS.getCode()+"");
		setExptMsg(ResultEnum.EXCEPTION_SYS.getMsg());
	}
	public SysErrorExeception(String strExptMsg) {
		super();
		setExptCode(ResultEnum.EXCEPTION_SYS.getCode()+"");
		setExptMsg(strExptMsg);
	}
	public SysErrorExeception(String strExptCode, String strExptMsg) {
		super();
		setExptCode(strExptCode);
		setExptMsg(strExptMsg);
	}
	
}
