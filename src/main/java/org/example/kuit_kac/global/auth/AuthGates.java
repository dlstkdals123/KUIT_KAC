//package org.example.kuit_kac.global.auth;
//
//import org.example.kuit_kac.domain.user.model.UserPrincipal;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//
//public final class AuthGates {
//    private AuthGates() {}
//
//    public static boolean isDev(Authentication auth) {
//        if (auth == null) return false;
//        for (GrantedAuthority a : auth.getAuthorities()) {
//            if ("ROLE_DEV".equals(a.getAuthority())) return true;
//        }
//        return false;
//    }
//
//    /** 온보딩 필요 여부: DEV는 무조건 우회 */
//    public static boolean onboardingRequired(UserPrincipal p, Authentication auth) {
//        if (isDev(auth)) return false;
//        return p != null && p.isOnboardingNeeded();
//    }
//
//    /** 약관 필요 여부: DEV는 무조건 우회 (당장 terms 로직 유지 시) */
//    public static boolean termsRequired(UserPrincipal p, Authentication auth) {
//        if (isDev(auth)) return false;
//        return p != null && !p.isTermsAgreed();
//    }
//}
