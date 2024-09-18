//package com.datn.Configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//
//@Configuration
//@EnableJpaAuditing
//       // (auditorAwareRef = "auditorAware")
//public class JpaAuditingConfig {
////
////    @Bean
////    public AuditorAware<String> auditorAware() {
////        return () -> {
////            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////            if (authentication == null || !authentication.isAuthenticated()) {
////                return Optional.empty();
////            }
////            return Optional.ofNullable(authentication.getName());
////        };
////    }
//}
