/**
 * 
 */
package io.pratik.elasticsearch.services;

import io.pratik.elasticsearch.models.Tag;
import io.pratik.elasticsearch.repositories.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TagService {

	private static final String TAG_INDEX = "tagindex";

	private ElasticsearchOperations elasticsearchOperations;
	private TagRepository tagRepository;

	@Autowired
	public TagService(final ElasticsearchOperations elasticsearchOperations,
					  final TagRepository tagRepository) {
		super();
		this.elasticsearchOperations 	= elasticsearchOperations;
		this.tagRepository				= tagRepository;
	}

	public List<String> createTagIndexBulk(final List<Tag> tags) {

		List<IndexQuery> queries = tags.stream()
				.map(tag -> new IndexQueryBuilder().withId(tag.getId().toString()).withObject(tag).build())
				.collect(Collectors.toList());
		;

		return elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(TAG_INDEX));

	}

	public String createTagIndex(Tag tag) {

		IndexQuery indexQuery = new IndexQueryBuilder().withId(tag.getId().toString()).withObject(tag).build();
		String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of(TAG_INDEX));

		return documentId;
	}

	public void findTagsByUser(final String user) {
		QueryBuilder queryBuilder = QueryBuilders
				.matchQuery("user", user);
		// .fuzziness(0.8)
		// .boost(1.0f)
		// .prefixLength(0)
		// .fuzzyTranspositions(true);

		Query searchQuery = new NativeSearchQueryBuilder()
				.withQuery(queryBuilder)
				.build();

		SearchHits<Tag> productHits =
				elasticsearchOperations
				.search(searchQuery, Tag.class,
				  IndexCoordinates.of(TAG_INDEX));

		log.info("productHits {} {}", productHits.getSearchHits().size(), productHits.getSearchHits());

		List<SearchHit<Tag>> searchHits =
				productHits.getSearchHits();
		int i = 0;
		for (SearchHit<Tag> searchHit : searchHits) {
			log.info("searchHit {}", searchHit);
		}

	}


	public List<Tag> processSearch(final String query) {
		log.info("Search with query {}", query);
//
//
//		// 1. Create query on multiple fields enabling fuzzy search
//		QueryBuilder queryBuilder =
//				QueryBuilders
//				.matchQuery(query, "user");
//				//.fuzziness(Fuzziness.AUTO);
//
//		Query searchQuery = new NativeSearchQueryBuilder()
//				                .withFilter(queryBuilder)
//				                .build();
//
//		// 2. Execute search
//		SearchHits<Tag> productHits =
//				elasticsearchOperations
//				.search(searchQuery, Tag.class,
//				IndexCoordinates.of(TAG_INDEX));
//
//		// 3. Map searchHits to product list
//		List<Tag> tagMatches = new ArrayList<Tag>();
//		productHits.forEach(srchHit->{
//			tagMatches.add(srchHit.getContent());
//		});
		return tagRepository.findByUser(query);
	}

	

	
	public List<String> fetchSuggestions(String query) {
		QueryBuilder queryBuilder = QueryBuilders
				.wildcardQuery("tag", query+"*");

		Query searchQuery = new NativeSearchQueryBuilder()
				.withFilter(queryBuilder)
				.withPageable(PageRequest.of(0, 5))
				.build();

		SearchHits<Tag> searchSuggestions =
				elasticsearchOperations.search(searchQuery,
						Tag.class,
				IndexCoordinates.of(TAG_INDEX));
		
		List<String> suggestions = new ArrayList<String>();
		
		searchSuggestions.getSearchHits().forEach(searchHit->{
			suggestions.add(searchHit.getContent().getTag());
		});
		return suggestions;
	}

	public Page<Tag> fetchByContentType(String query, Pageable pageable) {
	return tagRepository.findByContentType(query,pageable);
	}

	public Long count() {
		return tagRepository.count();
	}
}
