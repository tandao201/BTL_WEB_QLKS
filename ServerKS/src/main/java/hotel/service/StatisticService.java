package hotel.service;

import org.springframework.stereotype.Service;

@Service
public interface StatisticService {
	public Long getTotalTurnover();

	public Long getTotalRevervationDone();

	public Long getTotalAccountEnabled();
}
