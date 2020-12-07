package telran.ashkelon2020.lostfound.dao;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import telran.ashkelon2020.lostfound.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
	
	Page<Post> findByIdNotNullAndTypePost(boolean typePost, Pageable paging);
	
	@Query("{'typePost': ?0, 'type': ?1, 'breed': ?2, 'tags': { $all: ?3 }, 'location': {$near: {$geometry: {'type': 'Point', coordinates: ?4}, $maxDistance: ?5}}}")
	Page<Post> filterSearch(boolean typePost, String type, String breed, Set<String> tags, Double[] point, Integer maxDistance, Pageable paging);

	List<Post> findByIdIn(List<String> postIds);

}
