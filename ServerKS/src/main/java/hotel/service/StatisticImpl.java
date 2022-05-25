package hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hotel.repository.RoomBookingRepository;
import hotel.repository.UserRepository;

@Component
public class StatisticImpl implements StatisticService {

	@Autowired
	RoomBookingRepository bookingRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public Long getTotalTurnover() {
		return bookingRepository.countTurnover();
	}

	@Override
	public Long getTotalRevervationDone() {
		return bookingRepository.countRevervationDone();
	}

	@Override
	public Long getTotalAccountEnabled() {
		return userRepository.countTotalAccountEnabled();
	}

}
