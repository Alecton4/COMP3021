package main.java;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;

public class QuarantineSystem {
    public static class DashBoard {
        List<Person> People;
        List<Integer> patientNums;
        List<Integer> infectNums;
        List<Double> infectAvgNums;
        List<Integer> vacNums;
        List<Integer> vacInfectNums;

        public DashBoard(List<Person> p_People) {
            this.People = p_People;
            this.patientNums = new ArrayList<>(8);
            this.infectNums = new ArrayList<>(8);
            this.infectAvgNums = new ArrayList<>(8);
            this.vacNums = new ArrayList<>(8);
            this.vacInfectNums = new ArrayList<>(8);
        }

        public void runDashBoard() {
            for (int i = 0; i < 8; i++) {
                this.patientNums.add(0);
                this.infectNums.add(0);
                this.vacNums.add(0);
                this.vacInfectNums.add(0);
            }

            /*
             * TODO: Collect the statistics based on People
             * Add the data in the lists, such as patientNums, infectNums, etc.
             */
        }
    }

    private List<Person> People;
    private List<Patient> Patients;
    private List<Hospital> Hospitals;
    private List<Record> Records;

    private int newHospitalNum;

    private Map<String, Person> PeopleMap;
    private Map<String, Patient> PatientsMap;
    private Map<String, Hospital> HospitalsMap;

    private DashBoard dashBoard;

    public QuarantineSystem() throws IOException {
        importPeople();
        importHospital();
        importRecords();
        this.dashBoard = null;
        this.Patients = new ArrayList<>();

        // maintain map for easy look up
        this.PeopleMap = People.stream()
                .collect(Collectors.toMap(Person::getIDCardNo, Function.identity(), (key1, key2) -> key2));
        this.PatientsMap = new HashMap<>();
        this.HospitalsMap = Hospitals.stream()
                .collect(Collectors.toMap(Hospital::getHospitalID, Function.identity(), (key1, key2) -> key2));
    }

    public void startQuarantine() throws IOException {
        /*
         * Task 1: Saving Patients
         */
        System.out.println("Task 1: Saving Patients");
        /*
         * REVIEW: Process each record
         */
        for (Record record : this.Records) {
            switch (record.getStatus()) {
                case Confirmed:
                    this.saveSinglePatient(record);
                    break;
                case Recovered:
                    this.releaseSinglePatient(record);
                    break;
                default:
                    System.out.println("Something is WRONG in this record!");
                    break;
            }
        }
        exportRecordTreatment();

        /*
         * Task 2: Displaying Statistics
         */
        System.out.println("Task 2: Displaying Statistics");
        this.dashBoard = new DashBoard(this.People);
        this.dashBoard.runDashBoard();
        exportDashBoard();
    }

    /*
     * REVIEW: Save a single patient when the status of the record is Confirmed
     */
    public void saveSinglePatient(Record record) {
        // get the person corresponding to the ID
        Person person = this.PeopleMap.get(record.getIDCardNo());

        // find the nearest available hospital
        SymptomLevel symptomLevel = record.getSymptomLevel();
        int minDis = Integer.MAX_VALUE;
        Hospital nearestHospital = null;
        for (Hospital hospital : this.Hospitals) {
            if (hospital.getCapacity().getSingleCapacity(symptomLevel) > 0) {
                int distance = person.getLoc().getDisSquare(hospital.getLoc());
                if (distance < minDis) {
                    minDis = distance;
                    nearestHospital = hospital;
                }
            }
        }

        // if no such hospital, create a new hospital
        if (nearestHospital == null) {
            newHospitalNum++;
            String newHosID = "H-New-" + newHospitalNum;
            Capacity newHosCap = new Capacity(5, 10, 20);
            minDis = 0;
            // Hospitals.add(new Hospital(newHosID, person.getLoc(), newHosCap));
            // nearestHospital = Hospitals.get(Hospitals.size() - 1);
            // ??? is this doable
            nearestHospital = new Hospital(newHosID, person.getLoc(), newHosCap);
            this.Hospitals.add(nearestHospital);
            this.HospitalsMap.put(newHosID, nearestHospital);
        }

        // add person as patient
        Patient patient = new Patient(person, symptomLevel);
        patient.setHospitalID(nearestHospital.getHospitalID());
        nearestHospital.addPatient(patient);
        this.Patients.add(patient);
        this.PatientsMap.put(patient.getIDCardNo(), patient);

        // set Hospital ID in corresponding record;
        record.setHospitalID(nearestHospital.getHospitalID());
    }

    /*
     * REVIEW: Release a single patient when the status of the record is Recovered
     */
    public void releaseSinglePatient(Record record) {
        // get the patient corresponding to the ID
        Patient patient = this.PatientsMap.get(record.getIDCardNo());
        // get the corresponding hospital
        Hospital hospital = this.HospitalsMap.get(patient.getHospitalID());

        // release patient from hospital
        hospital.releasePatient(patient);
        this.Patients.remove(patient);
        this.PatientsMap.remove(patient.getIDCardNo());

        // set Hospital ID in corresponding record;
        record.setHospitalID(hospital.getHospitalID());
    }

