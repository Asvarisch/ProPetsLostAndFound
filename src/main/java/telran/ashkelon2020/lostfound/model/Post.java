package telran.ashkelon2020.lostfound.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Document(collection = "lostAndFoundPosts")
public class Post implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4193242471949278006L;
	@Id
	String id;
	boolean typePost;
	String userLogin;
	String userName;
	String avatar;
	LocalDateTime datePost;
	String type;
	String sex;
	String breed;
	Set<String> tags = new HashSet<>();
	List<String> photos = new ArrayList<>();
	Address address;
	Location location;

}
