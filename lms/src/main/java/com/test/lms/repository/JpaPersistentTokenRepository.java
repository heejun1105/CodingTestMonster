package com.test.lms.repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import com.test.lms.entity.PersistentLogins;

@Component
public class JpaPersistentTokenRepository implements PersistentTokenRepository {
	
	   private final PersistentLoginsRepository persistentLoginsRepository;

	    public JpaPersistentTokenRepository(PersistentLoginsRepository persistentLoginsRepository) {
	        this.persistentLoginsRepository = persistentLoginsRepository;
	    }
	
	    @Override
	    public void createNewToken(PersistentRememberMeToken token) {
	        PersistentLogins persistentLogins = new PersistentLogins();
	        persistentLogins.setSeries(token.getSeries());
	        persistentLogins.setUsername(token.getUsername());
	        persistentLogins.setToken(token.getTokenValue());
	        persistentLogins.setLastUsed(LocalDateTime.now());
	        persistentLoginsRepository.save(persistentLogins);
	    }

		@Override
	    public void updateToken(String series, String tokenValue, Date lastUsed) {
	        Optional<PersistentLogins> optionalPersistentLogins = persistentLoginsRepository.findBySeries(series);
	        if (optionalPersistentLogins.isPresent()) {
	            PersistentLogins persistentLogins = optionalPersistentLogins.get();
	            persistentLogins.setToken(tokenValue);
	            persistentLogins.setLastUsed(lastUsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	            persistentLoginsRepository.save(persistentLogins);
	        }
	    }
	
		@Override
		public PersistentRememberMeToken getTokenForSeries(String seriesId) {
	        Optional<PersistentLogins> optionalPersistentLogins = persistentLoginsRepository.findBySeries(seriesId);
	        if (optionalPersistentLogins.isPresent()) {
	            PersistentLogins persistentLogins = optionalPersistentLogins.get();
	            return new PersistentRememberMeToken(
	                persistentLogins.getUsername(),
	                persistentLogins.getSeries(),
	                persistentLogins.getToken(),
	                Date.from(persistentLogins.getLastUsed().atZone(ZoneId.systemDefault()).toInstant())
	            );
	        }
	        return null;
	    }
	
		@Override
		public void removeUserTokens(String username) {
	        persistentLoginsRepository.deleteByUsername(username);
	
		}

}
