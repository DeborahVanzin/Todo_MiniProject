package login;

import model.Token;

public interface ILoginHandler {
	public Token login(String email, String password);
	public String validateToken(String token);
}
