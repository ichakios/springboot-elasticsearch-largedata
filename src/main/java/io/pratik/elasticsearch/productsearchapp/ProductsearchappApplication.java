package io.pratik.elasticsearch.productsearchapp;

import java.io.InputStream;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import io.pratik.elasticsearch.models.Tag;
import io.pratik.elasticsearch.repositories.TagRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import io.pratik.elasticsearch.models.Product;
import io.pratik.elasticsearch.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ProductsearchappApplication {
	
	private static final String COMMA_DELIMITER = ",";

	@Autowired
	private ElasticsearchOperations esOps;

	@Autowired
	private ProductRepository productRepo;


	@Autowired
	private TagRepository tagRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProductsearchappApplication.class, args);
	}
	
	@PreDestroy
	public void deleteIndex() {
		esOps.indexOps(Product.class).delete();
	}
	
	
	@PostConstruct
	public void buildIndex() {

		esOps.indexOps(Product.class).refresh();
		productRepo.deleteAll();
		productRepo.saveAll(prepareDataset());

		Long tagsCount = tagRepository.count();
		System.out.println("\n\n\n\n\n tagsCount["+tagsCount+"]");
		if(tagsCount > 0 )return ;

		esOps.indexOps(Tag.class).refresh();
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
		tagRepository.saveAll(prepareTags());
	}

	private Collection<Tag> prepareTags() {


		List<Tag> tagsList = new ArrayList<Tag>();

		Tag tag = null;
		String[] tags = new String[]{"Favorite","Dislike"};
		String[] cType = new String[]{"track","video"};
		Random ran = new Random();
		try{

		for(int i = 0; i < 100000 ; i++){
			tag = new Tag(UUID.randomUUID().toString(),
					UUID.randomUUID().toString(),
					tags[(i%2)],
					Calendar.getInstance().getTime(),
					cType[(i%2)],
					cType[(i%2)]+ "_" + ran.nextInt(900000000) );
			tagsList.add(tag);
		}



		} catch (Exception e) {
			log.error("File read error {}",e);;
		}
		return tagsList;
	}

	private Collection<Product> prepareDataset() {
		Resource resource = new ClassPathResource("fashion-products.csv");
		List<Product> productList = new ArrayList<Product>();

		try (
			InputStream input = resource.getInputStream();
			Scanner scanner = new Scanner(resource.getInputStream());) {
			int lineNo = 0;
			while (scanner.hasNextLine()) {
				++lineNo;				
				String line = scanner.nextLine();
				if(lineNo == 1) continue;
				Optional<Product> product = 
						csvRowToProductMapper(line);
				if(product.isPresent())
				productList.add(product.get());
			}
		} catch (Exception e) {
			log.error("File read error {}",e);;
		}
		return productList;
	}

	private Optional<Product> csvRowToProductMapper(final String line) {
		try (			
			Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(COMMA_DELIMITER);
			while (rowScanner.hasNext()) {
				String name = rowScanner.next();
				String description = rowScanner.next();
				String manufacturer = rowScanner.next();
				return Optional.of(
						Product.builder()
						.name(name)
						.description(description)
						.manufacturer(manufacturer)
						.build());

			}
		}
		return Optional.of(null);
	}

}
