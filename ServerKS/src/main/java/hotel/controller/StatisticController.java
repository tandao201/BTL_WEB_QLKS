package hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hotel.model.Statistic;
import hotel.service.StatisticService;

@RestController
@RequestMapping(path = "/api/statistic", produces = "application/json")
@CrossOrigin(origins = "*")
public class StatisticController {

	@Autowired
	private StatisticService statisticService;

	@GetMapping
	public ResponseEntity<?> getAllStatistic() {
		Statistic statistic = new Statistic();
		statistic.setTotalAccount(statisticService.getTotalAccountEnabled());
		statistic.setTotalRevervation(statisticService.getTotalRevervationDone());
		statistic.setTurnover(statisticService.getTotalTurnover());
		return ResponseEntity.ok(statistic);
	}
}
