package kr.hhplus.be.server.interfaces.resolver;

import jakarta.servlet.http.HttpServletRequest;
import kr.hhplus.be.server.interfaces.info.UserInfo;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Component
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String userIdHeader = request.getHeader("Authorization");

        if (userIdHeader != null && userIdHeader.startsWith("Bearer ")) {
            String userId = userIdHeader.substring(7); // "Bearer " 이후 값 추출
            return new UserInfo(Long.parseLong(userId)); // UserInfo 객체 반환
        }

        throw new IllegalArgumentException("Authorization 헤더가 없거나 잘못되었습니다.");
    }
}