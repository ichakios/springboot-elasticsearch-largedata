/**
 * 
 */
package io.pratik.elasticsearch.repositories;

import io.pratik.elasticsearch.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TagRepository extends ElasticsearchRepository<Tag, String> {
    List<Tag> findByUser(String user);

    List<Tag> findByTag(String tag);

    Page<Tag> findByContentType(String contentType, Pageable pageable);
}
