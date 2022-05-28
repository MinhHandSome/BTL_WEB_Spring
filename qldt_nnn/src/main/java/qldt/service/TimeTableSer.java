package qldt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qldt.ClassHP;
import qldt.Student;
import qldt.TimeTable;
import qldt.data.TimeTableRepo;

@Service
public class TimeTableSer {
	@Autowired
	private TimeTableRepo timetableRepo;

	public TimeTable addTimeTable(TimeTable timetable) {
		return timetableRepo.save(timetable);
	}

	public List<TimeTable> getTimeTable() {
		return timetableRepo.findAll();
	}

	public TimeTable getTimeTableByID(long ID) {

		Optional<TimeTable> model = timetableRepo.findById(ID);

		if (model.isPresent()) {
			return model.get();
		}
		return null;
	}

	public void deleteByTimeTableId(Long ID) {
		timetableRepo.deleteById(ID);

	}
	public List<TimeTable> findTimeTablebyStudenT(Student student){
		List<TimeTable> timetables1 = timetableRepo.findAll();
		List<TimeTable> timetables =new ArrayList<>();
		for(TimeTable timetable: timetables1) {
			if(timetable.getStudent().getID()==student.getID()) {
				timetables.add(timetable);
			}
		}
		return timetables;
	}
	public List<TimeTable> findTimeTablebyClassHP(ClassHP classHP){
		List<TimeTable> timetables1 = timetableRepo.findAll();
		List<TimeTable> timetables =new ArrayList<>();
		for(TimeTable timetable: timetables1) {
			if(timetable.getClassHP().getID()==classHP.getID()) {
				timetables.add(timetable);
			}
		}
		return timetables;
	}

}
