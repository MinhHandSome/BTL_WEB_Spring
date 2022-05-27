package qldt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qldt.Notification;
import qldt.Student;
import qldt.data.NotificationRepo;




@Service
public class NotificationSer {
	@Autowired
	private NotificationRepo notificationRepo;

	public Notification addNotification(Notification notification) {
		return notificationRepo.save(notification);
	}

	public List<Notification> getNotificationt() {
		return notificationRepo.findAll();
	}
	public Notification getNodByID(long ID) {

		Optional<Notification> model = notificationRepo.findById(ID);

		if (model.isPresent()) {
			return model.get();
		}
		return null;
	}
	public void deleteNotificationById(Long ID) {
		notificationRepo.deleteById(ID);

	}
}
