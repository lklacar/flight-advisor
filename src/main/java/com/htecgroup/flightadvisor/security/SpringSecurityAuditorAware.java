package com.htecgroup.flightadvisor.security;

import com.htecgroup.flightadvisor.config.Constants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.Optional;


@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    @Nonnull
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin()
                .orElse(Constants.SYSTEM_ACCOUNT));
    }
}
