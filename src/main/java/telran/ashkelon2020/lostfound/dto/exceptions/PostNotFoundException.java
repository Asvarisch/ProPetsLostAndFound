package telran.ashkelon2020.lostfound.dto.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PostNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4010005140315894295L;
	
	public PostNotFoundException(String id) {
		super("Post with id " + id + " not found");
	}

}
