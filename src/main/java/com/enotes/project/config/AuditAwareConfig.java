package com.enotes.project.config;
import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditAwareConfig implements AuditorAware<Integer>{

    @SuppressWarnings("null")
    @Override
    public Optional<Integer> getCurrentAuditor() {
        return Optional.of(1);
    }

   

}
