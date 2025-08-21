package org.example.kuit_kac.config;

import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Component("owner") // SpEL에서 @owner 로 접근
public class OwnerGuard {
    public boolean same(Long targetUserId, Authentication auth) {
        if (auth == null || auth.getPrincipal() == null) return false;
        Object p = auth.getPrincipal();
        if (p instanceof UserPrincipal up && up.getUserId() != null) {
            return Objects.equals(up.getUserId(), targetUserId);
        }
        return false;
    }
    // DTO에 getUserId()가 있으면 자동 비교
    public boolean sameBody(Object body, Authentication auth) {
        if (body == null) return false;
        try {
            Method m = body.getClass().getMethod("getUserId");
            Object v = m.invoke(body);
            Long target = (v instanceof Number n) ? n.longValue() : (v==null? null : Long.valueOf(v.toString()));
            return same(target, auth);
        } catch (NoSuchMethodException e) {
            return false; // userId 없는 바디면 불허(또는 true로 완화 가능)
        } catch (Exception e) {
            return false;
        }
    }
}

