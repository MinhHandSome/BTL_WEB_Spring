package qldt.data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qldt.Notification;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

}