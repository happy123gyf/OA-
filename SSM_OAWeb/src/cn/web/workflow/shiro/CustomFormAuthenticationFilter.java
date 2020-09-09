package cn.web.workflow.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//自定义CustomFormAuthenticationFilter继承FormAuthenticationFilter
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter{
	//重写onAccessDenied方法 ,返回true表示不会调用realm
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
	/*	HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		//获取用户提交验证码
		String randomcode = req.getParameter("randomcode");
		//获取validatecode.jsp页面的验证码
		 String validateCode = (String) req.getSession().getAttribute("validateCode");
		 //验证码不相等时候执行
		if (validateCode!=null&& randomcode!=null &&!randomcode.equals(validateCode)) {
			//存放错误信息到request作用域
			req.setAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, "validateCodeError");
			return true;
		}
		*/

		return super.onAccessDenied(request, response); //执行默认操作,调用realm
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
									 ServletResponse response) throws Exception {
		WebUtils.getAndClearSavedRequest(request);
		WebUtils.redirectToSavedRequest(request, response, "/index");
		return false;
	}






}