    /*
     * Import the information of the people in the area from Person.txt
     * The data is finally stored in the attribute People
     * You do not need to change the method.
     */
    public void importPeople() throws IOException {
        this.People = new ArrayList<>();
        File filename = new File("./assignment1/data/Person.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int lineNum = 0;

        while (line != null) {
            lineNum++;
            if (lineNum > 1) {
                String[] records = line.split("        ");
                assert (records.length == 6);
                String pIDCardNo = records[0];
                System.out.println(pIDCardNo);
                int XLoc = Integer.parseInt(records[1]);
                int YLoc = Integer.parseInt(records[2]);
                Location pLoc = new Location(XLoc, YLoc);
                assert (records[3].equals("Male") || records[3].equals("Female"));
                String pGender = records[3];
                int pAge = Integer.parseInt(records[4]);
                assert (records[5].equals("Yes") || records[5].equals("No"));
                boolean pIsVac = (records[5].equals("Yes"));
                Person p = new Person(pIDCardNo, pLoc, pGender, pAge, pIsVac);
                this.People.add(p);
            }
            line = br.readLine();
        }
    }

    /*
     * Import the information of the records
     * The data is finally stored in the attribute Records
     * You do not need to change the method.
     */
    public void importRecords() throws IOException {
        this.Records = new ArrayList<>();

        File filename = new File("./assignment1/data/Record.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int lineNum = 0;

        while (line != null) {
            lineNum++;
            if (lineNum > 1) {
                String[] records = line.split("        ");
                assert (records.length == 3);
                String pIDCardNo = records[0];
                System.out.println(pIDCardNo);
                assert (records[1].equals("Critical") || records[1].equals("Moderate") || records[1].equals("Mild"));
                assert (records[2].equals("Confirmed") || records[2].equals("Recovered"));
                Record r = new Record(pIDCardNo, records[1], records[2]);
                Records.add(r);
            }
            line = br.readLine();
        }
    }

    /*
     * Import the information of the hospitals
     * The data is finally stored in the attribute Hospitals
     * You do not need to change the method.
     */
    public void importHospital() throws IOException {
        this.Hospitals = new ArrayList<>();
        this.newHospitalNum = 0;

        File filename = new File("./assignment1/data/Hospital.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int lineNum = 0;

        while (line != null) {
            lineNum++;
            if (lineNum > 1) {
                String[] records = line.split("        ");
                assert (records.length == 6);
                String pHospitalID = records[0];
                System.out.println(pHospitalID);
                int XLoc = Integer.parseInt(records[1]);
                int YLoc = Integer.parseInt(records[2]);
                Location pLoc = new Location(XLoc, YLoc);
                int pCritialCapacity = Integer.parseInt(records[3]);
                int pModerateCapacity = Integer.parseInt(records[4]);
                int pMildCapacity = Integer.parseInt(records[5]);
                Capacity cap = new Capacity(pCritialCapacity, pModerateCapacity, pMildCapacity);
                Hospital hospital = new Hospital(pHospitalID, pLoc, cap);
                this.Hospitals.add(hospital);
            }
            line = br.readLine();
        }
    }

    /*
     * Export the information of the records
     * The data is finally dumped into RecordTreatment.txt
     * !!! DO NOT change the functionality of the method
     * Otherwise, you may generate wrong results in Task 1
     */
    public void exportRecordTreatment() throws IOException {
        File filename = new File("./assignment1/output/my_RecordTreatment.txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename));
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write("IDCardNo        SymptomLevel        Status        HospitalID\n");
        for (Record record : Records) {
            // Invoke the toString method of Record.
            bw.write(record.toString() + "\n");
        }
        bw.close();
    }

    /*
     * Export the information of the dashboard
     * The data is finally dumped into Statistics.txt
     * !!! DO NOT change the functionality of the method
     * Otherwise, you may generate wrong results in Task 2
     */
    public void exportDashBoard() throws IOException {
        File filename = new File("./assignment1/output/my_Statistics.txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename));
        BufferedWriter bw = new BufferedWriter(writer);

        bw.write("AgeRange        patientNums        infectAvgNums        vacNums        vacInfectNums\n");

        for (int i = 0; i < 8; i++) {
            String ageRageStr = "";
            switch (i) {
                case 0:
                    ageRageStr = "(0, 10)";
                    break;
                case 7:
                    ageRageStr = "[70, infinite)";
                    break;
                default:
                    ageRageStr = "[" + String.valueOf(i) + "0, " + String.valueOf(i + 1) + "0)";
                    break;
            }
            String patientNumStr = String.valueOf(dashBoard.patientNums.get(i));
            String infectAvgNumsStr = String.valueOf(dashBoard.infectAvgNums.get(i));
            String vacNumsStr = String.valueOf(dashBoard.vacNums.get(i));
            String vacInfectNumsStr = String.valueOf(dashBoard.vacInfectNums.get(i));

            bw.write(ageRageStr + "        " + patientNumStr + "        " + infectAvgNumsStr + "        " + vacNumsStr
                    + "        " + vacInfectNumsStr + "\n");
        }

        bw.close();
    }

    /* The entry of the project */
    public static void main(String[] args) throws IOException {
        QuarantineSystem system = new QuarantineSystem();
        System.out.println("Start Quarantine System");
        system.startQuarantine();
        System.out.println("Quarantine Finished");
    }
}
