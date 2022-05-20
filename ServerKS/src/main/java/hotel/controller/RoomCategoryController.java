package hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hotel.model.RoomCategories;
import hotel.repository.RoomCategoryRepository;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/categories", produces = "application/json")
public class RoomCategoryController {
	@Autowired
	RoomCategoryRepository repository;

	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public RoomCategories createCategory(@RequestBody RoomCategories category) {
		return repository.save(category);
	}

	@GetMapping()
	public ResponseEntity<?> getAllCategory() {
		List<RoomCategories> categories = repository.findAll();
		return ResponseEntity.ok(categories);
	}

	@DeleteMapping("/{id}")
	public void deleteCategory(@PathVariable("id") String id) {
		Long idUp = Long.valueOf(id);
		repository.deleteById(idUp);
	}
}
