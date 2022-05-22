package midterm02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Test {
	static ArrayList<Student> parseStudents(String filename) throws IOException {
		ArrayList<Student> students = new ArrayList<Student>();

		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = reader.readLine();
		while (line != null) {
			if (!line.equals("\n")) {
				String[] splits = line.split(":");
				students.add(new Student(splits[0], Integer.parseInt(splits[1]), Double.parseDouble(splits[2])));
			}
			// read next line
			line = reader.readLine();
		}
		reader.close();

		return students;
	}

	static void runStudentTest(int idx) throws IOException {
		ArrayList<Student> students = parseStudents("students.txt");
		ArrayList<Student> students_result = parseStudents("results_" + idx + ".txt");
		SortedList<Student> l1 = new SortedList<Student>(students);
		if (l1.getList().equals(students_result)) {
			System.out.println("Test student passed!");
		} else {
			System.out.println("Test student failed!");
		}
	}

	public static void main(String[] args) throws IOException {
		runStudentTest(15);
	}
}
