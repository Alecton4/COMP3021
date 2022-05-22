package midterm02;

import java.util.Objects;

class Student implements Comparable<Student> {
	private String name;
	private Integer age;
	private Double score;

	Student(String name, int age, double score) {
		this.name = name;
		this.age = age;
		this.score = score;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the score
	 */
	public Double getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return Objects.equals(age, other.age) && Objects.equals(name, other.name) && Objects.equals(score, other.score);
	}

	@Override
	public int compareTo(Student student) {
		// REVIEW Auto-generated method stub
		if (this.equals(student)) {
			return 0;
		} else if (!this.age.equals(student.getAge())) {
			if (this.age < student.getAge()) {
				return -1;
			} else {
				return 1;
			}
		} else if (!this.score.equals(student.getScore())) {
			if (this.score < student.getScore()) {
				return -1;
			} else {
				return 1;
			}
		} else {
			return this.getName().compareTo(student.getName());
		}
	}
}
