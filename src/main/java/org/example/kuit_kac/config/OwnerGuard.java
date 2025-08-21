package org.example.kuit_kac.config;

import org.example.kuit_kac.domain.user.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;

@Component("owner") // SpEL에서 @owner 로 접근
@Slf4j
public class OwnerGuard {
    private static final Logger log = LoggerFactory.getLogger(OwnerGuard.class);

    public OwnerGuard() {
        System.out.println("=== OwnerGuard 생성자 호출됨 ===");
        log.info("OwnerGuard 빈이 생성되었습니다.");
    }

    public boolean same(Long targetUserId, Authentication auth) {
        // 강제 출력으로 확인
        System.out.println("=== OwnerGuard.same() 호출됨 ===");
        System.out.println("targetUserId: " + targetUserId);
        System.out.println("auth: " + auth);
        
        log.info("OwnerGuard.same() 호출됨 - targetUserId: {}, auth: {}", targetUserId, auth);
        
        if (auth == null || auth.getPrincipal() == null) {
            log.warn("인증 정보가 null입니다 - auth: {}, principal: {}", auth, auth != null ? auth.getPrincipal() : "N/A");
            return false;
        }
        
        Object p = auth.getPrincipal();
        log.info("Principal 객체: {}, 클래스: {}", p, p != null ? p.getClass().getSimpleName() : "N/A");
        
        if (p instanceof UserPrincipal up && up.getUserId() != null) {
            log.info("UserPrincipal 확인됨 - userId: {}, targetUserId: {}, 일치여부: {}", 
                    up.getUserId(), targetUserId, Objects.equals(up.getUserId(), targetUserId));
            return Objects.equals(up.getUserId(), targetUserId);
        }
        
        log.warn("UserPrincipal이 아니거나 userId가 null입니다 - principal: {}, instanceof UserPrincipal: {}", 
                p, p instanceof UserPrincipal);
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

    public boolean test() {
        System.out.println("=== OwnerGuard.test() 호출됨 ===");
        log.info("OwnerGuard.test() 호출됨");
        return true;
    }
}

