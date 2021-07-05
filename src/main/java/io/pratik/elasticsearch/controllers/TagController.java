
package io.pratik.elasticsearch.controllers;

import io.pratik.elasticsearch.models.Product;
import io.pratik.elasticsearch.models.Tag;
import io.pratik.elasticsearch.services.ProductSearchService;
import io.pratik.elasticsearch.services.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@Slf4j
public class TagController {

	private TagService tagService;

	@Autowired
	public TagController(TagService tagService) {
	    this.tagService = tagService;
	}

	@GetMapping("/search")
	@ResponseBody
	public List<Tag> fetchByNameOrTag(@RequestParam(value = "q", required = false) String query) {
        log.info("searching by name {}",query);
		List<Tag> Tags = tagService.processSearch(query) ;
	    log.info("products {}",Tags);
		return Tags;
	  }
	
	@GetMapping("/suggestions")
	@ResponseBody
	public List<String> fetchSuggestions(@RequestParam(value = "q", required = false) String query) {                         
        log.info("fetch suggests {}",query);
        List<String> suggests = tagService.fetchSuggestions(query);
        log.info("suggests {}",suggests);
        return suggests;
	  }

	@GetMapping("/searchByContentType")
	@ResponseBody
	public ResponseEntity<Page<Tag>> fetchByContentType(
			@RequestParam(value = "q", required = false) String query,
			Pageable pageable) {
		return ResponseEntity.ok(tagService.fetchByContentType(query,pageable));
	}


	@GetMapping("/count")
	@ResponseBody
	public ResponseEntity<Long> count() {
		return ResponseEntity.ok(tagService.count());
	}

}
