package model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class Token {	
	private String token;
	private LocalDateTime creationDateTime;
	private int expiryTimeMinutes;
	
	public Token(int expiryTimeMinutes)
	{	//Zufälliger Token generieren mit Java-Klasse : Optional
		token = UUID.randomUUID().toString();
		token = token.toUpperCase();
		token = token.replace("-", "");
		creationDateTime = LocalDateTime.now();
		this.expiryTimeMinutes = expiryTimeMinutes;
	}
	
	public boolean isValid() {
		long elapsedMinutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), creationDateTime);
		return elapsedMinutes < expiryTimeMinutes;
	}

	@Override
	public String toString() {
		return token;
	}
}
